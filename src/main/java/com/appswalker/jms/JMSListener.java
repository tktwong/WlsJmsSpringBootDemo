package com.appswalker.jms;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class JMSListener {
    @JmsListener(containerFactory = "dpmsTestServiceQcf", destination = "${jms.destinationJndiName}")
    public void receiveMessage(String msg) {
        System.out.println(msg);
    }
}