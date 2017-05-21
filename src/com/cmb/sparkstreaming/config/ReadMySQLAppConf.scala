package com.cmb.sparkstreaming.config

import java.util.HashMap
import java.util.Properties
import java.io.{FileNotFoundException, InputStream}

import org.apache.spark.sql.SQLContext
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by 80374643 on 2017/5/11.
  *
  */
object ReadMySQLAppConf {
  // 从MySQL中获取app_config表数据
  private val appConf = new HashMap[String, String]()
  // 从MySQL中获取app_smap表数据
  private val appSMAP = new HashMap[String, String]()
  // 从MySQL中获取应用分行对应的结果topic名
  private val resultTopic = new HashMap[String, String]()

  private val prop = new Properties()

  def getSparkContext(appName: String) : SparkContext ={

    val conf = new SparkConf().setMaster("local[2]").setAppName(appName)
    val sc = new SparkContext(conf)

    var in: InputStream = null

    try {
      in = this.getClass.getResourceAsStream("mysql.properties")
      prop.load(in)
    }
    catch {
      case e: FileNotFoundException => {
        println("#" * 30)
        println("no mysql.properties file")
        println("#" * 30)
      }
    }
    finally {
      if (in != null) {
        in.close()
      }
    }

    sc
  }

  def getAppConf(sc : SparkContext, appName : String) : HashMap[String, String] = {
    val sqlContext = new SQLContext(sc)
    val app_config = sqlContext.read.jdbc(prop.getProperty("url"), "app_config", Array("app_name = '"+appName+"'"),prop)
    for(i <- 0 to app_config.columns.length - 1){
      appConf.put(app_config.columns(i), app_config.first().getString(i))
    }
    appConf
  }

  def getAppSMAP(sc : SparkContext, appName : String) : HashMap[String, String] = {
    val sqlContext = new SQLContext(sc)
    val app_smap = sqlContext.read.jdbc(prop.getProperty("url"), "app_smap", Array("app_name = '"+appName+"'"),prop)
    val rows = app_smap.collect()
    rows.foreach( row => appSMAP.put(row.getString(1)+":"+row.getString(2), row.getString(3)+":"+row.getString(4)))
    appSMAP
  }


  def getResultTopic(sc : SparkContext, appName : String) : HashMap[String, String] = {
    val sqlContext = new SQLContext(sc)
    val topic_stat = sqlContext.read.jdbc(prop.getProperty("url"), "topic_stat", Array("app_name = '"+appName+"'"),prop)
    val rows = topic_stat.collect()
    rows.foreach( row => resultTopic.put(row.getString(0), row.getString(3)))
    resultTopic
  }
}
