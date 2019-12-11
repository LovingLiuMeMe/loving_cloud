package cn.lovingliu.loving;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class LovingApplication {

    public static void main(String[] args) {
        SpringApplication.run(LovingApplication.class, args);
    }

}
