package com.iimetra.skip.listener;

import com.iimetra.skip.model.DeliveryCreatedEvent;
import com.iimetra.skip.repository.DeliveryEventRepository;
import com.iimetra.skip.repository.DeliveryRecordRepository;
import com.iimetra.skip.repository.entity.DeliveryCreatedEntity;
import com.iimetra.skip.repository.entity.DeliveryRecordEntity;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
@RabbitListener(queues = "${spring.rabbitmq.deliveryQueue}")
public class DeliveryCreatedListener {

    private final DeliveryRecordRepository deliveryRecordRepository;
    private final DeliveryEventRepository deliveryEventRepository;

    @RabbitHandler
    @Transactional
    public void receive(DeliveryCreatedEvent event) {
        String deliveryId = event.getDeliveryId();
        log.info("Received delivery created event: {}", deliveryId);

        DeliveryCreatedEntity deliveryCreatedEntity = DeliveryCreatedEntity.builder()
            .deliveryId(event.getDeliveryId())
            .courierId(event.getCourierId())
            .createdTimestamp(Timestamp.valueOf(LocalDateTime.parse(event.getCreatedTimestamp())))
            .value(event.getValue())
            .build();
        deliveryEventRepository.save(deliveryCreatedEntity);

        DeliveryRecordEntity deliveryRecordEntity = DeliveryRecordEntity.builder()
            .deliveryRecordId(UUID.randomUUID().toString())
            .deliveryId(event.getDeliveryId())
            .lastModifiedAt(Timestamp.valueOf(LocalDateTime.parse(event.getCreatedTimestamp())))
            .courierId(event.getCourierId())
            .value(event.getValue())
            .build();
        deliveryRecordRepository.save(deliveryRecordEntity);

        log.info("Successfully processed delivery created event: {}", deliveryId);
    }
}
