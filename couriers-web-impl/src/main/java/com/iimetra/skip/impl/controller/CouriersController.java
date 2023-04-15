package com.iimetra.skip.impl.controller;

import com.iimetra.skip.api.CouriersApi;
import com.iimetra.skip.impl.service.DeliveryService;
import com.iimetra.skip.model.DeliveryResponse;
import com.iimetra.skip.model.DeliveryTransactionsResponse;
import com.iimetra.skip.model.StatementResponse;
import com.iimetra.skip.repository.entity.DeliveryRecordEntity;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CouriersController implements CouriersApi {

    private final DeliveryService deliveryService;

    @Override
    public ResponseEntity<DeliveryTransactionsResponse> getCourierDeliveries(String id, String start, String end) {
        log.info("Received request to get deliveries for courier: {}, from {} to {}", id, start, end);

        List<DeliveryRecordEntity> statementsWithin = deliveryService.getDeliveriesBetween(id, start, end);

        DeliveryTransactionsResponse response = DeliveryTransactionsResponse.builder()
            .from(start)
            .to(end)
            .courierId(id)
            .numberOfTransactions(statementsWithin.size())
            .deliveries(
                statementsWithin.stream()
                    .map(st -> DeliveryResponse.builder()
                        .deliveryId(st.getDeliveryId())
                        .sum(st.getValue())
                        .build()
                    ).collect(Collectors.toList())
            )
            .build();
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<StatementResponse> getCourierStatement(String id, String start, String end) {
        log.info("Received request to get statement for courier: {}", id);

        List<DeliveryRecordEntity> statementsWithin;
        if (!StringUtils.isAllBlank(start, end)) {
            log.info("Getting statements for provided timeframe [{} - {}] for courier: {}", start, end, id);
            statementsWithin = deliveryService.getDeliveriesBetween(id, start, end);
        } else {
            log.info("Getting weekly statements for courier: {}", id);
            statementsWithin = deliveryService.getPastWeekDeliveriesBy(id);
        }

        StatementResponse response = StatementResponse.builder()
            .deliveryRecordIds(statementsWithin.stream().map(DeliveryRecordEntity::getDeliveryRecordId).collect(Collectors.toList()))
            .deliveryIds(statementsWithin.stream().map(DeliveryRecordEntity::getDeliveryId).collect(Collectors.toList()))
            .sum(statementsWithin.stream().map(DeliveryRecordEntity::getValue).reduce(BigDecimal::add).orElse(BigDecimal.ZERO))
            .build();
        return ResponseEntity.ok(response);
    }
}
