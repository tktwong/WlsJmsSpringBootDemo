package com.appswalker.jms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(value = "/")
class JmsController {
    @Autowired
    private JmsTemplate dpmsTopicTemplate;

    @Autowired
    private JmsTemplate dpmsQueTemplate;

    @RequestMapping(value ="/sendMsg", method = RequestMethod.POST)
    @ResponseBody
    public String sendMessage(HttpServletRequest req, HttpServletResponse resp){
        dpmsTopicTemplate.convertAndSend("hello world from topic at " +System.currentTimeMillis());
        dpmsQueTemplate.convertAndSend("hello world from queue at " +System.currentTimeMillis());
        return "OK";
    }
}
