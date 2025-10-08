package com.freight.contract.config;

import com.freight.contract.config.repository.CursorPagingRepositoryFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(
    basePackages = "com.freight.contract.repository",
    repositoryFactoryBeanClass = CursorPagingRepositoryFactory.class
)
public class RepositoryConfig {
}