package org.example.repository;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;


@NoRepositoryBean
public interface CreateandDeleteRepository<T,ID> extends Repository<T,ID> {
    <S extends T> S save(S entity) ;
    void delete(T entity);
}
