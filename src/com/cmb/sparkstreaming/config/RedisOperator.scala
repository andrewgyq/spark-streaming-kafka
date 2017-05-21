package com.cmb.sparkstreaming.config

import java.io.{FileNotFoundException, InputStream}
import java.util.TreeSet
import java.util.{HashSet, Properties}

import org.apache.commons.pool2.impl.GenericObjectPoolConfig
import redis.clients.jedis.{HostAndPort, JedisCluster, JedisPool}

/**
  * Created by 80374643 on 2017/5/15.
  */
object RedisOperator {
  private val prop = new Properties()
  private val keys = new TreeSet[String] ()
  // 获取Redis集群连接
  def getRedisConn(): JedisCluster = {

    var in: InputStream = null

    try {
      in = this.getClass.getResourceAsStream("redis.properties")
      prop.load(in)
    }
    catch {
      case e: FileNotFoundException => {
        println("#" * 30)
        println("no redis.properties file")
        println("#" * 30)
      }
    }
    finally {
      if (in != null) {
        in.close()
      }
    }

    val jedisClusterNodes = new HashSet[HostAndPort]()
    val hosts = prop.getProperty("hosts").split("/")
    for(host <- hosts){
      jedisClusterNodes.add(new HostAndPort(host,prop.getProperty("port").toInt))
    }
    val password = prop.getProperty("requirepass")

    val jedisCluster = new JedisCluster(jedisClusterNodes,1000,1000,1,password, new GenericObjectPoolConfig())

    jedisCluster
  }

  // 根据模式获取所有的key值
  def keys(jedisCluster : JedisCluster, pattern : String): TreeSet[String] = {

    val clusterNodes = jedisCluster.getClusterNodes()
    val clusterNodesArray = clusterNodes.keySet().toArray

    for(i <- 0 to clusterNodesArray.size-1){
      val jp = clusterNodes.get(clusterNodesArray(i));
      val connection = jp.getResource();
      try {
        keys.addAll(connection.keys(pattern));
      } catch {
        case e: Exception => {
          println("#" * 30)
          println("keys Exception")
          println("#" * 30)
        }
      } finally{
        //用完一定要close这个链接！！！
        connection.close();
      }

    }

    keys
  }
}
