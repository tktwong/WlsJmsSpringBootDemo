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
        User user = User.builder().gender("Male").name("Parken").password("password").build();
        dpmsTopicTemplate.convertAndSend("hello world from topic at " +System.currentTimeMillis());
        dpmsTopicTemplate.convertAndSend(user);
        dpmsQueTemplate.convertAndSend("hello world from queue at " +System.currentTimeMillis());
        dpmsQueTemplate.convertAndSend("hello world from queue at " +System.currentTimeMillis(), m -> {
            m.setStringProperty("classType", "text");
            return m;
        });
        dpmsQueTemplate.convertAndSend(
                user, m -> {
            m.setStringProperty("classType", "user");
            return m;
        });
        return "OK";
    }
}
