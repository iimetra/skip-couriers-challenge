package com.iimetra.skip.repository;

import com.iimetra.skip.repository.entity.DeliveryRecordEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryRecordRepository extends JpaRepository<DeliveryRecordEntity, String> {

    Optional<DeliveryRecordEntity> findRecordByDeliveryId(String deliveryId);

}
