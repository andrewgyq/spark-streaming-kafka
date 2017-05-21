package com.cmb.sparkstreaming.subtopic

import java.util.HashMap

import com.alibaba.fastjson.JSON
import com.cmb.sparkstreaming.config.{ReadMySQLAppConf, RedisOperator}
import com.cmb.sparkstreaming.util.{KafkaClient, KafkaFactory, StreamingUtils}
import org.apache.spark.streaming.{Seconds, StreamingContext}

import scala.collection.mutable



/**
  * Created by 80374643 on 2017/5/10.
  */
object LV46_SUB_LI08_A_CMB03_A3DTA_A6TXCDTAP_SRC {
  def main(args: Array[String]) {
    // 跟对象名、项目名保持一致
    val appName = "LV46_SUB_LI08_A_CMB03_A3DTA_A6TXCDTAP_SRC"

    val sc = ReadMySQLAppConf.getSparkContext(appName)
    val appConf = ReadMySQLAppConf.getAppConf(sc,appName)
    val appSMAP = ReadMySQLAppConf.getAppSMAP(sc,appName)
    val bbkTopic = ReadMySQLAppConf.getBbkTopic(sc,appName)

    val brokers = appConf.get("kafka_input_brokers")
    val topic = appConf.get("kafka_input_topics")
    val checkpointDir = appConf.get("app_hdfs_dir") + "/checkpoint"
    val kafkaParams = Map[String, String]("metadata.broker.list" -> brokers)

    val ssc = new StreamingContext(sc, Seconds(appConf.get("streaming_batch_duration").toLong))
    ssc.checkpoint(checkpointDir)

    // 定期地从kafka的topic+partition中查询最新的偏移量，再根据偏移量范围在每个batch里面处理数据
    val messages = StreamingUtils.createDirectStream(ssc, kafkaParams, topic.split(",").toSet, appConf)
    val lines = messages.map(_._2)

    lines.foreachRDD { rdd =>
      rdd.foreachPartition { partition =>
        val producer = KafkaFactory.getOrCreateProducer(brokers)
        partition.foreach { message =>
          // 遍历appSMAP查询拆分条件获取目标基层分行号集合
          val srcColumnArray = appSMAP.keySet().toArray
          val dstBBKSet = mutable.Set.empty[String]
          for(i <- 0 to srcColumnArray.size-1){
            val srcTable = srcColumnArray(i).toString
            val smapTable = appSMAP.get(srcTable).toString
            dstBBKSet += getDstBBK(message,srcTable,smapTable)
          }

          if(dstBBKSet.size > 0){
            dstBBKSet.foreach(dstBBK => writeResultTopic(message,dstBBK,bbkTopic,producer))
          }else{
            // 写入引擎数据库
          }


          // 引擎数据库里取未匹配数据操作
          queryUnmatched(appSMAP,bbkTopic,producer)
        }
      }
    }
    // 开始计算
    ssc.start()
    ssc.awaitTermination()
  }

  // 根据分行号查询SMAP表目标基层分行号，获取到并查询是否是待拆分分行，是的话写入分行结果topic；未获取到写入引擎数据库
  def getDstBBK(message : String, srcTable : String, smapTable : String): String ={
    val jsonStr = JSON.parseObject(message)
    val srcTableName = srcTable.toString.split(":")(0)
    val srcTableColumn = srcTable.toString.split(":")(1)
    val smapTableName = srcTableName.toString.split(":")(0)
    val smapTableColumn = srcTableColumn.toString.split(":")(1)
    // 取源表待匹配字段
    val key = jsonStr.getJSONObject(srcTableName).get(srcTableColumn).toString

    val jedisCluster = RedisOperator.getRedisConn()
    println("取基层分行号："+smapTableName+":"+smapTableColumn+":"+key)
    val value = jedisCluster.get(smapTableName+":"+smapTableColumn+":"+key)
    println("取到基层分行号："+value)

    value
  }

  // 引擎数据库里取未匹配数据操作
  def queryUnmatched(appSMAP : HashMap[String, String],bbkTopic : HashMap[String, String],producer : KafkaClient): Unit ={
    val jedisCluster = RedisOperator.getRedisConn()
    val unmatched = RedisOperator.keys(jedisCluster,"unmatched:*").toArray
    for(i <- 0 to unmatched.size-1){
      val key = jedisCluster.get(unmatched(i).toString)
      val value = jedisCluster.get(appSMAP.get("table_name")+":"+appSMAP.get("table_column")+":"+key)
      if (value != null){
        println("准备删除Key")
        jedisCluster.del(unmatched(i).toString)
        // 写入分行结果topic
        if(bbkTopic.get(value) != null){
          println("写入"+ bbkTopic.get(value) + ": " + unmatched(i).toString )
          //producer.send(bbkTopic.get(value), null, value+','+unmatched(i).toString)
        }
      }else{
        println("再次写入Redis")
        jedisCluster.set(unmatched(i).toString,key)
      }
    }
  }

  // 写入结果Topic
  def writeResultTopic(message : String, dstBBK : String, bbkTopic : HashMap[String, String], producer : KafkaClient): Unit ={
    if(bbkTopic.get(dstBBK) != null){
      val resultTopic = bbkTopic.get(dstBBK)
      println("写入" + resultTopic + ": " + message )
      //producer.send(bbkTopic.get(dstBBK), null, dstBBK+','+message)
    }
  }

}
