package com.example.activemq.broker;

import org.apache.activemq.broker.BrokerFactory;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.store.kahadb.KahaDBStore;
import org.apache.log4j.net.JMSAppender;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.jms.JMSException;
import java.io.File;

public class InnerBroker {

    public BrokerService createEmbeddedBroker() throws Exception{
        BrokerService brokerService = new BrokerService();

        File dataFileDir = new File("kahadb");

        KahaDBStore kahaDBStore = new KahaDBStore();
        kahaDBStore.setDirectory(dataFileDir);
        //使用更大的日志文件。
        kahaDBStore.setJournalMaxFileLength(1024*100);
        //小批量意味着更频繁和更小的写入。
        kahaDBStore.setIndexWriteBatchSize(100);
        //索引是否在一个单独的线程中写入?
        kahaDBStore.setEnableIndexWriteAsync(Boolean.TRUE);
        //设置broker持久化适配器
        brokerService.setPersistenceAdapter(kahaDBStore);
        brokerService.addConnector("tcp://localhost:61616");
        brokerService.start();
        return brokerService;
    }


    public static void startBrokerWay1() throws Exception{
        //启动broker就像启动一个sorket
        BrokerService brokerService = new BrokerService();
        //关闭持久化，以内存的方式存储数据
        brokerService.setPersistent(Boolean.FALSE);
        //启动多个broker需要为其设置一个名字
        brokerService.setBrokerName("brokerService1");
        brokerService.setUseJmx(Boolean.TRUE);
        //设置客户端的连接方式
        brokerService.addConnector("tcp://localhost:61616");
        brokerService.start();
    }

    public static void startBrokerWay2() throws Exception{
        String brokerURI = "broker:tcp://localhost:61616";
        BrokerService brokerService = BrokerFactory.createBroker(brokerURI,Boolean.TRUE);
        brokerService.setBrokerName("brokerService1");
    }

    private static void springStartBrokerWay(){
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        BrokerService brokerService = context.getBean(BrokerService.class);


    }

    public static void main(String[] args) throws Exception{
//        InnerBroker.startBrokerWay2();
        InnerBroker.springStartBrokerWay();
    }
}
