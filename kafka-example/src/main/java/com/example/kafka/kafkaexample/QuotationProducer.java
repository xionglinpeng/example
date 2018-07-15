package com.example.kafka.kafkaexample;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;

import java.text.DecimalFormat;
import java.util.Objects;
import java.util.Properties;
import java.util.Random;

public class QuotationProducer {

    private static final int MSG_SIZE = 100;

    private static final String TOPIC = "stock-quotation";

    private static final String BROKER_LIST = "192.168.56.2:9092,192.168.56.4:9092,192.168.56.5:9092";

    private static KafkaProducer<String,String> producer = null;

    static {
        Properties config = initConfig();
        //初始化一个KafkaProducer
        producer = new KafkaProducer<>(config);
    }

    private static Properties initConfig() {
        Properties properties = new Properties();
        //kafka broker列表
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,BROKER_LIST);
        //设置序列化类
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,StringSerializer.class.getName());
        return properties;
    }

    /**
     * 生成股票行情信息
     * @return
     */
    public static StockQuotationInfo createQuotationInfo() {
        StockQuotationInfo stockQuotationInfo = new StockQuotationInfo();
        //随机产生1到10之间的帐数，然后与600100相加组成股票代码
        Random r = new Random();
        Integer stockCode = 600100+r.nextInt(10);
        //随机产生一个0-1之间的浮点数
        float random = (float) Math.random();
        //设置涨跌规则
        if (random / 2 < 0.5) {
            random = -random;
        }

        DecimalFormat decimalFormat = new DecimalFormat(".00");//设备保存两位有效数字
        stockQuotationInfo.setCurrentPrice(Float.valueOf(decimalFormat.format(11+random)));//设置最新价在11元浮动
        stockQuotationInfo.setPreClosePrice(11.80f);//设置昨日收盘价为固定值
        stockQuotationInfo.setOpenPrice(11.5f);//设置开盘价
        stockQuotationInfo.setLowPrice(10.5f);//设置最低价，并不考虑10%限制；以及当前价是否已是最低价
        stockQuotationInfo.setHighPrice(12.5f);//设置最高价，并不考虑10%限制；以及当前价是否已是最高价
        stockQuotationInfo.setStockCode(stockCode.toString());
        stockQuotationInfo.setTradeTime(System.currentTimeMillis());
        stockQuotationInfo.setStockName("股票-"+stockCode);
        return stockQuotationInfo;
    }

    public static void main(String[] args) {
        ProducerRecord<String,String> record = null;
        StockQuotationInfo quotationInfo = null;
        try {
            int num = 0;
            for (int i = 0; i < MSG_SIZE; i++) {
                quotationInfo = createQuotationInfo();
                //String topic, Integer partition, Long timestamp, K key, V value
                record = new ProducerRecord<>(TOPIC,null,quotationInfo.getTradeTime(),quotationInfo.getStockCode(),quotationInfo.toString());
                //异步发送消息
                producer.send(record, new Callback() {
                    @Override
                    public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                        if (Objects.nonNull(e)) {//发送异常记录异常信息
                            System.out.printf("Send message occurs exception : %s\n",e);
                        }
                        if (Objects.nonNull(recordMetadata)) {
                            System.out.printf("offset : %s, partition: %s\n",recordMetadata.offset(),recordMetadata.partition());
                        }
                    }
                });
                if (num++ % 10 ==0) {
                    Thread.sleep(2000L);//休眠2s
                }
            }
        } catch (InterruptedException e) {
            System.out.printf("Send message occurs execption : %s",e);
        } finally {
            producer.close();
        }


    }
}
