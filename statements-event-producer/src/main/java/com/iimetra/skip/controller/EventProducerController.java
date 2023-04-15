package com.iimetra.skip.controller;

import com.iimetra.skip.api.EventsApi;
import com.iimetra.skip.model.AdjustmentModifiedEvent;
import com.iimetra.skip.model.AdjustmentRequest;
import com.iimetra.skip.model.BonusModifiedEvent;
import com.iimetra.skip.model.BonusRequest;
import com.iimetra.skip.model.CreatedAdjustmentResponse;
import com.iimetra.skip.model.CreatedBonusResponse;
import com.iimetra.skip.model.CreatedDeliveryResponse;
import com.iimetra.skip.model.DeliveryCreatedEvent;
import com.iimetra.skip.model.DeliveryRequest;
import com.iimetra.skip.producer.EventProducerService;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class EventProducerController implements EventsApi {

    private final EventProducerService eventProducerService;

    @Override
    public ResponseEntity<CreatedAdjustmentResponse> createAdjustment(AdjustmentRequest adjustmentRequest) {
        AdjustmentModifiedEvent event = AdjustmentModifiedEvent.builder()
            .deliveryId(adjustmentRequest.getDeliveryId())
            .value(adjustmentRequest.getValue())
            .adjustmentId(UUID.randomUUID().toString())
            .modifiedTimestamp(
                LocalDateTime.now().toString()
            )
            .build();

        eventProducerService.sendAdjustment(event);
        log.info("Successfully submitted adjustment event: {}", event.getAdjustmentId());
        return ResponseEntity.ok(new CreatedAdjustmentResponse(event.getAdjustmentId()));
    }

    @Override
    public ResponseEntity<CreatedBonusResponse> createBonus(BonusRequest bonusRequest) {
        BonusModifiedEvent event = BonusModifiedEvent.builder()
            .deliveryId(bonusRequest.getDeliveryId())
            .value(bonusRequest.getValue())
            .bonusId(UUID.randomUUID().toString())
            .modifiedTimestamp(
                LocalDateTime.now().toString()
            )
            .build();

        eventProducerService.sendBonus(event);
        log.info("Successfully submitted bonus event: {}", event.getBonusId());
        return ResponseEntity.ok(new CreatedBonusResponse(event.getBonusId()));
    }

    @Override
    public ResponseEntity<CreatedDeliveryResponse> createDelivery(DeliveryRequest deliveryRequest) {
        DeliveryCreatedEvent event = DeliveryCreatedEvent.builder()
            .courierId(deliveryRequest.getCourierId())
            .value(deliveryRequest.getValue())
            .deliveryId(UUID.randomUUID().toString())
            .createdTimestamp(
                LocalDateTime.now().toString()
            )
            .build();

        eventProducerService.sendDelivery(event);
        log.info("Successfully submitted delivery event: {}", event.getDeliveryId());
        return ResponseEntity.ok(new CreatedDeliveryResponse(event.getDeliveryId()));
    }
}
