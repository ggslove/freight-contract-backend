package com.freight.contract.config;

import com.freight.contract.entity.*;
import com.freight.contract.repository.CurrencyRepository;
import com.freight.contract.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;

@Configuration
@RequiredArgsConstructor
@Profile("dev")
public class DataInitializer {

    private final UserRepository userRepository;
    private final CurrencyRepository currencyRepository;
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

            // 初始化币种数据
            initCurrencies();
        };
    }

    private void initCurrencies() {
        // 人民币
        if (!currencyRepository.existsByCode("CNY")) {
            Currency cny = new Currency();
            cny.setCode("CNY");
            cny.setName("人民币");
            cny.setSymbol("¥");
            cny.setExchangeRate(BigDecimal.ONE);
            cny.setIsActive(true);
            currencyRepository.save(cny);
            System.out.println("初始化币种: CNY - 人民币");
        }

        // 美元
        if (!currencyRepository.existsByCode("USD")) {
            Currency usd = new Currency();
            usd.setCode("USD");
            usd.setName("美元");
            usd.setSymbol("$");
            usd.setExchangeRate(new BigDecimal("7.2"));
            usd.setIsActive(true);
            currencyRepository.save(usd);
            System.out.println("初始化币种: USD - 美元");
        }


        // 印尼盾
        if (!currencyRepository.existsByCode("IDR")) {
            Currency idr = new Currency();
            idr.setCode("IDR");
            idr.setName("印尼盾");
            idr.setSymbol("Rp");
            idr.setExchangeRate(new BigDecimal("0.0046")); // 示例汇率，需要根据实际情况调整
            idr.setIsActive(true);
            currencyRepository.save(idr);
            System.out.println("初始化币种: IDR - 印尼盾");
        }

    }
}