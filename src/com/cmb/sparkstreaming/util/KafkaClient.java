package com.cmb.sparkstreaming.util;

import com.unistacks.tamboo.kafkaclient.producer.KafkaProducer;
import com.unistacks.tamboo.kafkaclient.producer.KafkaProducerFactory;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.log4j.Logger;

import java.util.Properties;
/**
 * Created by 80374643 on 2017/5/10.
 */
public class KafkaClient {
    private KafkaProducer producer;
    Logger logger = Logger.getLogger(KafkaClient.class);
    public void send(String topic, String key, String value) {
        if (key != null)
            producer.send(new ProducerRecord<byte[], byte[]>(topic, key.getBytes(), value.getBytes()));
        else
            producer.send(new ProducerRecord<byte[], byte[]>(topic, value.getBytes()));
    }
    public KafkaClient(String brokers) {
        Properties properties = new Properties();
        properties.put("acks", "all");
        producer = KafkaProducerFactory.getProducer(brokers, properties);

    }
    public void close() {
        producer.close();
    }
}
