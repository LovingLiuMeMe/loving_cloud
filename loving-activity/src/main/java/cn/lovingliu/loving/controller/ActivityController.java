package cn.lovingliu.loving.controller;


import cn.lovingliu.loving.common.ServerResponse;
import cn.lovingliu.loving.dto.ActivityItemDTO;
import cn.lovingliu.loving.enums.ExceptionCodeEnum;
import cn.lovingliu.loving.model.Activity;
import cn.lovingliu.loving.model.ActivityItem;
import cn.lovingliu.loving.service.ActivityItemService;
import cn.lovingliu.loving.service.ActivityService;
import cn.lovingliu.loving.vo.ActivityVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author：LovingLiu
 * @Description:
 * @Date：Created in 2019-11-12
 */
@RestController
public class ActivityController {
    @Autowired
    private ActivityService activityService;
    @Autowired
    private ActivityItemService activityItemService;

    /**
     * 创建活动
     * @param activity
     * @return
     */
    @PostMapping("/saveActivity")
    public ServerResponse saveActivity(@RequestBody Activity activity){
        if(StringUtils.isBlank(activity.getActivityName())){
            ServerResponse.createByErrorMessage(ExceptionCodeEnum.PARAM_ERROR.getMsg());
        }
        int count = activityService.save(activity);
        if(count > 0){
            return ServerResponse.createBySuccessMessage("创建成功");
        }
        return  ServerResponse.createByErrorMessage("创建失败");
    }

    /**
     * 添加活动商品
     * @param activityItemDTOList
     * @param result
     * @return
     */
    @PostMapping("/addActivityItems")
    public ServerResponse addActivityItem(@Valid @RequestBody List<ActivityItemDTO> activityItemDTOList,
                                          BindingResult result){
        if(result.hasErrors()){
            return ServerResponse.createByErrorMessage(result.getFieldError().getDefaultMessage());
        }
        int count = activityItemService.save(activityItemDTOList);
        if(count > 0){
            return ServerResponse.createBySuccessMessage("添加成功");
        }
        return  ServerResponse.createByErrorMessage("添加失败");
    }

    /**
     * 删除活动商品
     * @param activityItemId
     * @return
     */
    @PostMapping("/deleteActivityItem")
    public ServerResponse deleteActivityItem(@RequestParam("activityItemId") Long activityItemId){
        int count = activityItemService.deleteByActivityItemId(activityItemId);
        if(count > 0){
            return ServerResponse.createBySuccessMessage("删除成功");
        }
        return  ServerResponse.createByErrorMessage("删除失败");
    }

    /**
     * 删除活动
     * @param activityId
     * @return
     */
    @PostMapping("/deleteActivity")
    public ServerResponse deleteActivity(@RequestParam("activityId") Long activityId){
        int count = activityService.deleteByActivityId(activityId);
        if(count > 0){
            return ServerResponse.createBySuccessMessage("删除成功");
        }
        return  ServerResponse.createByErrorMessage("删除失败");
    }

    /**
     * 获得活动列表
     * @return
     */
    @GetMapping("/activityList")
    public ServerResponse activityList(){
        /**
         * 1.查询所有的首页活动
        */
        List<Activity> activityList = activityService.listByDeleted(null);

        /**
         * 2.查询所有的活动商品
         */
        List<ActivityItem> activityItemList = activityItemService.list();

        /**
         * 数据封装
         */

        List<ActivityVO> activityVOList = activityList.stream().map( e -> {
            ActivityVO activityVO = new ActivityVO();
            BeanUtils.copyProperties(e, activityVO);
            return activityVO;
        }).collect(Collectors.toList());

        for (ActivityVO activityVO : activityVOList) {
            Long activityVOId = activityVO.getActivityId();
            List<ActivityItem> activityItemListTemp = new ArrayList<>();

            for(ActivityItem activityItem : activityItemList) {
                Long  activityVOId2= activityItem.getActivityId();
                if(activityVOId == activityVOId2){
                    activityItemListTemp.add(activityItem);
                }
            }
            activityVO.setActivityItemList(activityItemListTemp);
        }

        return ServerResponse.createBySuccess("获得成功",activityVOList);
    }

}
