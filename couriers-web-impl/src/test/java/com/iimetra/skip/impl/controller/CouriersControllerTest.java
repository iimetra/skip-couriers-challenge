package com.iimetra.skip.impl.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import com.iimetra.skip.impl.service.DeliveryService;
import com.iimetra.skip.model.DeliveryResponse;
import com.iimetra.skip.model.DeliveryTransactionsResponse;
import com.iimetra.skip.model.StatementResponse;
import com.iimetra.skip.repository.entity.DeliveryRecordEntity;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class CouriersControllerTest {

    @InjectMocks
    private CouriersController couriersController;

    @Mock
    private DeliveryService deliveryService;

    @Test
    void testGetDeliveries() {
        String courierId = "courierId";
        String start = "2023-04-14 12:00:00";
        String end = "2023-04-15 13:00:00";

        List<DeliveryRecordEntity> statementEntities = List.of(
            DeliveryRecordEntity.builder()
                .deliveryRecordId("statementId1")
                .value(BigDecimal.TEN)
                .deliveryId("deliveryId1")
                .courierId("courierId")
                .lastModifiedAt(Timestamp.valueOf(LocalDateTime.now()))
                .build(),
            DeliveryRecordEntity.builder()
                .deliveryRecordId("statementId2")
                .value(BigDecimal.ONE)
                .deliveryId("deliveryId2")
                .courierId("courierId")
                .lastModifiedAt(Timestamp.valueOf(LocalDateTime.now()))
                .build()
        );

        when(deliveryService.getDeliveriesBetween(eq(courierId), eq(start), eq(end))).thenReturn(statementEntities);

        ResponseEntity<DeliveryTransactionsResponse> response = couriersController.getCourierDeliveries(courierId, start, end);
        DeliveryTransactionsResponse deliveryTransactionsResponse = response.getBody();
        assertNotNull(deliveryTransactionsResponse);
        assertEquals(start, deliveryTransactionsResponse.getFrom());
        assertEquals(end, deliveryTransactionsResponse.getTo());
        assertEquals(courierId, deliveryTransactionsResponse.getCourierId());
        assertEquals(2, deliveryTransactionsResponse.getNumberOfTransactions());

        List<DeliveryResponse> deliveries = deliveryTransactionsResponse.getDeliveries();
        assertNotNull(deliveries);
        assertFalse(deliveries.isEmpty());
        assertEquals(2, deliveries.size());
        deliveries.forEach(deliveryResponse -> {
            assertNotNull(deliveryResponse);
            assertNotNull(deliveryResponse.getDeliveryId());
            assertNotNull(deliveryResponse.getSum());
        });
    }

    @Test
    void testGetWeeklyStatements() {
        String courierId = "courierId";

        List<DeliveryRecordEntity> statementEntities = List.of(
            DeliveryRecordEntity.builder()
                .deliveryRecordId("statementId1")
                .value(BigDecimal.TEN)
                .deliveryId("deliveryId1")
                .courierId("courierId")
                .lastModifiedAt(Timestamp.valueOf(LocalDateTime.now()))
                .build(),
            DeliveryRecordEntity.builder()
                .deliveryRecordId("statementId2")
                .value(BigDecimal.ONE)
                .deliveryId("deliveryId2")
                .courierId("courierId")
                .lastModifiedAt(Timestamp.valueOf(LocalDateTime.now()))
                .build()
        );

        when(deliveryService.getPastWeekDeliveriesBy(eq(courierId))).thenReturn(statementEntities);

        ResponseEntity<StatementResponse> response = couriersController.getCourierStatement(courierId, null, null);
        assertNotNull(response);
        StatementResponse statementResponse = response.getBody();
        assertNotNull(statementResponse);
        assertEquals(BigDecimal.valueOf(11), statementResponse.getSum());
        assertEquals(2, statementResponse.getDeliveryRecordIds().size());
        assertEquals(2, statementResponse.getDeliveryIds().size());
    }
}
