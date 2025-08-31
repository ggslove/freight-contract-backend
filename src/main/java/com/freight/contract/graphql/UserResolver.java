package com.freight.contract.graphql;

import com.freight.contract.entity.User;
import com.freight.contract.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserResolver {
    
    private final UserService userService;
    
    @QueryMapping
    public List<User> users() {
        return userService.getAllUsers();
    }
    
    @QueryMapping
    public User user(@Argument Long id) {
        return userService.getUserById(id).orElse(null);
    }
    
    @QueryMapping
    public User userByUsername(@Argument String username) {
        return userService.getUserByUsername(username).orElse(null);
    }
    
    @MutationMapping
    public User createUser(@Argument String username, @Argument String email,
                         @Argument String password, @Argument String role,
                         @Argument String status) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setRole(com.freight.contract.entity.Role.valueOf(role));
        user.setStatus(com.freight.contract.entity.UserStatus.valueOf(status));
        return userService.createUser(user);
    }
    
    @MutationMapping
    public User updateUser(@Argument Long id, @Argument String username,
                         @Argument String email, @Argument String password,
                         @Argument String role, @Argument String status) {
        User userDetails = new User();
        if (username != null) userDetails.setUsername(username);
        if (email != null) userDetails.setEmail(email);
        if (password != null) userDetails.setPassword(password);
        if (role != null) userDetails.setRole(com.freight.contract.entity.Role.valueOf(role));
        if (status != null) userDetails.setStatus(com.freight.contract.entity.UserStatus.valueOf(status));
        return userService.updateUser(id, userDetails);
    }
    
    @MutationMapping
    public Boolean deleteUser(@Argument Long id) {
        userService.deleteUser(id);
        return true;
    }
}