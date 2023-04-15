package com.iimetra.skip.impl.service;

import com.iimetra.skip.repository.ReadOnlyDeliveryRecordRepository;
import com.iimetra.skip.repository.entity.DeliveryRecordEntity;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeliveryService {

    private final ReadOnlyDeliveryRecordRepository statementRepository;

    public List<DeliveryRecordEntity> getPastWeekDeliveriesBy(String courierId) {
        List<DeliveryRecordEntity> foundStatements = statementRepository.findByCourierIdWithinPastWeek(courierId, Timestamp.from(Instant.now().minus(7, ChronoUnit.DAYS)));
        log.info("Found {} statements for courier {} for past week", courierId, foundStatements.size());
        return foundStatements;
    }

    public List<DeliveryRecordEntity> getDeliveriesBetween(String courierId, String start, String end) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        List<DeliveryRecordEntity> foundStatements = statementRepository.findByCourierIdBetweenPeriod(
            courierId,
            Timestamp.valueOf(LocalDateTime.parse(start, df)),
            Timestamp.valueOf(LocalDateTime.parse(end, df))
        );

        log.info("Found {} statements for courier {} for timeframe [{} - {}]", foundStatements.size(), courierId, start, end);

        return foundStatements;
    }
}
