package cn.lovingliu.loving.dao;


import cn.lovingliu.loving.model.Order;
import cn.lovingliu.loving.model.OrderItem;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Set;

public interface OrderMapper {
    int deleteByPrimaryKey(Long orderId);

    int insert(Order record);

    int insertSelective(Order record);

    Order selectByPrimaryKey(Long orderId);

    int updateByPrimaryKeySelective(Order record);

    int updateByPrimaryKey(Order record);

    List<Order> selectByIsDeleted(@Param("userId") Long userId, @Param("isDeleted") Integer isDeleted);

    int updateOrderIsDeleted(@Param("orderIds") Long[] orderIds);

    List<OrderItem> selectTimeOutOrderItem(@Param("date") Date date);

    int updateOrderStatus(@Param("orderStatus") Integer orderStatus, @Param("orderIdSet") Set<Long> orderIdSet);
}