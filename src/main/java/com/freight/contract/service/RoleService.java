package com.freight.contract.service;

import com.freight.contract.entity.Role;
import com.freight.contract.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {
    
    @Autowired
    private RoleRepository roleRepository;
    
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }
    
    public Optional<Role> getRoleById(Long id) {
        return roleRepository.findById(id);
    }
    
    public Optional<Role> getRoleByName(String name) {
        return roleRepository.findByName(name);
    }
    
    public Role createRole(Role role) {
        return roleRepository.save(role);
    }
    
    public Optional<Role> updateRole(Long id, Role roleDetails) {
        return roleRepository.findById(id)
                .map(role -> {
                    role.setName(roleDetails.getName());
                    role.setDescription(roleDetails.getDescription());
                    return roleRepository.save(role);
                });
    }
    
    public boolean deleteRole(Long id) {
        return roleRepository.findById(id)
                .map(role -> {
                    roleRepository.delete(role);
                    return true;
                })
                .orElse(false);
    }
}