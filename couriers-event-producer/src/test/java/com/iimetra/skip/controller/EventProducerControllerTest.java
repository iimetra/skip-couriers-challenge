package com.iimetra.skip.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;

import com.iimetra.skip.model.AdjustmentModifiedEvent;
import com.iimetra.skip.model.AdjustmentRequest;
import com.iimetra.skip.model.BonusModifiedEvent;
import com.iimetra.skip.model.BonusRequest;
import com.iimetra.skip.model.CreatedAdjustmentResponse;
import com.iimetra.skip.model.CreatedBonusResponse;
import com.iimetra.skip.model.CreatedDeliveryResponse;
import com.iimetra.skip.model.DeliveryCreatedEvent;
import com.iimetra.skip.model.DeliveryRequest;
import com.iimetra.skip.service.EventProducerService;
import java.math.BigDecimal;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class EventProducerControllerTest {

    @InjectMocks
    private EventProducerController controller;

    @Mock
    private EventProducerService service;

    @Test
    void testCreateAdjustment() {
        AdjustmentRequest request = AdjustmentRequest.builder()
            .deliveryId("deliveryId")
            .value(BigDecimal.ONE)
            .build();

        ResponseEntity<CreatedAdjustmentResponse> response = controller.createAdjustment(request);
        assertNotNull(response);
        CreatedAdjustmentResponse adjustmentResponse = response.getBody();
        assertNotNull(adjustmentResponse);
        assertTrue(StringUtils.isNotBlank(adjustmentResponse.getAdjustmentId()));

        ArgumentCaptor<AdjustmentModifiedEvent> adjustmentCaptor = ArgumentCaptor.forClass(AdjustmentModifiedEvent.class);
        verify(service).sendAdjustment(adjustmentCaptor.capture());
        AdjustmentModifiedEvent event = adjustmentCaptor.getValue();
        assertNotNull(event);
        assertEquals(adjustmentResponse.getAdjustmentId(), event.getAdjustmentId());
        assertEquals(request.getDeliveryId(), event.getDeliveryId());
        assertEquals(request.getValue(), event.getValue());
        assertNotNull(event.getModifiedTimestamp());
    }

    @Test
    void testCreateBonus() {
        BonusRequest request = BonusRequest.builder()
            .deliveryId("deliveryId")
            .value(BigDecimal.ONE)
            .build();

        ResponseEntity<CreatedBonusResponse> response = controller.createBonus(request);
        assertNotNull(response);
        CreatedBonusResponse bonusResponse = response.getBody();
        assertNotNull(bonusResponse);
        assertTrue(StringUtils.isNotBlank(bonusResponse.getBonusId()));

        ArgumentCaptor<BonusModifiedEvent> bonusCaptor = ArgumentCaptor.forClass(BonusModifiedEvent.class);
        verify(service).sendBonus(bonusCaptor.capture());
        BonusModifiedEvent event = bonusCaptor.getValue();
        assertNotNull(event);
        assertEquals(bonusResponse.getBonusId(), event.getBonusId());
        assertEquals(request.getDeliveryId(), event.getDeliveryId());
        assertEquals(request.getValue(), event.getValue());
        assertNotNull(event.getModifiedTimestamp());
    }

    @Test
    void testCreateDelivery() {
        DeliveryRequest request = DeliveryRequest.builder()
            .courierId("courierId")
            .value(BigDecimal.ONE)
            .build();

        ResponseEntity<CreatedDeliveryResponse> response = controller.createDelivery(request);
        assertNotNull(response);
        CreatedDeliveryResponse deliveryResponse = response.getBody();
        assertNotNull(deliveryResponse);
        assertTrue(StringUtils.isNotBlank(deliveryResponse.getDeliveryId()));

        ArgumentCaptor<DeliveryCreatedEvent> deliveryCaptor = ArgumentCaptor.forClass(DeliveryCreatedEvent.class);
        verify(service).sendDelivery(deliveryCaptor.capture());
        DeliveryCreatedEvent event = deliveryCaptor.getValue();
        assertNotNull(event);
        assertEquals(deliveryResponse.getDeliveryId(), event.getDeliveryId());
        assertEquals(request.getCourierId(), event.getCourierId());
        assertEquals(request.getValue(), event.getValue());
        assertNotNull(event.getCreatedTimestamp());
    }
}
