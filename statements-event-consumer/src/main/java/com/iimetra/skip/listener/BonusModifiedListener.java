package com.iimetra.skip.listener;

import com.iimetra.skip.model.BonusModifiedEvent;
import com.iimetra.skip.repository.BonusEventRepository;
import com.iimetra.skip.repository.StatementRepository;
import com.iimetra.skip.repository.entity.BonusModifiedEntity;
import com.iimetra.skip.repository.entity.StatementEntity;
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

    private final StatementRepository statementRepository;
    private final BonusEventRepository bonusEventRepository;

    @RabbitHandler
    @Transactional
    public void receive(BonusModifiedEvent event) {
        String bonusId = event.getBonusId();
        log.info("Received bonus modified event: {}", bonusId);

        String deliveryId = event.getDeliveryId();
        StatementEntity statementEntity = statementRepository.findStatementByDeliveryId(deliveryId)
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

        statementEntity = StatementEntity.builder()
            .courierId(statementEntity.getCourierId())
            .deliveryId(deliveryId)
            .statementId(statementEntity.getStatementId())
            .lastModifiedAt(Timestamp.valueOf(LocalDateTime.parse(event.getModifiedTimestamp())))
            .value(statementEntity.getValue().add(event.getValue()))
            .build();
        statementRepository.save(statementEntity);

        log.info("Successfully processed bonus modified event: {}", bonusId);
    }
}
