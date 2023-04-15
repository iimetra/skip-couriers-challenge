package com.iimetra.skip.listener;

import com.iimetra.skip.model.DeliveryCreated;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

@Slf4j
@RabbitListener(queues = "${spring.rabbitmq.queue}")
public class DeliveryCreatedListener {

    @RabbitHandler
    public void receive(DeliveryCreated event) {
        log.info("Received delivery created event: {}", event.getDeliveryId());

        log.info("Successfully processed delivery created event: {}", event.getDeliveryId());
    }
}
