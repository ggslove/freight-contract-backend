package com.freight.contract.repository;

import com.freight.contract.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    
    List<UserRole> findByUserId(Long userId);
    
    List<UserRole> findByRoleId(Long roleId);
    
    @Query("SELECT ur FROM UserRole ur WHERE ur.user.id = :userId AND ur.role.id = :roleId")
    Optional<UserRole> findByUserIdAndRoleId(@Param("userId") Long userId, @Param("roleId") Long roleId);
    
    void deleteByUserIdAndRoleId(Long userId, Long roleId);
}