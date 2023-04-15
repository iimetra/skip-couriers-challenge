package com.iimetra.skip.service;

import com.iimetra.skip.model.AdjustmentModifiedEvent;
import com.iimetra.skip.model.BonusModifiedEvent;
import com.iimetra.skip.model.DeliveryCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventProducerService {

    @Value("${spring.rabbitmq.deliveryExchange}")
    private String deliveryExchange;
    @Value("${spring.rabbitmq.deliveryRoutingKey}")
    private String deliveryRoutingKey;

    @Value("${spring.rabbitmq.adjustmentExchange}")
    private String adjustmentExchange;
    @Value("${spring.rabbitmq.adjustmentRoutingKey}")
    private String adjustmentRoutingKey;

    @Value("${spring.rabbitmq.bonusExchange}")
    private String bonusExchange;
    @Value("${spring.rabbitmq.bonusRoutingKey}")
    private String bonusRoutingKey;

    private final RabbitTemplate rabbitTemplate;

    public void sendDelivery(DeliveryCreatedEvent event) {
        log.info("Sending event {} to {} exchange", event.getDeliveryId(), deliveryExchange);
        rabbitTemplate.convertAndSend(deliveryExchange, deliveryRoutingKey, event);
    }

    public void sendAdjustment(AdjustmentModifiedEvent event) {
        log.info("Sending event {} to {} exchange", event.getDeliveryId(), adjustmentExchange);
        rabbitTemplate.convertAndSend(adjustmentExchange, adjustmentRoutingKey, event);
    }

    public void sendBonus(BonusModifiedEvent event) {
        log.info("Sending event {} to {} exchange", event.getDeliveryId(), bonusExchange);
        rabbitTemplate.convertAndSend(bonusExchange, bonusRoutingKey, event);
    }
}
