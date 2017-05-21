package com.cmb.sparkstreaming.subtopic

import java.util.HashMap

import com.alibaba.fastjson.JSON
import com.cmb.sparkstreaming.config.{ReadMySQLAppConf, RedisOperator}
import com.cmb.sparkstreaming.util.StreamingUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}

import scala.collection.mutable



/**
  * Created by 80374643 on 2017/5/10.
  */
object LV46_SUB_LI08_A_CMB03_A3DTA_A6TXCDTAP_SRC {
  // 跟对象名、项目名保持一致
  private val appName = "LV46_SUB_LI08_A_CMB03_A3DTA_A6TXCDTAP_SRC"

  def main(args: Array[String]) {

    val sc = ReadMySQLAppConf.getSparkContext(appName)
    val appConf = ReadMySQLAppConf.getAppConf(sc,appName)
    val appSMAP = ReadMySQLAppConf.getAppSMAP(sc,appName)
    val resultTopic = ReadMySQLAppConf.getResultTopic(sc,appName)

    val topic = appConf.get("kafka_input_topics")
    val kafkaParams = Map[String, String]("metadata.broker.list" -> appConf.get("kafka_input_brokers"))

    val ssc = new StreamingContext(sc, Seconds(appConf.get("streaming_batch_duration").toLong))
    ssc.checkpoint(appConf.get("app_hdfs_dir") + "/checkpoint")

    // 定期地从kafka的topic+partition中查询最新的偏移量，再根据偏移量范围在每个batch里面处理数据
    val messages = StreamingUtils.createDirectStream(ssc, kafkaParams, topic.split(",").toSet, appConf)
    val lines = messages.map(_._2)

    lines.foreachRDD { rdd =>
      rdd.foreachPartition { partition =>
        partition.foreach { message =>
          // 遍历appSMAP查询拆分条件获取目标基层分行号集合
          val srcColumnArray = appSMAP.keySet().toArray
          val dstBBKSet = mutable.Set.empty[String]
          for(i <- 0 to srcColumnArray.size - 1){
            val key = srcColumnArray(i)
            val value = appSMAP.get(key)
            dstBBKSet += getDstBBK(message,key.toString,value)
          }

          if(dstBBKSet.size > 0){
            dstBBKSet.foreach(dstBBK => writeResultTopic(message,dstBBK,resultTopic))
          }else{
            // 写入引擎数据库
            RedisOperator.set(RedisOperator.getRedisConn(), appName + ":" + message, null)
          }

          // 引擎数据库里取未匹配数据操作
          queryUnmatched(appSMAP,resultTopic)
        }
      }
    }
    // 开始计算
    ssc.start()
    ssc.awaitTermination()
  }

  // 根据分行号查询SMAP表目标基层分行号，获取到并查询是否是待拆分分行，是的话写入分行结果topic；未获取到写入引擎数据库
  def getDstBBK(message : String, key : String, value : String): String ={
    val jsonStr = JSON.parseObject(message)
    val srcTable = key.split(":")(0)
    val srcColumn = key.split(":")(1)
    val smapTable = value.split(":")(0)
    val smapColumn = value.split(":")(1)
    // 取源表待匹配字段
    val column = jsonStr.getJSONObject(srcTable).get(srcColumn)
    val dstBBK = RedisOperator.getByKey(RedisOperator.getRedisConn(), smapTable+":"+smapColumn+":"+column)

    dstBBK
  }

  // 引擎数据库里取未匹配数据操作
  def queryUnmatched(appSMAP : HashMap[String, String],resultTopic : HashMap[String, String]): Unit ={
    val unmatchedArray = RedisOperator.keys(RedisOperator.getRedisConn(), appName + ":*").toArray
    for(i <- 0 to unmatchedArray.size-1){
      val message = unmatchedArray(i).toString.split(":")(1)
      val srcColumnArray = appSMAP.keySet().toArray
      val dstBBKSet = mutable.Set.empty[String]
      for(i <- 0 to srcColumnArray.size-1){
        val key = srcColumnArray(i)
        val value = appSMAP.get(key)
        dstBBKSet += getDstBBK(message,key.toString,value)
      }

      if(dstBBKSet.size > 0){
        dstBBKSet.foreach(dstBBK => writeResultTopic(message,dstBBK,resultTopic))
        // 删除Redis该条记录
        RedisOperator.deleteByKey(RedisOperator.getRedisConn(),unmatchedArray(i).toString)
      }else{
        // 写入引擎数据库
        RedisOperator.set(RedisOperator.getRedisConn(), appName + ":" + message, null)
      }
    }
  }

  // 写入结果Topic
  def writeResultTopic(message : String, dstBBK : String, resultTopic : HashMap[String, String]): Unit ={
    if(resultTopic.get(dstBBK) != null){
      val resultTopic = resultTopic.get(dstBBK)
      //producer.send(resultTopic.get(dstBBK), null, dstBBK+','+message)
    }
  }

}
