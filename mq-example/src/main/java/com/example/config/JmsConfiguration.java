package com.example.config;

import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.jms.Destination;

@Configuration
public class JmsConfiguration {

    @Bean
    public Destination ajaxDestination(){
        return new ActiveMQTopic("AJAX-TEST");
    }
}
