package com.example.controller;

import com.example.component.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.jms.Destination;
import java.io.UnsupportedEncodingException;
import java.util.Map;

@Validated
@RequestMapping("/activemq")
@RestController
public class ActiveMQController {

    @Autowired
    private JmsTemplate jmsTemplate;
    @Autowired
    @Qualifier("ajaxDestination")
    private Destination destination;


    @GetMapping("/index")
    public ModelAndView index(){
        return new ModelAndView("index");
    }


    @GetMapping(value = "/text")
    public Map<String,Object> sender(@RequestParam String textMessage) throws UnsupportedEncodingException{
//        Map<String,Object> map = new HashMap<>();
//        map.put("name","小明");
//        jmsTemplate.convertAndSend(destination,map);

//        jmsTemplate.send(destination,session -> {
//            MapMessage message = session.createMapMessage();
//            message.setString("name","小明");
//            return message;
//        });
        jmsTemplate.convertAndSend(destination,textMessage);
        return Model.success();
    }
}
