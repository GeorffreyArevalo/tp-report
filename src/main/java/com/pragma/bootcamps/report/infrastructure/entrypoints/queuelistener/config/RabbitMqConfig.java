package com.pragma.bootcamps.report.infrastructure.entrypoints.queuelistener.config;

import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.support.converter.MessageConverter;

@Configuration
public class RabbitMqConfig {

     @Bean
     public MessageConverter jsonMessageConverter() {
         return new JacksonJsonMessageConverter();
    }

}
