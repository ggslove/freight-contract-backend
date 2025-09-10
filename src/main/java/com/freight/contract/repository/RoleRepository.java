package com.freight.contract.repository;

import com.freight.contract.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(String name);

    boolean existsByName(String name);

    boolean existsByCode(String code);

    Optional<Role> findByCode(String code);
}