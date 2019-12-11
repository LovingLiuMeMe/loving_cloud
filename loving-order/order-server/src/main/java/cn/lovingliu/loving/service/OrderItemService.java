package cn.lovingliu.loving.service;


import cn.lovingliu.loving.model.OrderItem;

import java.util.List;

/**
 * @Author：LovingLiu
 * @Description: 订单详情
 * @Date：Created in 2019-11-05
 */
public interface OrderItemService {
    List<OrderItem> listWithOrderId(Long orderId);
    List<OrderItem> listWithOrderIdList(List<Long> orderIdList);
    int saveList(List<OrderItem> orderItemList);
}
