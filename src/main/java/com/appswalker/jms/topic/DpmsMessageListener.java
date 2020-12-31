package com.appswalker.jms.topic;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import com.appswalker.jms.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.stereotype.Component;

@Component
public class DpmsMessageListener implements MessageListener{

    @Autowired
    private MessageConverter jacksonJmsMessageConverter;

    @Override
    public void onMessage(Message message) {
        if (message instanceof TextMessage) {
            try {
                String msg = ((TextMessage)message).getText();
                Object payload = jacksonJmsMessageConverter.fromMessage(message);
                if (payload instanceof User) {
                    System.out.println(String.format("Consume message from topic (User): %s", (User)payload));
                } else {
                    System.out.println(String.format("Consume message from topic (text): %s", msg));
                }
            } catch (JMSException e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new IllegalArgumentException(
                    "Message must be of type TestMessage");
        }
    }
}



