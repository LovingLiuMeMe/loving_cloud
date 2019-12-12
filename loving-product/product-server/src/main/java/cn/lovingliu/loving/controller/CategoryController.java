package cn.lovingliu.loving.controller;

import cn.lovingliu.loving.commmon.ServerResponse;
import cn.lovingliu.loving.enums.CommonCodeEnum;
import cn.lovingliu.loving.model.GoodsCategory;
import cn.lovingliu.loving.service.GoodsCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author：LovingLiu
 * @Description:
 * @Date：Created in 2019-12-10
 */
@RestController
@RequestMapping("/category")
@Slf4j
public class CategoryController {
    @Autowired
    private GoodsCategoryService categoryService;

    @GetMapping("/list")
    public ServerResponse<List<GoodsCategory>> list(){
        List<GoodsCategory> list = categoryService.findAllByDeletedStatus(CommonCodeEnum.PRODUCT_CATEGORY_NO_DELETED.getCode());
        return ServerResponse.createBySuccess(list);
    }
}
