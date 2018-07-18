package com.example.kafka.videoExample;

import com.example.kafka.KafkaConfig;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.HashMap;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class KafkaProducerDemo implements Runnable{

    private KafkaProducer<Integer,String> kafkaProducer;

    private String topic;


    public KafkaProducerDemo(String topic) {

        Properties config = new Properties();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConfig.BROKER_LIST);
        config.put(ProducerConfig.CLIENT_ID_CONFIG,"KafkaProducerDemo");
        config.put(ProducerConfig.ACKS_CONFIG,"-1");
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class.getName());
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        kafkaProducer = new KafkaProducer<>(config);

        this.topic = topic;
    }

    @Override
    public void run() {
        int num = 0;
        while (num < 50) {
            String message = "message-"+num;
            System.out.println("begin send message : "+message);
            kafkaProducer.send(new ProducerRecord<>(topic,message));
            num++;
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    static final int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    public static void main(String[] args) {
//        KafkaProducerDemo kafkaProducerDemo = new KafkaProducerDemo("test");
//        Thread thread = new Thread(kafkaProducerDemo);
//        thread.start();
        System.out.println((16-1) & hash("45645645dghfgh"));

    }
}
