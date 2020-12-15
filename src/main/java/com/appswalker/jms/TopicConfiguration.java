package com.appswalker.jms;

import java.util.Properties;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.jndi.JndiObjectFactoryBean;
import org.springframework.jndi.JndiTemplate;

@Configuration
public class TopicConfiguration {

    private String connectionFactory ="jms/DpmsServiceTcf";
    private String topic ="jms/DpmsServiceTopic";
    private String url ="t3://localhost:7001";

    @Autowired
    private DpmsMessageListener dpmsMessageListener;

    @Bean(name ="weblogicJms")
    public JndiTemplate weblogicJms(){
        Properties props = new Properties();
        props.setProperty("java.naming.factory.initial","weblogic.jndi.WLInitialContextFactory");
        props.setProperty("java.naming.provider.url", url);
        JndiTemplate jmsJndiTemplate = new JndiTemplate();
        jmsJndiTemplate.setEnvironment(props);
        return jmsJndiTemplate;
    }

    @Bean(name ="jmsConnectionFactory")
    public JndiObjectFactoryBean jmsConnectionFactory(){
        JndiObjectFactoryBean jndiObjectFactoryBean = new JndiObjectFactoryBean();
        jndiObjectFactoryBean.setJndiName(connectionFactory);
        jndiObjectFactoryBean.setJndiTemplate(weblogicJms());
        return jndiObjectFactoryBean;
    }

    @Bean(name ="jmsDestination")
    public JndiObjectFactoryBean jmsDestination(){
        JndiObjectFactoryBean jmsDestination =new JndiObjectFactoryBean();
        jmsDestination.setJndiName(topic);
        jmsDestination.setJndiTemplate(weblogicJms());
        return jmsDestination;
    }

    @Bean(name="listenerTopic")
    @ConditionalOnMissingBean
    public DefaultMessageListenerContainer listenerTopic(){
        DefaultMessageListenerContainer listener = new DefaultMessageListenerContainer();
        listener.setConnectionFactory((ConnectionFactory) jmsConnectionFactory().getObject());
        listener.setDestination((Destination) jmsDestination().getObject());
        listener.setAutoStartup(true);
        listener.setPubSubDomain(true);
        listener.setMessageListener(dpmsMessageListener);
        return listener;
    }

    @Bean
    public JmsTemplate dpmsTopicTemplate() {
        JmsTemplate jmsTemplate = new JmsTemplate();
        jmsTemplate.setConnectionFactory((ConnectionFactory) jmsConnectionFactory().getObject());
        jmsTemplate.setDefaultDestination((Destination) jmsDestination().getObject());
        jmsTemplate.setPubSubDomain(true);
        jmsTemplate.setExplicitQosEnabled(true);
        return jmsTemplate;
    }

}



