package cn.lovingliu.loving.service;


import cn.lovingliu.loving.dto.ActivityItemDTO;
import cn.lovingliu.loving.model.ActivityItem;

import java.util.List;

/**
 * @Author：LovingLiu
 * @Description:
 * @Date：Created in 2019-11-13
 */
public interface ActivityItemService {
    int save(List<ActivityItemDTO> activityItemDTOList);
    List<ActivityItem> list();
    int deleteByActivityItemId(Long activityItemId);
    int deleteByActivityId(Long activityId);
}
