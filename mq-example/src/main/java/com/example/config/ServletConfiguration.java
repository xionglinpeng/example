package com.example.config;

import org.apache.activemq.web.AjaxServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jms.activemq.ActiveMQProperties;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

@Configuration
public class ServletConfiguration implements ServletContextInitializer{

    @Autowired
    private ActiveMQProperties activeMQProperties;

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {

        servletContext.setInitParameter("org.apache.activemq.brokerURL",activeMQProperties.getBrokerUrl());

    }

    @Bean
    public ServletRegistrationBean<AjaxServlet> activemqAjaxServlet(){
        ServletRegistrationBean<AjaxServlet> servletRegistrationBean = new ServletRegistrationBean<>();
        servletRegistrationBean.setServlet(new AjaxServlet());
        servletRegistrationBean.setName("AjaxServlet");
        servletRegistrationBean.addUrlMappings("/amq/*");
        return servletRegistrationBean;
    }

}
