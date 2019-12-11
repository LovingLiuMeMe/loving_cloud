package cn.lovingliu.loving.service.impl;

import cn.lovingliu.loving.dao.OrderItemMapper;
import cn.lovingliu.loving.enums.ExceptionCodeEnum;
import cn.lovingliu.loving.exception.OrderException;
import cn.lovingliu.loving.model.OrderItem;
import cn.lovingliu.loving.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author：LovingLiu
 * @Description: 订单详情
 * @Date：Created in 2019-11-05
 */
@Service
public class OrderItemServiceImpl implements OrderItemService {
    @Autowired
    private OrderItemMapper orderItemMapper;

    @Override
    public List<OrderItem> listWithOrderId(Long orderId) {
        if(orderId == null){
            throw new OrderException(ExceptionCodeEnum.PARAM_ERROR);
        }
        return orderItemMapper.selectByOrderId(orderId);
    }

    @Override
    public List<OrderItem> listWithOrderIdList(List<Long> orderIdList) {
        return orderItemMapper.selectByOrderIdList(orderIdList);
    }

    @Override
    public int saveList(List<OrderItem> orderItemList) {
        return orderItemMapper.insertSelectiveList(orderItemList);
    }
}
