package cn.lovingliu.loving.controller;

import com.google.common.collect.Lists;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @Author：LovingLiu
 * @Description: HystrixController
 * @Date：Created in 2019-12-04
 */
@RestController
@DefaultProperties(defaultFallback = "defaultFallback")
@Slf4j
public class HystrixController {

    @HystrixCommand(fallbackMethod = "specialFallback")
    @GetMapping("/getGoodsInfoList")
    public String getGoodsInfoList(){
        RestTemplate restTemplate = new RestTemplate();
        /**
         * 注意: 服务内部不通过api-gateway 访问
         */
        return restTemplate.postForObject("http://127.0.0.1:3000/product/getByIdList" ,Lists.newArrayList(10003,10004) ,String.class);
    }

    @HystrixCommand(fallbackMethod = "specialFallback")
    @GetMapping("/getGoodsInfoListException")
    public String getGoodsInfoListException(){
        /**
         * 发生异常也可以触发服务降级
         */
        throw new RuntimeException("异常也可导致服务降级");
    }

    @GetMapping("/getProductInfoListTimeout")
    @HystrixCommand(commandProperties = { // 触发服务降级的 超时时间
         @HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds",value="3000") //设置超时时间
    })
    public String getProductInfoListTimeout(){
        /**
         * 触发服务降级的 服务调用时间默认为1s, 当超过这个时间, 也会触发服务降级
         */
        try{
            Thread.sleep(2000);
        }catch (Exception e){
            log.error(e.getMessage());
        }
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.postForObject("http://127.0.0.1:3000/product/getByIdList", Lists.newArrayList(10003,10004) ,String.class);
    }

    @HystrixCommand(commandProperties = {
            @HystrixProperty(name = "circuitBreaker.enabled",value = "true"),
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold",value = "10"),
            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds",value = "10000"),
            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage",value = "10")
    })
    @GetMapping("/getProductInfoListHystrix")
    public String getProductInfoListHystrix(@RequestParam("num") Integer num){
        if(num % 2 == 0){
            return "success";
        }else {
            RestTemplate restTemplate = new RestTemplate();
            return restTemplate.postForObject("http://127.0.0.1:3000/product/getByIdList", Lists.newArrayList(10003,10004) ,String.class);
        }
    }

    /**
     * 指定和默认的回调方法
     */
    private String specialFallback(){
        return "当前请求人数过多,请稍后再试";
    }

    private String defaultFallback(){
        return "处理所有未单独指定回调的 服务降级 方法";
    }
}
