package com.xlp.example;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ){
//        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:application.xml");
//        System.out.println("context startup successfully.");
//        MessageService messageService = context.getBean(MessageService.class);
//

//        XmlBeanFactory beanFactory = new XmlBeanFactory(new ClassPathResource("application.xml"));

        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
        reader.loadBeanDefinitions(new ClassPathResource("application.xml"));

        MessageService messageService = beanFactory.getBean("messageService",MessageService.class);
        System.out.println("message : "+messageService.getMessage());
    }
}
