package com.cmb.sparkstreaming.test
import java.util.HashMap

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord}

/**
  * Created by 80374643 on 2017/5/10.
  */
object LI08_A_CMB03_A3DTA_A6TXCDTAP_SRC {
  def main(args: Array[String]) {

    // Zookeeper连接属性配置
    val props = new HashMap[String, Object]()
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "99.12.156.134:9092")
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
      "org.apache.kafka.common.serialization.StringSerializer")
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
      "org.apache.kafka.common.serialization.StringSerializer")
    //创建KafkaProducer
    val producer = new KafkaProducer[String, String](props)

    // 向kafka集群发送消息
    while(true) {
      val bbk = Array("110","132","575","752","999")
      (0 to 4).foreach { index =>
        val line = "{\"JRNINF\":{\"XJRNRCV\":\"MXJRNR0001\",\"XJRNSEQ\":\"12345\",\"XJRNCMT\":\"67890\",\"XJRNRRN\":\"100\",\"XJRNTIM\":\"20550828124704\",\"XJRNTYP\":\"PT\",\"XSYSNAM\":\"CMBDEV1\",\"XSYSLGC\":\"CMB03\",\"XJOBNAM\":\"B3RMTSRVCR\",\"XUSRNAM\":\"PGCTX\",\"XJOBNBR\":\"123456\",\"XPGMNAM\":\"CTXKAFKARP\",\"XFILNAM\":\"A6TXCDTAP\",\"XFILLIB\":\"A3DTA\"},\"A6TXCDTAP\":{\"CTSACTNBR\":\"127900008400001\",\"CTSEACNBR\":\"127900008410201\",\"CTSCCYNBR\":\"10\",\"CTSCCYTYP\":\"0\",\"CTSLGRTYP\":\"20102000\",\"CTSBBKNBR\":\""+bbk(index)+"\",\"CTSBRNLGR\":\"127502\",\"CTSBRNGLG\":\"127\",\"CTSTRSTYP\":\"F\",\"CTSTRSSEQ\":\"127440000000043\",\"CTSSETSEQ\":\"127440000000042\",\"CTSSETNUM\":\"1\",\"CTSTRDCOD\":\"\",\"CTSTRDNUM\":\"0\",\"CTSTRSDAT\":\"20131014\",\"CTSREGTIM\":\"2014-01-02-15.42.51.517000\",\"CTSTRSNUM\":\"6\",\"CTSRVSTAG\":\"\",\"CTSRVSSET\":\"\",\"CTSTRSDIR\":\"D\",\"CTSTRSAMT\":\"-10.00\",\"CTSONLBAL\":\"1361.00\",\"CTSTXTCOD\":\"GALO\",\"CTSTXTC2G\":\"GALO\",\"CTSTXTBNK\":\"\",\"CTSTXTCLT\":\"\",\"CTSPSBCOD\":\"\",\"CTSPSBNBR\":\"\",\"CTSVALDAT\":\"20131014\",\"CTSAUBCOD\":\"\",\"CTSAUBAMT\":\".00\",\"CTSQRYCLT\":\"\",\"CTSLMTACT\":\"\",\"CTSLMTAMT\":\".00\",\"CTSCNLCOD\":\"DSK\",\"CTSCNLSEQ\":\"\",\"CTSENTTYP\":\"\",\"CTSEXTCCY\":\"\",\"CTSEXTAMT\":\".00\",\"CTSCHKCCY\":\"\",\"CTSCHKAMT\":\".00\",\"CTSASTCCY\":\"\",\"CTSASTAMT\":\".00\",\"CTSTXTCCY\":\"\",\"CTSTXTAMT\":\".00\",\"CTSSTACCY\":\"\",\"CTSSTAAMT\":\".00\",\"CTSSTATXT\":\"\",\"CTSANLCCY\":\"\",\"CTSANLAMT\":\".00\",\"CTSANLTXT\":\"\",\"CTSATCCCY\":\"\",\"CTSATCAMT\":\".00\",\"CTSATCTXT\":\"\",\"CTSBUSREF\":\"###1274400000216\",\"CTSMASPRD\":\"\",\"CTSSPL030\":\"\",\"CTSTRSSTS\":\"A\",\"CTSRCDDAT\":\"20131014\",\"CTSRCDVER\":\"1\"}}"
        val message = new ProducerRecord[String, String]("dev_test5", null, line)
        producer.send(message)
      }
      Thread.sleep(1000)

    }
  }
}
