package cn.lovingliu.loving.service;


import cn.lovingliu.loving.dto.OrderDTO;
import cn.lovingliu.loving.model.Order;

import java.util.List;

/**
 * @Author：LovingLiu
 * @Description:
 * @Date：Created in 2019-11-05
 */
public interface OrderService {
    List<Order> listWithDeletedStatus(int pageNum, int pageSize, String orderBy, String orderType, Long userId, Integer deletedStatus);
    Order info(Long orderId);
    Integer save(OrderDTO orderDTO);
    void saveAsync(OrderDTO orderDTO);
    Integer update(Order order);
    Integer deleteByOrderIds(Long[] orderIds);
    void cancelOrderWithTimeOut();
}
