package com.freight.contract.config.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

import javax.persistence.EntityManager;
import java.io.Serializable;

public class CursorPagingRepositoryFactory<R extends JpaRepository<T, ID>, T, ID extends Serializable>
    extends JpaRepositoryFactoryBean<R, T, ID> {

    public CursorPagingRepositoryFactory(Class<? extends R> repositoryInterface) {
        super(repositoryInterface);
    }

    @Override
    protected RepositoryFactorySupport createRepositoryFactory(EntityManager entityManager) {
        return new CursorPagingRepositoryFactoryBean(entityManager);
    }

    private static class CursorPagingRepositoryFactoryBean<T, ID extends Serializable> 
        extends JpaRepositoryFactory {

        public CursorPagingRepositoryFactoryBean(EntityManager entityManager) {
            super(entityManager);
        }

        @Override
        protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
            if (CursorPagingRepository.class.isAssignableFrom(metadata.getRepositoryInterface())) {
                return CursorPagingRepositoryImpl.class;
            }
            return super.getRepositoryBaseClass(metadata);
        }
    }
}