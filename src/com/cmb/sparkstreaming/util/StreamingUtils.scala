package com.cmb.sparkstreaming.util

import java.util

import kafka.common.TopicAndPartition
import kafka.message.MessageAndMetadata
import kafka.serializer.StringDecoder
import kafka.utils.ZkUtils
import org.I0Itec.zkclient.ZkClient
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.dstream.InputDStream
import org.apache.spark.streaming.kafka.{KafkaUtils, OffsetRange}

/**
  * Created by 80374643 on 2017/5/10.
  */
object StreamingUtils {
  var zkClient : ZkClient = null

  def zkInit(settings: util.HashMap[String, String]) = {
    if (zkClient == null) {
      val zkHosts = settings.get("zk_hosts")
      zkClient = new ZkClient(zkHosts, 180000, 180000)
    }
  }

  def createDirectStream(ssc: StreamingContext, kafkaParams: Map[String, String], topicSet : Set[String], settings: util.HashMap[String, String]): InputDStream[(String, String)] = {
    var stream: InputDStream[(String, String)] = null
    val isUpgraded = settings.get("app_upgraded").toBoolean
    if (isUpgraded) {
      zkInit(settings)
      val zkPath = s"${settings.get("zk_path")}/${settings.get("app_name")}"
      var fromOffsets: Map[TopicAndPartition, Long] = Map()
      for (topic <- topicSet) {
        val topicPath = s"${zkPath}/${topic}"
        val children = zkClient.countChildren(topicPath)
        for (i <- 0 until children) {
          val partitionOffset = zkClient.readData[String](s"${topicPath}/${i}")
          val tp = TopicAndPartition(topic, i)
          fromOffsets += (tp -> partitionOffset.toLong)
          println("topic[" + topic + "] partition[" + i + "] offset[" + partitionOffset + "]")
        }
      }
      val messageHandler = (mmd: MessageAndMetadata[String, String]) => (mmd.topic, mmd.message())
      stream = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder, (String, String)](ssc, kafkaParams, fromOffsets, messageHandler)
    } else {
      //create input kafka direct stream
      stream = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](
        ssc, kafkaParams, topicSet)
    }
    stream
  }

  def saveOffset(offsetRanges: Array[OffsetRange], settings: util.HashMap[String, String]) = {
    zkInit(settings)
    val zkPath = s"${settings.get("zk_path")}/${settings.get("app_name")}"
    try {
      for (o <- offsetRanges) {
        val path = s"${zkPath}/${o.topic}/${o.partition}"
        ZkUtils.updatePersistentPath(zkClient, path, o.untilOffset.toString)
        Log.info(this.getClass, s"topic  ${o.topic}  partition ${o.partition}  from offset ${o.fromOffset}  until offset ${o.untilOffset}")
      }
    } catch {
      case e: Exception => {
        Log.info(getClass, "reconnect to zookeeper.")
        zkClient = null
        zkInit(settings)
      }
    }
  }
}
