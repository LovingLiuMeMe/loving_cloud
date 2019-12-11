package cn.lovingliu.loving.controller;

import cn.lovingliu.loving.common.CommonPage;
import cn.lovingliu.loving.common.ServerResponse;
import cn.lovingliu.loving.enums.CommonCodeEnum;
import cn.lovingliu.loving.model.Order;
import cn.lovingliu.loving.model.OrderItem;
import cn.lovingliu.loving.service.OrderItemService;
import cn.lovingliu.loving.service.OrderService;
import cn.lovingliu.loving.vo.OrderVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author：LovingLiu
 * @Description:
 * @Date：Created in 2019-12-09
 */
@RestController
@RequestMapping("/admin")
@Slf4j
public class AdminOrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderItemService orderItemService;

    /**
     * 查询所有订单列表
     * @param pageNum
     * @param pageSize
     * @param orderBy
     * @param orderType
     * @param userId
     * @return
     */
    @GetMapping("/list")
    public ServerResponse<CommonPage> list(
                                           @RequestParam(value = "pageNum",defaultValue = "1")
                                                   Integer pageNum,

                                           @RequestParam(value = "pageSize",defaultValue = "10")
                                                   Integer pageSize,

                                           @RequestParam(value = "orderBy",defaultValue = "create_time")
                                                   String orderBy,

                                           @RequestParam(value = "orderType",defaultValue = "desc")
                                                   String orderType,

                                           @RequestParam(value = "user_id",required = false)
                                                   Long userId){
        List<Order> orderList = orderService.listWithDeletedStatus(pageNum,pageSize,orderBy,orderType,userId,null);
        return ServerResponse.createBySuccess("获取成功", CommonPage.restPage(orderList));
    }

    /**
     * 获取订单详情
     * @param orderId
     * @return
     */
    @GetMapping("/info")
    public ServerResponse<OrderVO> info(
                                        @RequestParam("orderId")
                                                Long orderId){
        OrderVO orderVO = new OrderVO();
        Order order = orderService.info(orderId);
        BeanUtils.copyProperties(order, orderVO);

        List<OrderItem> orderItemList = orderItemService.listWithOrderId(orderId);
        orderVO.setOrderItemList(orderItemList);
        return ServerResponse.createBySuccess("获取成功",orderVO);
    }

    /**
     * 更新订单状态
     * @param orderId
     * @param orderStatus
     * @return
     */
    @PostMapping("/updateOrderStatus")
    public ServerResponse updateOrderStatus(@RequestParam("orderId") Long orderId,
                                            @RequestParam("orderStatus") Byte orderStatus){
        Order order = orderService.info(orderId);
        Byte oldOrderStatus = order.getOrderStatus();
        if(oldOrderStatus == CommonCodeEnum.ORDER_STATUS_NO_PAY.getCode()){
            return ServerResponse.createByErrorMessage("订单暂未支付! 为保护您的合法权益,暂无法改变订单状态!");
        }
        // 设置订单状态
        order.setOrderStatus(orderStatus);
        // 保存订单
        int count = orderService.update(order);
        if(count > 0){
            return ServerResponse.createBySuccess("更新成功");
        }
        return ServerResponse.createByErrorMessage("更新失败");
    }

    /**
     * 删除订单
     * @param orderId
     * @return
     */
    @PostMapping("/delete")
    public ServerResponse delete(@RequestParam("orderId") Long orderId){
        Long[] orderIds = new Long[] {orderId};
        int count = orderService.deleteByOrderIds(orderIds);
        if(count > 0){
            return ServerResponse.createBySuccessMessage("删除成功");
        }
        return ServerResponse.createByErrorMessage("删除失败");
    }

    private ServerResponse defaultFallback() {
        return ServerResponse.createByErrorMessage("当前系统繁忙,请稍后再试!");
    }
}
