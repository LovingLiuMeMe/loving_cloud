package cn.lovingliu.loving.service;


import cn.lovingliu.loving.model.Activity;

import java.util.List;

/**
 * @Author：LovingLiu
 * @Description:
 * @Date：Created in 2019-11-12
 */
public interface ActivityService {
    int save(Activity activity);
    List<Activity> listByDeleted(Byte isDeleted);
    int deleteByActivityId(Long activityId);
}
