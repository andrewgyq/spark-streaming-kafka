package com.cmb.sparkstreaming.util

import org.apache.log4j.Logger

/**
  * Created by 80374643 on 2017/5/10.
  */
object Log {
  def debug(clazz: Class[_], msg : String): Unit = {
    Logger.getLogger(clazz).debug(msg)
  }
  def debug(clazz: Class[_], msg : String, e : Throwable): Unit = {
    Logger.getLogger(clazz).debug(msg, e)
  }
  def info(clazz: Class[_], msg : String): Unit = {
    Logger.getLogger(clazz).info(msg)
  }
  def info(clazz: Class[_], msg : String, e : Throwable): Unit = {
    Logger.getLogger(clazz).info(msg, e)
  }
  def warn(clazz: Class[_], msg : String): Unit = {
    Logger.getLogger(clazz).warn(msg)
  }
  def warn(clazz: Class[_], msg : String, e : Throwable): Unit = {
    Logger.getLogger(clazz).warn(msg, e)
  }
  def error(clazz: Class[_], msg : String): Unit = {
    Logger.getLogger(clazz).error(msg)
  }
  def error(clazz: Class[_], msg : String, e : Throwable): Unit = {
    Logger.getLogger(clazz).error(msg, e)
  }
  def fatal(clazz: Class[_], msg : String): Unit = {
    Logger.getLogger(clazz).fatal(msg)
  }
  def fatal(clazz: Class[_], msg : String, e : Throwable): Unit = {
    Logger.getLogger(clazz).fatal(msg, e)
  }
}
