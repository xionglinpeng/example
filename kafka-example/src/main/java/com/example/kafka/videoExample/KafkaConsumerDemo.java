package com.example.kafka.videoExample;

import com.example.kafka.KafkaConfig;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Collections;
import java.util.Properties;

public class KafkaConsumerDemo implements Runnable{

    private KafkaConsumer<Integer,String> kafkaConsumer;

    public KafkaConsumerDemo(String topic) {
        Properties config = new Properties();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConfig.BROKER_LIST);
        config.put(ConsumerConfig.GROUP_ID_CONFIG,"KafkaConsumerDemo");
        config.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,"true");
        config.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG,"1000");
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class.getName());
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"earliest");

        kafkaConsumer = new KafkaConsumer<>(config);
        kafkaConsumer.subscribe(Collections.singletonList(topic));
    }


    @Override
    public void run() {
        ConsumerRecords<Integer,String> consumerRecords = kafkaConsumer.poll(1000);
        consumerRecords.forEach(consumerRecord->{
            System.out.println("message receive : "+consumerRecord);
        });
    }

    public static void main(String[] args) {
        KafkaConsumerDemo kafkaConsumerDemo = new KafkaConsumerDemo("test");
        Thread thread = new Thread(kafkaConsumerDemo);
        thread.start();
    }
}
