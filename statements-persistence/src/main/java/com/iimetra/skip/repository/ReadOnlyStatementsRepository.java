package com.iimetra.skip.repository;

import com.iimetra.skip.repository.entity.StatementEntity;
import java.sql.Timestamp;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReadOnlyStatementsRepository extends ReadOnlyBaseRepository<StatementEntity, String> {

    @Query("select s from StatementEntity s where s.courierId = :courierId and s.lastModifiedAt > :weekAgo")
    List<StatementEntity> findByCourierIdWithinPastWeek(@Param("courierId") String courierId, @Param("weekAgo") Timestamp weekAgo);

    @Query("select s from StatementEntity s where s.courierId = :courierId and s.lastModifiedAt between :start and :end")
    List<StatementEntity> findByCourierIdBetweenPeriod(@Param("courierId") String courierId, @Param("start") Timestamp start, @Param("end") Timestamp end);
}
