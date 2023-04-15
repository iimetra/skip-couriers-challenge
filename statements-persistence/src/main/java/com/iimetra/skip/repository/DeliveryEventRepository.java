package com.iimetra.skip.repository;

import com.iimetra.skip.repository.entity.DeliveryCreatedEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryEventRepository extends JpaRepository<DeliveryCreatedEntity, String> {
}
