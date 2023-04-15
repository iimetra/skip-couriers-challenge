package com.iimetra.skip.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${spring.rabbitmq.deliveryQueue}")
    private String deliveryQueue;
    @Value("${spring.rabbitmq.deliveryExchange}")
    private String deliveryExchange;
    @Value("${spring.rabbitmq.deliveryRoutingKey}")
    private String deliveryRoutingKey;

    @Value("${spring.rabbitmq.adjustmentQueue}")
    private String adjustmentQueue;
    @Value("${spring.rabbitmq.adjustmentExchange}")
    private String adjustmentExchange;
    @Value("${spring.rabbitmq.adjustmentRoutingKey}")
    private String adjustmentRoutingKey;

    @Value("${spring.rabbitmq.bonusQueue}")
    private String bonusQueue;
    @Value("${spring.rabbitmq.bonusExchange}")
    private String bonusExchange;
    @Value("${spring.rabbitmq.bonusRoutingKey}")
    private String bonusRoutingKey;

    @Value("${spring.rabbitmq.username}")
    private String username;
    @Value("${spring.rabbitmq.password}")
    private String password;
    @Value("${spring.rabbitmq.host}")
    private String host;
    @Bean
    public Queue deliveryQueue() {
        return new Queue(deliveryQueue, true);
    }
    @Bean
    public Exchange deliveryExchange() {
        return ExchangeBuilder.directExchange(deliveryExchange).durable(true).build();
    }
    @Bean
    public Binding deliveryBinding() {
        return BindingBuilder
            .bind(deliveryQueue())
            .to(deliveryExchange())
            .with(deliveryRoutingKey)
            .noargs();
    }

    @Bean
    public Queue adjustmentQueue() {
        return new Queue(adjustmentQueue, true);
    }
    @Bean
    public Exchange adjustmentExchange() {
        return ExchangeBuilder.directExchange(adjustmentExchange).durable(true).build();
    }
    @Bean
    public Binding adjustmentBinding() {
        return BindingBuilder
            .bind(adjustmentQueue())
            .to(adjustmentExchange())
            .with(adjustmentRoutingKey)
            .noargs();
    }

    @Bean
    public Queue bonusQueue() {
        return new Queue(bonusQueue, true);
    }
    @Bean
    public Exchange bonusExchange() {
        return ExchangeBuilder.directExchange(bonusExchange).durable(true).build();
    }
    @Bean
    public Binding bonusBinding() {
        return BindingBuilder
            .bind(bonusQueue())
            .to(bonusExchange())
            .with(bonusRoutingKey)
            .noargs();
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory(host);
        cachingConnectionFactory.setUsername(username);
        cachingConnectionFactory.setPassword(password);
        return cachingConnectionFactory;
    }
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
}
