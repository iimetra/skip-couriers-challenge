package com.iimetra.skip.repository;

import com.iimetra.skip.repository.entity.BonusModifiedEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BonusEventRepository extends JpaRepository<BonusModifiedEntity, String> {
}
