package com.iimetra.skip.impl.service;

import com.iimetra.skip.repository.ReadOnlyStatementsRepository;
import com.iimetra.skip.repository.entity.StatementEntity;
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
public class StatementsService {

    private final ReadOnlyStatementsRepository statementRepository;

    public List<StatementEntity> getStatementsBy(String courierId) {
        List<StatementEntity> foundStatements = statementRepository.findByCourierIdWithinPastWeek(courierId, Timestamp.from(Instant.now().minus(7, ChronoUnit.DAYS)));
        log.info("Found {} statements for courier {} for past week", courierId, foundStatements.size());
        return foundStatements;
    }

    public List<StatementEntity> getStatementsWithin(String courierId, String start, String end) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        List<StatementEntity> foundStatements = statementRepository.findByCourierIdBetweenPeriod(
            courierId,
            Timestamp.valueOf(LocalDateTime.parse(start, df)),
            Timestamp.valueOf(LocalDateTime.parse(end, df))
        );

        log.info("Found {} statements for courier {} for timeframe [{} - {}]", foundStatements.size(), courierId, start, end);

        return foundStatements;
    }
}
