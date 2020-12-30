package com.appswalker.jms;

import java.util.Properties;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.naming.Context;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.destination.DestinationResolver;
import org.springframework.jms.support.destination.JndiDestinationResolver;
import org.springframework.jndi.JndiObjectFactoryBean;
import org.springframework.jndi.JndiTemplate;

@Configuration
public class QueueConfig {
    
    // Url to access to the queue or topic
    private String providerUrl = "t3://localhost:7001";
    // Number of consumers in the application
    private String connectionFactoryJndiName = "jms/DpmsServiceQcf";
    // Name of the queue or topic to extract the message
    private String destinationJndiName = "jms/DpmsServiceQue";
    @Value("${jms.concurrentConsumers}")
    private String concurrentConsumers;
    
    @Bean
    public DefaultJmsListenerContainerFactory dpmsServiceQcf(ConnectionFactory queueConnFactory,
                                                             DestinationResolver queueDestResolver) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(queueConnFactory);
        factory.setDestinationResolver(queueDestResolver);
        factory.setConcurrency(concurrentConsumers);
        return factory;
    }

    private JndiTemplate wlsProvider() {
        Properties env = new Properties();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
        env.put(Context.PROVIDER_URL, providerUrl);
        return new JndiTemplate(env);
    }
    
    @Bean
    public JndiObjectFactoryBean queueConnFactory(){
        JndiObjectFactoryBean factory = new JndiObjectFactoryBean();
        factory.setJndiTemplate(wlsProvider());
        factory.setJndiName(connectionFactoryJndiName);
        factory.setProxyInterface(ConnectionFactory.class);
        return factory;
    }
    
    @Bean
    public JndiDestinationResolver queueDestResolver(){
        JndiDestinationResolver destResolver = new JndiDestinationResolver();
        destResolver.setJndiTemplate(wlsProvider());
        return destResolver;
    }
    
    @Bean
    public JndiObjectFactoryBean queueDestination() {
        JndiObjectFactoryBean dest = new JndiObjectFactoryBean();
        dest.setJndiTemplate(wlsProvider());
        dest.setJndiName(destinationJndiName);
        return dest;
    }

    @Bean
    public JmsTemplate dpmsQueTemplate() {
        JmsTemplate jmsTemplate = new JmsTemplate();
        jmsTemplate.setConnectionFactory((ConnectionFactory) queueConnFactory().getObject());
        jmsTemplate.setDefaultDestination((Destination) queueDestination().getObject());
        return jmsTemplate;
    }
}
