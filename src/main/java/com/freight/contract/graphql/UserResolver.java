package com.freight.contract.graphql;

import com.freight.contract.entity.User;
import com.freight.contract.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller

public class UserResolver {
    @Autowired
    private UserService userService;

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
    public User createUser(@Argument String username,
                           @Argument String email,
                           @Argument String realName,
                           @Argument String phone,
                           @Argument String password,
                           @Argument String role,
                           @Argument String status) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setRealName(realName);
        user.setPhone(phone);
        user.setRole(com.freight.contract.entity.Role.valueOf(role));
        user.setStatus(com.freight.contract.entity.UserStatus.valueOf(status));
        return userService.createUser(user, password);
    }

    @MutationMapping
    public User updateUser(@Argument Long id,
                           @Argument String username,
                           @Argument String realName,
                           @Argument String email,
                           @Argument String phone,
                           @Argument String status
    ) {
        User user = new User();

        if (username != null) {
            user.setUsername(username);
        }
        if (realName != null) {
            user.setRealName(realName);
        }
        if (email != null) {
            user.setEmail(email);
        }
        if (phone != null) {
            user.setPhone(phone);
        }
        if (status != null) {
            user.setStatus(com.freight.contract.entity.UserStatus.valueOf(status));
        }
//        if (role != null) user.setRole(com.freight.contract.entity.Role.valueOf(role));
//        if (status != null) user.setStatus(com.freight.contract.entity.UserStatus.valueOf(status));
        return userService.updateUser(id, user).orElse(null);
    }

    @MutationMapping
    public Boolean deleteUser(@Argument Long id) {
        userService.deleteUser(id);
        return true;
    }

    @MutationMapping
    public User updateUserStatus(@Argument Long id, @Argument String status) {
        User user = userService.getUserById(id).orElse(null);
        if (user == null) {
            return null;
        }
        user.setStatus(com.freight.contract.entity.UserStatus.valueOf(status));
        return userService.updateUser(id, user).orElse(null);

    }
}