package com.iimetra.skip.service;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

import com.iimetra.skip.model.AdjustmentModifiedEvent;
import com.iimetra.skip.model.BonusModifiedEvent;
import com.iimetra.skip.model.DeliveryCreatedEvent;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class EventProducerServiceTest {

    private EventProducerService eventProducerService;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @BeforeEach
    void setUp() {
        eventProducerService = new EventProducerService(rabbitTemplate);
        ReflectionTestUtils.setField(eventProducerService, "deliveryExchange", "deliveryExchange");
        ReflectionTestUtils.setField(eventProducerService, "deliveryRoutingKey", "deliveryRoutingKey");

        ReflectionTestUtils.setField(eventProducerService, "adjustmentExchange", "adjustmentExchange");
        ReflectionTestUtils.setField(eventProducerService, "adjustmentRoutingKey", "adjustmentRoutingKey");

        ReflectionTestUtils.setField(eventProducerService, "bonusExchange", "bonusExchange");
        ReflectionTestUtils.setField(eventProducerService, "bonusRoutingKey", "bonusRoutingKey");
    }

    @Test
    void testSendDeliveryEvent() {
        DeliveryCreatedEvent event = DeliveryCreatedEvent.builder()
            .deliveryId("deliveryId")
            .courierId("courierId")
            .value(BigDecimal.TEN)
            .createdTimestamp("2023-04-15 12:00:00")
            .build();

        eventProducerService.sendDelivery(event);

        verify(rabbitTemplate).convertAndSend(eq("deliveryExchange"), eq("deliveryRoutingKey"), eq(event));
    }

    @Test
    void testSendAdjustmentEvent() {
        AdjustmentModifiedEvent event = AdjustmentModifiedEvent.builder()
            .deliveryId("deliveryId")
            .adjustmentId("adjustmentId")
            .value(BigDecimal.TEN)
            .modifiedTimestamp("2023-04-15 12:00:00")
            .build();

        eventProducerService.sendAdjustment(event);

        verify(rabbitTemplate).convertAndSend(eq("adjustmentExchange"), eq("adjustmentRoutingKey"), eq(event));
    }

    @Test
    void testSendBonusEvent() {
        BonusModifiedEvent event = BonusModifiedEvent.builder()
            .deliveryId("deliveryId")
            .bonusId("bonusId")
            .value(BigDecimal.TEN)
            .modifiedTimestamp("2023-04-15 12:00:00")
            .build();

        eventProducerService.sendBonus(event);

        verify(rabbitTemplate).convertAndSend(eq("bonusExchange"), eq("bonusRoutingKey"), eq(event));
    }
}
