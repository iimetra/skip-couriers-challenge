package com.iimetra.skip.impl;

import com.iimetra.skip.api.CouriersApi;
import com.iimetra.skip.impl.service.StatementsService;
import com.iimetra.skip.model.DeliveryResponse;
import com.iimetra.skip.model.DeliveryTransactionsResponse;
import com.iimetra.skip.model.StatementsResponse;
import com.iimetra.skip.repository.entity.StatementEntity;
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

    private final StatementsService statementsService;

    @Override
    public ResponseEntity<DeliveryTransactionsResponse> getDeliveries(String id, String start, String end) {
        log.info("Received request to get deliveries for courier: {}, from {} to {}", id, start, end);

        List<StatementEntity> statementsWithin = statementsService.getStatementsWithin(id, start, end);

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
    public ResponseEntity<StatementsResponse> getStatements(String id, String start, String end) {
        log.info("Received request to get statement for courier: {}", id);

        List<StatementEntity> statementsWithin;
        if (!StringUtils.isAllBlank(start, end)) {
            log.info("Getting statements for provided timeframe [{} - {}] for courier: {}", start, end, id);
            statementsWithin = statementsService.getStatementsWithin(id, start, end);
        } else {
            log.info("Getting weekly statements for courier: {}", id);
            statementsWithin = statementsService.getStatementsBy(id);
        }

        StatementsResponse response = StatementsResponse.builder()
            .statementIds(statementsWithin.stream().map(StatementEntity::getStatementId).collect(Collectors.toList()))
            .deliveryIds(statementsWithin.stream().map(StatementEntity::getDeliveryId).collect(Collectors.toList()))
            .sum(statementsWithin.stream().map(StatementEntity::getValue).reduce(BigDecimal::add).orElse(BigDecimal.ZERO))
            .build();
        return ResponseEntity.ok(response);
    }
}
