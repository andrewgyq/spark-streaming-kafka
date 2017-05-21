import com.cmb.sparkstreaming.config.ReadMySQLAppConf

/**
  * Created by 80374643 on 2017/5/18.
  */
object MySQLTest {
  def main(args: Array[String]): Unit = {
    val appName = "LV46_SUB_LU29_A_CMB10_C3DTA_PYTRXDTAP_SRC"
    val sc = ReadMySQLAppConf.getSparkContext(appName)
    println(ReadMySQLAppConf.getAppSMAP(sc, appName))
  }
}
