package com.freight.contract.graphql;

import com.freight.contract.entity.Role;
import com.freight.contract.service.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@AllArgsConstructor
public class RoleResolver {
    
    private final RoleService roleService;
    
    @QueryMapping
    public List<Role> roles() {
        return roleService.getAllRoles();
    }
    
    @QueryMapping
    public Role role(@Argument Long id) {
        return roleService.getRoleById(id).orElse(null);
    }
    
    @MutationMapping
    public Role createRole(@Argument String name, @Argument String description) {
        Role role = new Role();
        role.setName(name);
        role.setDescription(description);
        return roleService.createRole(role);
    }
    
    @MutationMapping
    public Role updateRole(@Argument Long id, @Argument String name, @Argument String description) {
        Role roleDetails = new Role();
        roleDetails.setName(name);
        roleDetails.setDescription(description);
        return roleService.updateRole(id, roleDetails).orElse(null);
    }
    
    @MutationMapping
    public Boolean deleteRole(@Argument Long id) {
        return roleService.deleteRole(id);
    }
}