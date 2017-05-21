package com.cmb.sparkstreaming.util

import org.apache.log4j.Logger

import scala.collection.mutable
/**
  * Created by 80374643 on 2017/5/10.
  */
object KafkaFactory {
  private val logger = Logger.getLogger(getClass)
  private val threadLocal = new ThreadLocal[mutable.Map[String, KafkaClient]]

  def getOrCreateProducer(brokers: String): KafkaClient = {
    var producerMap = threadLocal.get()
    if (producerMap == null) {
      producerMap = mutable.Map[String, KafkaClient]()
      threadLocal.set(producerMap)
    }
    producerMap.getOrElseUpdate(brokers, {
      logger.info(s"Create Kafka producer , brokers: $brokers")
      val kafkaClient = new KafkaClient(brokers)
      sys.addShutdownHook {
        logger.info(s"Close Kafka producer, brokers: $brokers")
        kafkaClient.close()
      }
      kafkaClient
    })

  }

}
