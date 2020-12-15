package com.appswalker.jms;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class JMSListener {
    @JmsListener(containerFactory = "dpmsServiceQcf", destination = "jms/DpmsServiceQue")
    public void receiveMessage(String msg) {
        System.out.println(String.format("Emit to frontend from queue: %s", msg));
    }
}