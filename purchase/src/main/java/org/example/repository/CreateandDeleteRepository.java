package org.example.repository;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;


@NoRepositoryBean
public interface CreateandDeleteRepository<T,ID> extends Repository<T,ID> {
    <S extends T> Mono<S> save(S entity) ;
    Mono<Void> delete(T entity);
}
