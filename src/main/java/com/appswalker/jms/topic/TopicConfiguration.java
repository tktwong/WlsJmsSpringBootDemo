package com.appswalker.jms.topic;

import java.util.Properties;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.naming.Context;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jndi.JndiObjectFactoryBean;
import org.springframework.jndi.JndiTemplate;

@Configuration
public class TopicConfiguration {

    private String connectionFactory ="jms/DpmsServiceTcf";
    private String topic ="jms/DpmsServiceTopic";
    // Url to access to the queue or topic
//    @Value("${jms.providerUrl}")
//    private String url;

    @Autowired
    private MessageConverter jacksonJmsMessageConverter;

    @Autowired
    private DpmsMessageListener dpmsMessageListener;

    @Bean
    public JmsTemplate dpmsTopicTemplate(JndiTemplate wlsProvider) {
        JmsTemplate jmsTemplate = new JmsTemplate();
        jmsTemplate.setConnectionFactory((ConnectionFactory) topicConnFactory(wlsProvider).getObject());
        jmsTemplate.setDefaultDestination((Destination) topicDestination(wlsProvider).getObject());
        jmsTemplate.setPubSubDomain(true);
        jmsTemplate.setExplicitQosEnabled(true);
        jmsTemplate.setMessageConverter(jacksonJmsMessageConverter);
        return jmsTemplate;
    }

    @Bean
    public JndiObjectFactoryBean topicConnFactory(JndiTemplate wlsProvider) {
        JndiObjectFactoryBean jndiObjectFactoryBean = new JndiObjectFactoryBean();
        jndiObjectFactoryBean.setJndiName(connectionFactory);
        jndiObjectFactoryBean.setJndiTemplate(wlsProvider);
        return jndiObjectFactoryBean;
    }

    @Bean
    public JndiObjectFactoryBean topicDestination(JndiTemplate wlsProvider){
        JndiObjectFactoryBean jmsDestination =new JndiObjectFactoryBean();
        jmsDestination.setJndiName(topic);
        jmsDestination.setJndiTemplate(wlsProvider);
        return jmsDestination;
    }

    @Bean(name="listenerTopic")
    @ConditionalOnMissingBean
    public DefaultMessageListenerContainer listenerTopic(JndiTemplate wlsProvider){
        DefaultMessageListenerContainer listener = new DefaultMessageListenerContainer();
        listener.setConnectionFactory((ConnectionFactory) topicConnFactory(wlsProvider).getObject());
        listener.setDestination((Destination) topicDestination(wlsProvider).getObject());
        listener.setAutoStartup(true);
        listener.setPubSubDomain(true);
        listener.setMessageListener(dpmsMessageListener);
        listener.setMessageConverter(jacksonJmsMessageConverter);
        return listener;
    }
}



