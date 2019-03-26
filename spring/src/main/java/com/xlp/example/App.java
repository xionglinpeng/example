package com.xlp.example;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ){
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:application.xml");
        System.out.println("context startup successfully.");
        MessageService messageService = context.getBean(MessageService.class);
        System.out.println("message : "+messageService.getMessage());
    }
}
