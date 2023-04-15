package com.iimetra.skip.listener;

import com.iimetra.skip.model.AdjustmentModifiedEvent;
import com.iimetra.skip.repository.AdjustmentEventRepository;
import com.iimetra.skip.repository.DeliveryRecordRepository;
import com.iimetra.skip.repository.entity.AdjustmentModifiedEntity;
import com.iimetra.skip.repository.entity.DeliveryRecordEntity;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
@RabbitListener(queues = "${spring.rabbitmq.adjustmentQueue}")
public class AdjustmentModifiedListener {

    private final DeliveryRecordRepository deliveryRecordRepository;
    private final AdjustmentEventRepository adjustmentEventRepository;

    @RabbitHandler
    @Transactional
    public void receive(AdjustmentModifiedEvent event) {
        String adjustmentId = event.getAdjustmentId();
        log.info("Received adjustment modified event: {}", adjustmentId);

        String deliveryId = event.getDeliveryId();
        DeliveryRecordEntity deliveryRecordEntity = deliveryRecordRepository.findRecordByDeliveryId(deliveryId)
            .orElseThrow(() -> {
                log.error("No delivery {} found for adjustment {}", deliveryId, adjustmentId);
                return new EntityNotFoundException(String.format("There is no delivery %s found", deliveryId));
            });

        AdjustmentModifiedEntity adjustmentModifiedEntity = AdjustmentModifiedEntity.builder()
            .deliveryId(deliveryId)
            .adjustmentId(event.getAdjustmentId())
            .modifiedTimestamp(Timestamp.valueOf(LocalDateTime.parse(event.getModifiedTimestamp())))
            .value(event.getValue())
            .build();
        adjustmentEventRepository.save(adjustmentModifiedEntity);

        deliveryRecordEntity = DeliveryRecordEntity.builder()
            .courierId(deliveryRecordEntity.getCourierId())
            .deliveryId(deliveryId)
            .deliveryRecordId(deliveryRecordEntity.getDeliveryRecordId())
            .lastModifiedAt(Timestamp.valueOf(LocalDateTime.parse(event.getModifiedTimestamp())))
            .value(deliveryRecordEntity.getValue().add(event.getValue()))
            .build();
        deliveryRecordRepository.save(deliveryRecordEntity);

        log.info("Successfully processed adjustment modified event: {}", adjustmentId);
    }
}
