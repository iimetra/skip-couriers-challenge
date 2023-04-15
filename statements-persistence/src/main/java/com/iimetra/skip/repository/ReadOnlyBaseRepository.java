package com.iimetra.skip.repository;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

@NoRepositoryBean
public interface ReadOnlyBaseRepository<T, ID> extends Repository<T, ID> {

    Iterable<T> findAll();
}
