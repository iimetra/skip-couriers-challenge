package com.iimetra.skip.impl.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.iimetra.skip.repository.ReadOnlyDeliveryRecordRepository;
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

@ExtendWith(MockitoExtension.class)
class DeliveryServiceTest {

    @InjectMocks
    private DeliveryService deliveryService;

    @Mock
    private ReadOnlyDeliveryRecordRepository statementsRepository;

    @Test
    void testPastWeekStatements() {
        String courierId = "courierId";

        List<DeliveryRecordEntity> statementEntities = List.of(
            DeliveryRecordEntity.builder()
                .deliveryRecordId("statementId1")
                .value(BigDecimal.TEN)
                .deliveryId("deliveryId")
                .courierId("courierId")
                .lastModifiedAt(Timestamp.valueOf(LocalDateTime.now()))
                .build(),
            DeliveryRecordEntity.builder()
                .deliveryRecordId("statementId2")
                .value(BigDecimal.ONE)
                .deliveryId("deliveryId")
                .courierId("courierId")
                .lastModifiedAt(Timestamp.valueOf(LocalDateTime.now()))
                .build()
        );
        when(statementsRepository.findByCourierIdWithinPastWeek(eq(courierId), any())).thenReturn(statementEntities);

        List<DeliveryRecordEntity> pastWeekStatements = deliveryService.getPastWeekDeliveriesBy(courierId);
        assertNotNull(pastWeekStatements);
        assertFalse(pastWeekStatements.isEmpty());
        assertEquals(2, pastWeekStatements.size());

        verify(statementsRepository).findByCourierIdWithinPastWeek(any(), any());
    }

    @Test
    void testStatementsBetweenDates() {
        String courierId = "courierId";

        List<DeliveryRecordEntity> statementEntities = List.of(
            DeliveryRecordEntity.builder()
                .deliveryRecordId("statementId1")
                .value(BigDecimal.TEN)
                .deliveryId("deliveryId")
                .courierId("courierId")
                .lastModifiedAt(Timestamp.valueOf(LocalDateTime.now()))
                .build(),
            DeliveryRecordEntity.builder()
                .deliveryRecordId("statementId2")
                .value(BigDecimal.ONE)
                .deliveryId("deliveryId")
                .courierId("courierId")
                .lastModifiedAt(Timestamp.valueOf(LocalDateTime.now()))
                .build()
        );

        when(statementsRepository.findByCourierIdBetweenPeriod(eq(courierId), any(), any())).thenReturn(statementEntities);

        List<DeliveryRecordEntity> statements = deliveryService.getDeliveriesBetween(courierId, "2023-04-14 12:00:00", "2023-04-15 13:00:00");
        assertNotNull(statements);
        assertFalse(statements.isEmpty());
        assertEquals(2, statements.size());

        verify(statementsRepository).findByCourierIdBetweenPeriod(any(), any(), any());
    }
}
