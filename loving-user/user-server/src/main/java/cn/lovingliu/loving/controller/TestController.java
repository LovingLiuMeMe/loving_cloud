package cn.lovingliu.loving.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author：LovingLiu
 * @Description:
 * @Date：Created in 2019-11-28
 */
@RestController
@RequestMapping("/test")
@Slf4j
public class TestController {

    @Autowired
    private DiscoveryClient client; // 服务发现客户端

    @GetMapping("/hello")
    public String hello() {
        List<String> services = client.getServices();
        String result = "";
        for (String service: services) {
            result += "service-name: "+service + "\n";
        }
        return result;
    }
}
