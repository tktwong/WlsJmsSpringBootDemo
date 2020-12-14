package com.appswalker.jms;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.stereotype.Component;

@Component
public class DpmsMessageListener implements MessageListener{

    @Override
    public void onMessage(Message message) {
        if (message instanceof TextMessage) {
            try {
                String msg = ((TextMessage)message).getText();
                System.out.println("ok--"+msg);
                //JSONObject.parseObject("",clazz);
            } catch (JMSException e) {
                throw new RuntimeException(e);
            }

        } else {
            throw new IllegalArgumentException(
                    "Message must be of type TestMessage");
        }
    }



}



