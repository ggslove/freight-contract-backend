package com.freight.contract.config;

import com.freight.contract.entity.Role;
import com.freight.contract.entity.User;
import com.freight.contract.entity.UserStatus;
import com.freight.contract.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
@Profile("dev")
public class DataInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner initDatabase() {
        return args -> {
            System.out.println("密码编码器类型: " + passwordEncoder.getClass().getName());
            
            // 创建管理员用户
            if (!userRepository.existsByUsername("admin")) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setRealName("管理员");
                admin.setEmail("admin@example.com");
                String rawPassword = "admin123";
                String encodedPassword = passwordEncoder.encode(rawPassword);
                System.out.println("Admin原始密码: " + rawPassword);
                System.out.println("Admin编码密码: " + encodedPassword);
                System.out.println("Admin密码匹配测试: " + passwordEncoder.matches(rawPassword, encodedPassword));
                admin.setPassword(encodedPassword);
                admin.setRole(Role.ADMIN);
                admin.setStatus(UserStatus.ENABLED);
                userRepository.save(admin);
                System.out.println("管理员用户创建成功: admin/admin123");
            }

            // 创建测试经理用户
            if (!userRepository.existsByUsername("manager")) {
                User manager = new User();
                manager.setUsername("manager");
                manager.setRealName("业务经理");
                manager.setEmail("manager@example.com");
                String rawPassword = "manager123";
                String encodedPassword = passwordEncoder.encode(rawPassword);
                System.out.println("Manager原始密码: " + rawPassword);
                System.out.println("Manager编码密码: " + encodedPassword);
                manager.setPassword(encodedPassword);
                manager.setRole(Role.MANAGER);
                manager.setStatus(UserStatus.ENABLED);
                userRepository.save(manager);
                System.out.println("业务经理用户创建成功: manager/manager123");
            }

            // 创建财务用户
            if (!userRepository.existsByUsername("finance")) {
                User finance = new User();
                finance.setUsername("finance");
                finance.setRealName("财务人员");
                finance.setEmail("finance@example.com");
                String rawPassword = "finance123";
                String encodedPassword = passwordEncoder.encode(rawPassword);
                System.out.println("Finance原始密码: " + rawPassword);
                System.out.println("Finance编码密码: " + encodedPassword);
                finance.setPassword(encodedPassword);
                finance.setRole(Role.FINANCE);
                finance.setStatus(UserStatus.ENABLED);
                userRepository.save(finance);
                System.out.println("财务人员用户创建成功: finance/finance123");
            }
        };
    }
}