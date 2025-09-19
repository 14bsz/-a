package com.aftersales;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 售后单管理系统启动类
 */
@SpringBootApplication
public class AfterSalesApplication {

    public static void main(String[] args) {
        SpringApplication.run(AfterSalesApplication.class, args);
        System.out.println("售后单管理系统启动成功！");
        System.out.println("API文档地址: http://localhost:8080/api/after-sales");
    }
}