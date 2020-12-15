package com.appswalker.jms;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class JMSListener {
    @JmsListener(containerFactory = "dpmsServiceQcf", destination = "${jms.destinationJndiName}")
    public void receiveMessage(String msg) {
        System.out.println(String.format("emmit to frontend: %s", msg));
    }
}