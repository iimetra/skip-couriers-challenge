package com.iimetra.skip.repository;

import com.iimetra.skip.repository.entity.DeliveryRecordEntity;
import java.sql.Timestamp;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReadOnlyDeliveryRecordRepository extends ReadOnlyBaseRepository<DeliveryRecordEntity, String> {

    @Query("select s from DeliveryRecordEntity s where s.courierId = :courierId and s.lastModifiedAt > :weekAgo")
    List<DeliveryRecordEntity> findByCourierIdWithinPastWeek(@Param("courierId") String courierId, @Param("weekAgo") Timestamp weekAgo);

    @Query("select s from DeliveryRecordEntity s where s.courierId = :courierId and s.lastModifiedAt between :start and :end")
    List<DeliveryRecordEntity> findByCourierIdBetweenPeriod(@Param("courierId") String courierId, @Param("start") Timestamp start, @Param("end") Timestamp end);
}
