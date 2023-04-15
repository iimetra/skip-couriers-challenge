package com.iimetra.skip.repository;

import com.iimetra.skip.repository.entity.Statement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatementRepository extends JpaRepository<Statement, String> {//Todo describe model
}
