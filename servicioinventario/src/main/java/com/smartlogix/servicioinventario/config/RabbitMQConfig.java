package com.smartlogix.servicioinventario.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String STOCK_QUEUE = "stock.critico.queue";
    public static final String STOCK_EXCHANGE = "stock.exchange";
    public static final String STOCK_ROUTING_KEY = "stock.critico";

    @Bean
    public Queue stockQueue() {
        return new Queue(STOCK_QUEUE, true);
    }

    @Bean
    public DirectExchange stockExchange() {
        return new DirectExchange(STOCK_EXCHANGE);
    }

    @Bean
    public Binding stockBinding(Queue stockQueue, DirectExchange stockExchange) {
        return BindingBuilder
                .bind(stockQueue)
                .to(stockExchange)
                .with(STOCK_ROUTING_KEY);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        return new RabbitTemplate(connectionFactory);
    }
}