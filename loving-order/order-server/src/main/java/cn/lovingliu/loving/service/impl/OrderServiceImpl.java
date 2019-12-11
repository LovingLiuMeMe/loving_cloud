package cn.lovingliu.loving.service.impl;

import cn.lovingliu.loving.dao.OrderMapper;
import cn.lovingliu.loving.dto.OrderDTO;
import cn.lovingliu.loving.dto.OrderItemDTO;
import cn.lovingliu.loving.enums.CommonCodeEnum;
import cn.lovingliu.loving.enums.ExceptionCodeEnum;
import cn.lovingliu.loving.exception.OrderException;
import cn.lovingliu.loving.message.RabbitmqSender;
import cn.lovingliu.loving.model.Order;
import cn.lovingliu.loving.model.OrderItem;
import cn.lovingliu.loving.product.client.ProductClient;
import cn.lovingliu.loving.product.common.DecreaseStockInput;
import cn.lovingliu.loving.product.common.GoodsInfoOutPut;
import cn.lovingliu.loving.product.common.GsonUtil;
import cn.lovingliu.loving.service.OrderItemService;
import cn.lovingliu.loving.service.OrderService;
import cn.lovingliu.loving.util.BigDecimalUtil;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author：LovingLiu
 * @Description:
 * @Date：Created in 2019-11-05
 */
