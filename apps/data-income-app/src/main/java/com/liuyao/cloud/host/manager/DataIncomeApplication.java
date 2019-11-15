package com.liuyao.cloud.host.manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class DataIncomeApplication {

    public static void main(String[] args) {
        SpringApplication.run(DataIncomeApplication.class, args);
    }
}
