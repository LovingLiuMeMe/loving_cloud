package cn.lovingliu.loving.controller;

import cn.lovingliu.loving.common.ServerResponse;
import cn.lovingliu.loving.dto.OrderDTO;
import cn.lovingliu.loving.model.Order;
import cn.lovingliu.loving.model.OrderItem;
import cn.lovingliu.loving.service.OrderItemService;
import cn.lovingliu.loving.service.OrderService;
import cn.lovingliu.loving.vo.OrderVO;
import com.google.common.collect.Lists;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author：LovingLiu
 * @Description:
 * @Date：Created in 2019-12-09
 */
@RestController
@RequestMapping("/user")
@DefaultProperties(defaultFallback = "defaultFallback")
@Slf4j
public class UserOrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderItemService orderItemService;

    /**
     * Feign下单 并实现熔断器
     * @param orderDTO
     * @return
     */
    @PostMapping("/save")
    @HystrixCommand(commandProperties = {
            @HystrixProperty(name = "circuitBreaker.enabled",value = "true"),
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold",value = "10"),
            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds",value = "10000"),
            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage",value = "10")
    })
    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.REPEATABLE_READ)
    public ServerResponse save(@RequestBody OrderDTO orderDTO){
        int count = orderService.save(orderDTO);
        return ServerResponse.createBySuccessMessage("订单创建成功");
    }

    /**
     * springCloud stream 异步下单
     * @param orderDTO
     * @return
     */
    @PostMapping("/saveAsync")
    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.REPEATABLE_READ)
    public ServerResponse saveAsync(@RequestBody OrderDTO orderDTO){
        orderService.saveAsync(orderDTO);
        return ServerResponse.createBySuccessMessage("订单创建成功");
    }

    /**
     * 查询指定员工的订单列表
     * @param userId
     * @return
     */
    @GetMapping("/list")
    public ServerResponse list(@RequestParam(value = "userId") Long userId){
        /**
         * 查询所以订单
         */
        List<Order> orderList = orderService.listWithDeletedStatus(0,0,"create_time","desc",userId, null);
        if(orderList.size() < 1){
            return ServerResponse.createByErrorMessage("暂没有订单");
        }
        List<Long> orderIdList = orderList.stream().map(e -> e.getOrderId()).collect(Collectors.toList());
        /**
         * 查看订单中的商品信息
         */
        List<OrderItem> orderItemList = orderItemService.listWithOrderIdList(orderIdList);
        List<OrderVO> orderVOList = Lists.newArrayList();
        for (Order order : orderList) {
            OrderVO orderVO = new OrderVO();
            BeanUtils.copyProperties(order, orderVO);

            List<OrderItem> tempOrderItemList = Lists.newArrayList();
            for(OrderItem orderItem : orderItemList) {
                if(order.getOrderId() == orderItem.getOrderId()){
                    tempOrderItemList.add(orderItem);
                }
            }
            orderVO.setOrderItemList(tempOrderItemList);
            orderVOList.add(orderVO);
        }

        return ServerResponse.createBySuccess("获取成功", orderVOList);
    }

    /**
     * 查看订单详情
     * @param orderId
     * @return
     */
    @GetMapping("/info")
    public ServerResponse<OrderVO> info(@RequestParam("orderId") Long orderId){
        OrderVO orderVO = new OrderVO();
        Order order = orderService.info(orderId);
        BeanUtils.copyProperties(order, orderVO);

        List<OrderItem> orderItemList = orderItemService.listWithOrderId(orderId);
        orderVO.setOrderItemList(orderItemList);
        return ServerResponse.createBySuccess("获取成功",orderVO);
    }

    /**
     * 删除订单
     * @param orderId
     * @return
     */
    @PostMapping("/delete")
    public ServerResponse delete(@RequestParam Long orderId){
        Long[] orderIds = new Long[] {orderId};
        int count = orderService.deleteByOrderIds(orderIds);
        if(count > 0){
            return ServerResponse.createBySuccessMessage("删除成功");
        }
        return ServerResponse.createByErrorMessage("删除失败");
    }
    /**
     * 默认服务降级处理
     * @return
     */
    private ServerResponse defaultFallback() {
        return ServerResponse.createByErrorMessage("当前系统繁忙,请稍后再试!");
    }
}
