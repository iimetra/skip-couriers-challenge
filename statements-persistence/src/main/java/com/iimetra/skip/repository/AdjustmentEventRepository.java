package com.iimetra.skip.repository;

import com.iimetra.skip.repository.entity.AdjustmentModifiedEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdjustmentEventRepository extends JpaRepository<AdjustmentModifiedEntity, String> {
}
