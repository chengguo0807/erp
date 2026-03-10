package com.enterprisehub.system;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * 系统管理服务启动类
 */
@SpringBootApplication
@ComponentScan("com.enterprisehub")
@MapperScan("com.enterprisehub.system.mapper")
public class SystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(SystemApplication.class, args);
        System.out.println("=== EnterpriseHub ERP 系统管理服务启动成功 ===");
    }
}
