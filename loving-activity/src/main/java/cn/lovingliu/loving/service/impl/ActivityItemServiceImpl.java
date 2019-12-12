package cn.lovingliu.loving.service.impl;


import cn.lovingliu.loving.dao.ActivityItemMapper;
import cn.lovingliu.loving.dao.ActivityMapper;
import cn.lovingliu.loving.dto.ActivityItemDTO;
import cn.lovingliu.loving.enums.ExceptionCodeEnum;
import cn.lovingliu.loving.exception.ActivityException;
import cn.lovingliu.loving.model.Activity;
import cn.lovingliu.loving.model.ActivityItem;
import cn.lovingliu.loving.product.client.ProductClient;
import cn.lovingliu.loving.product.common.GoodsInfoOutPut;
import cn.lovingliu.loving.service.ActivityItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author：LovingLiu
 * @Description:
 * @Date：Created in 2019-11-13
 */
@Service
@Slf4j
public class ActivityItemServiceImpl implements ActivityItemService {
    @Autowired
    private ActivityItemMapper activityItemMapper;

    @Autowired
    private ActivityMapper activityMapper;

    @Autowired
    private ProductClient productClient;

    public int save(List<ActivityItemDTO> activityItemDTOList){
        List<ActivityItem> activityItemList = new ArrayList<>();
        List<Long> goodsInfoIdList = activityItemDTOList.stream().map(e -> e.getGoodsId()).collect(Collectors.toList());
        List<GoodsInfoOutPut> goodsInfoList = productClient.ListByGoodsIdList(goodsInfoIdList);

        for (ActivityItemDTO activityItemDTO: activityItemDTOList) {
            Long activityId = activityItemDTO.getActivityId();
            // 1.活动是否存在
            Activity activity = activityMapper.selectByPrimaryKey(activityId);
            if(activity == null){
                throw new ActivityException(ExceptionCodeEnum.ACTIVITY_NOT_EXIT);
            }
            // 2.封装activityItem
            for (GoodsInfoOutPut goodsInfo: goodsInfoList) {
                /**
                 * 注意: 在JDK1.8 以后 Long类型判断相等 不能使用 ==
                 */
                if(activityItemDTO.getGoodsId().equals(goodsInfo.getGoodsId())){
                    ActivityItem activityItem = new ActivityItem();
                    BeanUtils.copyProperties(goodsInfo,activityItem);
                    activityItem.setActivityId(activityId);
                    activityItemList.add(activityItem);
                }
            }
        }
        log.warn("【数据处理之后的结果】{}",activityItemList);
        return activityItemMapper.insertSelectiveList(activityItemList);
    }

    @Override
    public List<ActivityItem> list() {
        return  activityItemMapper.selectAll();
    }

    @Override
    public int deleteByActivityItemId(Long activityItemId) {

        return activityItemMapper.deleteByPrimaryKey(activityItemId);
    }

    @Override
    public int deleteByActivityId(Long activityId) {
        return activityItemMapper.deleteByActivityId(activityId);
    }
}
