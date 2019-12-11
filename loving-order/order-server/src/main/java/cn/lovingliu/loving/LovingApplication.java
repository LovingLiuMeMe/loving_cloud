package cn.lovingliu.loving;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(basePackages = "cn.lovingliu.loving.product.client")
//@EnableDiscoveryClient
//@SpringBootApplication
//@EnableCircuitBreaker
@EnableHystrixDashboard
@SpringCloudApplication
public class LovingApplication {

    public static void main(String[] args) {
        SpringApplication.run(LovingApplication.class, args);
    }

}
