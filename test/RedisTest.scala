import com.cmb.sparkstreaming.config.RedisOperator

/**
  * Created by 80374643 on 2017/5/18.
  */
object RedisTest {
  def main(args: Array[String]): Unit = {
    val jedisCluster = RedisOperator.getRedisConn()
    val unmatched = RedisOperator.keys(jedisCluster,"SCFG_VBS_BBK_DST_BBK_MAP:*").toArray
    unmatched.foreach(row => println(row))

  }
}
