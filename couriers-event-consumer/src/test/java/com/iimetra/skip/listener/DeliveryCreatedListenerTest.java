package com.iimetra.skip.listener;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;

import com.iimetra.skip.model.DeliveryCreatedEvent;
import com.iimetra.skip.repository.DeliveryEventRepository;
import com.iimetra.skip.repository.DeliveryRecordRepository;
import com.iimetra.skip.repository.entity.DeliveryCreatedEntity;
import com.iimetra.skip.repository.entity.DeliveryRecordEntity;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DeliveryCreatedListenerTest {

    @InjectMocks
    private DeliveryCreatedListener listener;

    @Mock
    private DeliveryRecordRepository deliveryRecordRepository;
    @Mock
    private DeliveryEventRepository deliveryEventRepository;

    @Test
    void testReceiveEvent() {
        DeliveryCreatedEvent event = DeliveryCreatedEvent.builder()
            .deliveryId("deliveryId")
            .courierId("courierId")
            .value(BigDecimal.TEN)
            .createdTimestamp(LocalDateTime.now().toString())
            .build();

        listener.receive(event);

        ArgumentCaptor<DeliveryCreatedEntity> deliveryCaptor = ArgumentCaptor.forClass(DeliveryCreatedEntity.class);
        verify(deliveryEventRepository).save(deliveryCaptor.capture());
        DeliveryCreatedEntity deliveryEntity = deliveryCaptor.getValue();
        assertNotNull(deliveryEntity);
        assertEquals(event.getDeliveryId(), deliveryEntity.getDeliveryId());
        assertEquals(event.getValue(), deliveryEntity.getValue());
        assertEquals(event.getCourierId(), deliveryEntity.getCourierId());
        assertNotNull(deliveryEntity.getCreatedTimestamp());

        ArgumentCaptor<DeliveryRecordEntity> statementCaptor = ArgumentCaptor.forClass(DeliveryRecordEntity.class);
        verify(deliveryRecordRepository).save(statementCaptor.capture());
        DeliveryRecordEntity deliveryRecordEntity = statementCaptor.getValue();
        assertNotNull(deliveryRecordEntity);
        assertNotNull(deliveryRecordEntity.getDeliveryRecordId());
        assertEquals(event.getDeliveryId(), deliveryRecordEntity.getDeliveryId());
        assertEquals(event.getCourierId(), deliveryRecordEntity.getCourierId());
        assertEquals(event.getValue(), deliveryRecordEntity.getValue());
        assertNotNull(deliveryRecordEntity.getLastModifiedAt());
    }
}
