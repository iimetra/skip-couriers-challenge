package com.iimetra.skip.listener;

import com.iimetra.skip.model.BonusModifiedEvent;
import com.iimetra.skip.repository.BonusEventRepository;
import com.iimetra.skip.repository.DeliveryRecordRepository;
import com.iimetra.skip.repository.entity.BonusModifiedEntity;
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
@RabbitListener(queues = "${spring.rabbitmq.bonusQueue}")
public class BonusModifiedListener {

    private final DeliveryRecordRepository deliveryRecordRepository;
    private final BonusEventRepository bonusEventRepository;

    @RabbitHandler
    @Transactional
    public void receive(BonusModifiedEvent event) {
        String bonusId = event.getBonusId();
        log.info("Received bonus modified event: {}", bonusId);

        String deliveryId = event.getDeliveryId();
        DeliveryRecordEntity deliveryRecordEntity = deliveryRecordRepository.findRecordByDeliveryId(deliveryId)
            .orElseThrow(() -> {
                log.error("No delivery {} found for bonus {}", deliveryId, bonusId);
                return new EntityNotFoundException(String.format("There is no delivery %s found", deliveryId));
            });
        BonusModifiedEntity bonusModifiedEntity = BonusModifiedEntity.builder()
            .deliveryId(deliveryId)
            .bonusId(event.getBonusId())
            .modifiedTimestamp(Timestamp.valueOf(LocalDateTime.parse(event.getModifiedTimestamp())))
            .value(event.getValue())
            .build();
        bonusEventRepository.save(bonusModifiedEntity);

        deliveryRecordEntity = DeliveryRecordEntity.builder()
            .courierId(deliveryRecordEntity.getCourierId())
            .deliveryId(deliveryId)
            .deliveryRecordId(deliveryRecordEntity.getDeliveryRecordId())
            .lastModifiedAt(Timestamp.valueOf(LocalDateTime.parse(event.getModifiedTimestamp())))
            .value(deliveryRecordEntity.getValue().add(event.getValue()))
            .build();
        deliveryRecordRepository.save(deliveryRecordEntity);

        log.info("Successfully processed bonus modified event: {}", bonusId);
    }
}
