package com.appswalker.jms;

import java.util.Properties;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.naming.Context;

import org.springframework.beans.factory.annotation.Autowired;
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
    private String url ="t3://localhost:7001";

    @Autowired
    private MessageConverter jacksonJmsMessageConverter;

    @Autowired
    private DpmsMessageListener dpmsMessageListener;

    @Bean
    public JmsTemplate dpmsTopicTemplate() {
        JmsTemplate jmsTemplate = new JmsTemplate();
        jmsTemplate.setConnectionFactory((ConnectionFactory) topicConnFactory().getObject());
        jmsTemplate.setDefaultDestination((Destination) topicDestination().getObject());
        jmsTemplate.setPubSubDomain(true);
        jmsTemplate.setExplicitQosEnabled(true);
        jmsTemplate.setMessageConverter(jacksonJmsMessageConverter);
        return jmsTemplate;
    }

    private JndiTemplate wlsProvider(){
        Properties env = new Properties();
        env.setProperty(Context.INITIAL_CONTEXT_FACTORY,"weblogic.jndi.WLInitialContextFactory");
        env.setProperty(Context.PROVIDER_URL, url);
        return new JndiTemplate(env);
    }

    @Bean
    public JndiObjectFactoryBean topicConnFactory() {
        JndiObjectFactoryBean jndiObjectFactoryBean = new JndiObjectFactoryBean();
        jndiObjectFactoryBean.setJndiName(connectionFactory);
        jndiObjectFactoryBean.setJndiTemplate(wlsProvider());
        return jndiObjectFactoryBean;
    }

    @Bean
    public JndiObjectFactoryBean topicDestination(){
        JndiObjectFactoryBean jmsDestination =new JndiObjectFactoryBean();
        jmsDestination.setJndiName(topic);
        jmsDestination.setJndiTemplate(wlsProvider());
        return jmsDestination;
    }

    @Bean(name="listenerTopic")
    @ConditionalOnMissingBean
    public DefaultMessageListenerContainer listenerTopic(){
        DefaultMessageListenerContainer listener = new DefaultMessageListenerContainer();
        listener.setConnectionFactory((ConnectionFactory) topicConnFactory().getObject());
        listener.setDestination((Destination) topicDestination().getObject());
        listener.setAutoStartup(true);
        listener.setPubSubDomain(true);
        listener.setMessageListener(dpmsMessageListener);
        listener.setMessageConverter(jacksonJmsMessageConverter);
        return listener;
    }
}



