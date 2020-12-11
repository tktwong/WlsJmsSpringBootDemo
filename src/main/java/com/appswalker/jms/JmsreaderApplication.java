package com.appswalker.jms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration;
import org.springframework.jms.annotation.EnableJms;

@SpringBootApplication(exclude = JmxAutoConfiguration.class)
@EnableJms
public class JmsreaderApplication {
	public static void main(String[] args) {
		SpringApplication.run(JmsreaderApplication.class, args);
	}
}
