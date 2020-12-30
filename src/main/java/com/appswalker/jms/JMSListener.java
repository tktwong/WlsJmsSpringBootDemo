package com.appswalker.jms;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class JMSListener {

    @JmsListener(containerFactory = "dpmsServiceQcf", destination = "jms/DpmsServiceQue", selector = "classType = 'user'")
    public void receiveUser(User user) {
        System.out.println(String.format("Consume message from queue (User): %s", user));
    }

    @JmsListener(containerFactory = "dpmsServiceQcf", destination = "jms/DpmsServiceQue")
    public void receiveAnyMessage(Object msg) {
        System.out.println(String.format("Consume message from queue (Any Message): %s", msg.toString()));
    }

    @JmsListener(containerFactory = "dpmsServiceQcf", destination = "jms/DpmsServiceQue", selector = "classType = 'text'")
    public void receiveTextMessage(String text) {
        System.out.println(String.format("Consume message from queue (Text): %s", text));
    }
}