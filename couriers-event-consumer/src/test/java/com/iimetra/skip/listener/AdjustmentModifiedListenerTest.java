package com.iimetra.skip.listener;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.iimetra.skip.model.AdjustmentModifiedEvent;
import com.iimetra.skip.repository.AdjustmentEventRepository;
import com.iimetra.skip.repository.DeliveryRecordRepository;
import com.iimetra.skip.repository.entity.AdjustmentModifiedEntity;
import com.iimetra.skip.repository.entity.DeliveryRecordEntity;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;
import javax.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AdjustmentModifiedListenerTest {

    @InjectMocks
    private AdjustmentModifiedListener listener;

    @Mock
    private DeliveryRecordRepository deliveryRecordRepository;
    @Mock
    private AdjustmentEventRepository adjustmentEventRepository;

    @Test
    void testReceiveEvent_noDeliveryFound() {
        AdjustmentModifiedEvent event = AdjustmentModifiedEvent.builder()
            .adjustmentId("adjustmentId")
            .value(BigDecimal.TEN)
            .deliveryId("deliveryId")
            .modifiedTimestamp(LocalDateTime.now().toString())
            .build();

        when(deliveryRecordRepository.findRecordByDeliveryId(eq(event.getDeliveryId()))).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> listener.receive(event), String.format("There is no delivery %s found", event.getDeliveryId()));
    }

    @Test
    void testReceiveEvent() {
        AdjustmentModifiedEvent event = AdjustmentModifiedEvent.builder()
            .adjustmentId("adjustmentId")
            .value(BigDecimal.TEN)
            .deliveryId("deliveryId")
            .modifiedTimestamp(LocalDateTime.now().toString())
            .build();

        DeliveryRecordEntity deliveryRecordEntity = DeliveryRecordEntity.builder()
            .deliveryRecordId("statementId")
            .deliveryId("deliveryId")
            .courierId("courierId")
            .value(BigDecimal.TEN)
            .lastModifiedAt(Timestamp.valueOf(LocalDateTime.parse(event.getModifiedTimestamp())))
            .build();
        when(deliveryRecordRepository.findRecordByDeliveryId(eq(event.getDeliveryId()))).thenReturn(Optional.of(deliveryRecordEntity));

        listener.receive(event);

        ArgumentCaptor<AdjustmentModifiedEntity> adjustmentCaptor = ArgumentCaptor.forClass(AdjustmentModifiedEntity.class);
        verify(adjustmentEventRepository).save(adjustmentCaptor.capture());
        AdjustmentModifiedEntity adjustmentModifiedEntity = adjustmentCaptor.getValue();
        assertNotNull(adjustmentModifiedEntity);
        assertEquals(event.getAdjustmentId(), adjustmentModifiedEntity.getAdjustmentId());
        assertEquals(event.getValue(), adjustmentModifiedEntity.getValue());
        assertEquals(event.getDeliveryId(), adjustmentModifiedEntity.getDeliveryId());
        assertNotNull(adjustmentModifiedEntity.getModifiedTimestamp());

        ArgumentCaptor<DeliveryRecordEntity> statementCaptor = ArgumentCaptor.forClass(DeliveryRecordEntity.class);
        verify(deliveryRecordRepository).save(statementCaptor.capture());
        DeliveryRecordEntity updatedStatement = statementCaptor.getValue();
        assertNotNull(updatedStatement);
        assertNotNull(updatedStatement.getDeliveryRecordId());
        assertEquals(BigDecimal.valueOf(20), updatedStatement.getValue());
        assertEquals(event.getDeliveryId(), updatedStatement.getDeliveryId());
        assertEquals(deliveryRecordEntity.getCourierId(), updatedStatement.getCourierId());
    }
}
