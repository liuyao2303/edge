package com.liuyao.cloud.cloudregistercenter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class CloudRegisterCenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudRegisterCenterApplication.class, args);
    }

}
