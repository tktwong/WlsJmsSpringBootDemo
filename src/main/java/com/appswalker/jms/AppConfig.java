package com.appswalker.jms;

import java.util.Properties;
import javax.jms.ConnectionFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.support.destination.DestinationResolver;
import org.springframework.jms.support.destination.JndiDestinationResolver;
import org.springframework.jndi.JndiObjectFactoryBean;
import org.springframework.jndi.JndiTemplate;

@Configuration
public class AppConfig {
    
    // Url to access to the queue or topic
    @Value("${jms.providerUrl}")
    private String providerUrl;
    
    // Number of consumers in the application
    @Value("${jms.connectionFactoryJndiName}")
    private String connectionFactoryJndiName;
    
    // Name of the queue or topic to extract the message
    @Value("${jms.destinationJndiName}")
    private String destinationJndiName;
    
    // Number of consumers in the application
    @Value("${jms.concurrentConsumers}")
    private String concurrentConsumers;
    
    
    @Autowired
    @Bean
    public DefaultJmsListenerContainerFactory dpmsServiceQcf(ConnectionFactory connectionFactory, DestinationResolver destination) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setDestinationResolver(destination);
        factory.setConcurrency(concurrentConsumers);
        return factory;
    }

    @Bean
    public JndiTemplate provider() {
        Properties env = new Properties();
        env.put("java.naming.factory.initial", "weblogic.jndi.WLInitialContextFactory");
        env.put("java.naming.provider.url", providerUrl);
        return new JndiTemplate(env);
    }
    
    @Autowired
    @Bean
    public JndiObjectFactoryBean connectionFactory(JndiTemplate provider){  
        JndiObjectFactoryBean factory = new JndiObjectFactoryBean();
        factory.setJndiTemplate(provider);
        factory.setJndiName(connectionFactoryJndiName);
        factory.setProxyInterface(ConnectionFactory.class);
        return factory;
    }
    
    @Autowired
    @Bean
    public JndiDestinationResolver jmsDestinationResolver(JndiTemplate provider){
        JndiDestinationResolver destResolver = new JndiDestinationResolver();
        destResolver.setJndiTemplate(provider);
        return destResolver;
    }
    
    @Autowired
    @Bean
    public JndiObjectFactoryBean destination(JndiTemplate provider) {
        JndiObjectFactoryBean dest = new JndiObjectFactoryBean();
        dest.setJndiTemplate(provider);
        dest.setJndiName(destinationJndiName);
        return dest;
    }
}
