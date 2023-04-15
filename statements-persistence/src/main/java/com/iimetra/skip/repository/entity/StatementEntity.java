package com.iimetra.skip.repository.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class StatementEntity {

    @Id
    private String statementId;

    private String deliveryId;

    private String courierId;

    private BigDecimal value;

    private Timestamp lastModifiedAt;
}