@Service
@Slf4j
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private ProductClient productClient;

    @Autowired
    private RabbitmqSender sender;

    @Override
    public List<Order> listWithDeletedStatus(int pageNum,int pageSize,String orderBy,String orderType,Long userId,Integer deletedStatus) {
        if(pageNum == 0 || pageSize == 0){
            return orderMapper.selectByIsDeleted(userId, deletedStatus);
        }else{
            PageHelper.startPage(pageNum, pageSize);
            PageHelper.orderBy(orderBy+" "+orderType);
            return orderMapper.selectByIsDeleted(userId, deletedStatus);
        }
    }

    @Override
    public Order info(Long orderId) {
        Order order = orderMapper.selectByPrimaryKey(orderId);
        if(order == null){
            throw new OrderException(ExceptionCodeEnum.ORDER_NOT_EXIT);
        }
        return order;
    }

    @Override
    public Integer save(OrderDTO orderDTO) {
        BigDecimal orderAllAmount = new BigDecimal("0");

        Order order = new Order();
        BeanUtils.copyProperties(orderDTO, order);
        // 1.生成订单流水号
        order.setOrderNo(UUID.randomUUID().toString());

        if(order.getOrderStatus() == 1){
            order.setPayTime(new Date());
        }
        List<OrderItem> orderItemList = orderDTO.getOrderItemList();

        List<Long> goodsInfoIdList = orderItemList.stream().map(e -> {
            return e.getGoodsId();
        }).collect(Collectors.toList());

        List<GoodsInfoOutPut> goodsInfoList = productClient.ListByGoodsIdList(goodsInfoIdList);


        // 2.计算订单总金额
        for (GoodsInfoOutPut goodsInfo: goodsInfoList) {
            for(OrderItem orderItem : orderItemList) {
                if(goodsInfo.getGoodsId().longValue() == orderItem.getGoodsId().longValue()){
                    orderAllAmount = BigDecimalUtil.add(
                            orderAllAmount.doubleValue(),
                            BigDecimalUtil.mul(
                                    goodsInfo.getSellingPrice().doubleValue(),
                                    orderItem.getGoodsCount().doubleValue()
                            ).doubleValue()
                    );
                }
            }
        }
        order.setTotalPrice(orderAllAmount.intValue());

        // 3.保存订单
        int count  = orderMapper.insertSelective(order);
        if(count < 1){
            throw new OrderException(ExceptionCodeEnum.ORDER_SAVE_FAIL);
        }
        Long orderId = order.getOrderId();

        if(orderId == null){
            throw new OrderException(ExceptionCodeEnum.ORDER_NOT_EXIT);
        }

        // 4.订单详情入库
        for (GoodsInfoOutPut goodsInfo: goodsInfoList) {
            for(OrderItem orderItem : orderItemList) {
                if(goodsInfo.getGoodsId().longValue() == orderItem.getGoodsId().longValue()){
                    BeanUtils.copyProperties(goodsInfo,orderItem);
                    orderItem.setOrderId(orderId);
                }
            }
        }

        // 5.保存订单详情
        count = orderItemService.saveList(orderItemList);
        if(count < 1){
            throw new OrderException(ExceptionCodeEnum.PARAM_ERROR);
        }

        // 6.扣库存
        List<DecreaseStockInput> decreaseStockInputList = orderDTO.getOrderItemList().stream().map(e -> {
            DecreaseStockInput decreaseStockInput = new DecreaseStockInput();
            decreaseStockInput.setGoodsId(e.getGoodsId());
            decreaseStockInput.setGoodsStock(e.getGoodsCount());
            return decreaseStockInput;
        }).collect(Collectors.toList());


        count = productClient.decreaseStock(decreaseStockInputList);
        if(count < 0){
            throw new OrderException(ExceptionCodeEnum.ORDER_SAVE_FAIL);
        }
        return count;
    }

    @Override
    public void saveAsync(OrderDTO orderDTO) {
        BigDecimal orderAllAmount = new BigDecimal("0");

        Order order = new Order();
        BeanUtils.copyProperties(orderDTO, order);
        // 1.生成订单流水号
        order.setOrderNo(UUID.randomUUID().toString());

        if(order.getOrderStatus() == 1){
            order.setPayTime(new Date());
        }
        List<OrderItem> orderItemList = orderDTO.getOrderItemList();

        List<Long> goodsInfoIdList = orderItemList.stream().map(e -> {
            return e.getGoodsId();
        }).collect(Collectors.toList());

        List<GoodsInfoOutPut> goodsInfoList = productClient.ListByGoodsIdList(goodsInfoIdList);


        // 2.计算订单总金额
        for (GoodsInfoOutPut goodsInfo: goodsInfoList) {
            for(OrderItem orderItem : orderItemList) {
                if(goodsInfo.getGoodsId().longValue() == orderItem.getGoodsId().longValue()){
                    orderAllAmount = BigDecimalUtil.add(
                            orderAllAmount.doubleValue(),
                            BigDecimalUtil.mul(
                                    goodsInfo.getSellingPrice().doubleValue(),
                                    orderItem.getGoodsCount().doubleValue()
                            ).doubleValue()
                    );
                }
            }
        }
        order.setTotalPrice(orderAllAmount.intValue());

        // 3.保存订单
        int count  = orderMapper.insertSelective(order);
        if(count < 1){
            throw new OrderException(ExceptionCodeEnum.ORDER_SAVE_FAIL);
        }
        Long orderId = order.getOrderId();

        if(orderId == null){
            throw new OrderException(ExceptionCodeEnum.ORDER_NOT_EXIT);
        }

        // 4.订单详情入库
        for (GoodsInfoOutPut goodsInfo: goodsInfoList) {
            for(OrderItem orderItem : orderItemList) {
                if(goodsInfo.getGoodsId().longValue() == orderItem.getGoodsId().longValue()){
                    BeanUtils.copyProperties(goodsInfo,orderItem);
                    orderItem.setOrderId(orderId);
                }
            }
        }

        // 5.保存订单详情
        count = orderItemService.saveList(orderItemList);
        if(count < 1){
            throw new OrderException(ExceptionCodeEnum.PARAM_ERROR);
        }

        // 6.扣库存
        List<DecreaseStockInput> decreaseStockInputList = orderDTO.getOrderItemList().stream().map(e -> {
            DecreaseStockInput decreaseStockInput = new DecreaseStockInput();
            decreaseStockInput.setGoodsId(e.getGoodsId());
            decreaseStockInput.setGoodsStock(e.getGoodsCount());
            return decreaseStockInput;
        }).collect(Collectors.toList());

        String json = GsonUtil.toJson(decreaseStockInputList);
        sender.sendMessage(json);
    }

    @Override
    public Integer update(Order order) {
        return orderMapper.updateByPrimaryKey(order);
    }

    @Override
    public Integer deleteByOrderIds(Long[] orderIds) {
        return orderMapper.updateOrderIsDeleted(orderIds);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.SERIALIZABLE)
    public void cancelOrderWithTimeOut() {
        Map<Long,Integer> orderItemDTOMap = new HashMap<>();
        List<OrderItemDTO> orderItemDTOList = new ArrayList<>();
        Set<Long> orderIdSet = new HashSet<>();
        /**
         * 1.获取当前时间之前的两小时时间
         */
        Date date = new Date();
        Calendar dar=Calendar.getInstance();
        dar.setTime(date);
        dar.add(Calendar.HOUR_OF_DAY, -2);
        date = dar.getTime();
        /**
         * 2.查询出所有的 超过两小时 未付款的订单
         */
        List<OrderItem> orderItemList = orderMapper.selectTimeOutOrderItem(date);
        if(orderItemList.size() > 0){
            /**
             * 3.数据封装
             */
            for (OrderItem orderItem : orderItemList) {
                // 3.1 订单ID
                orderIdSet.add(orderItem.getOrderId());
                // 3.2 key=goodsId value=数量
                Long goodsId = orderItem.getGoodsId();
                if(orderItemDTOMap.containsKey(goodsId)){
                    Integer tempAmount = orderItemDTOMap.get(goodsId);
                    orderItemDTOMap.put(goodsId,tempAmount + orderItem.getGoodsCount());
                }else {
                    orderItemDTOMap.put(goodsId,orderItem.getGoodsCount());
                }
            }
            orderItemDTOList = orderItemDTOMap.entrySet().stream().map(e -> new OrderItemDTO(e.getKey(),e.getValue())).collect(Collectors.toList());
            /**
             * 4.批量更新订单状态
             */
            int orderCount = orderMapper.updateOrderStatus(CommonCodeEnum.ORDER_STATUS_TIME_OUT.getCode(),orderIdSet);
            log.info("【定时关闭订单】数量:"+orderCount);
            /**
             * 5.批量归还库存
             * 注意:mysql不支持批操作 需要配置&allowMultiQueries=true 才可以
             */
//            log.info("【参数是】数量:"+orderItemDTOList);
//            int goodsCount = goodsInfoMapper.updateGoodsInfoStockNum(orderItemDTOList);
//            log.info("【定时修改商品库存】数量:"+goodsCount);
        }
    }
}
