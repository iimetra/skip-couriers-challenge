package com.iimetra.skip.repository;

import com.iimetra.skip.repository.entity.StatementEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatementRepository extends JpaRepository<StatementEntity, String> {

    Optional<StatementEntity> findStatementByDeliveryId(String deliveryId);

}
