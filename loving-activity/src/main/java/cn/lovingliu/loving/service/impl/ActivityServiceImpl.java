package cn.lovingliu.loving.service.impl;


import cn.lovingliu.loving.dao.ActivityMapper;
import cn.lovingliu.loving.model.Activity;
import cn.lovingliu.loving.service.ActivityItemService;
import cn.lovingliu.loving.service.ActivityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author：LovingLiu
 * @Description:
 * @Date：Created in 2019-11-12
 */
@Service
@Slf4j
public class ActivityServiceImpl implements ActivityService {

    @Autowired
    private ActivityMapper activityMapper;
    @Autowired
    private ActivityItemService activityItemService;

    @Override
    public int save(Activity activity) {
        return activityMapper.insertSelective(activity);
    }

    @Override
    public List<Activity> listByDeleted(Byte isDeleted) {
        return activityMapper.selectByIsDeleted(isDeleted);
    }

    @Override
    public int deleteByActivityId(Long activityId) {
        int count = activityItemService.deleteByActivityId(activityId);
        log.info("删除活动: {} 下的 {} 件商品",activityId,count);
        return activityMapper.deleteByPrimaryKey(activityId);
    }
}
