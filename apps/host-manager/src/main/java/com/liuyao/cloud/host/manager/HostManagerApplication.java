package com.liuyao.cloud.host.manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class HostManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(HostManagerApplication.class, args);
    }
}
