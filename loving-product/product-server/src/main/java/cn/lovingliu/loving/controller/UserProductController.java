package cn.lovingliu.loving.controller;

import cn.lovingliu.loving.commmon.CommonPage;
import cn.lovingliu.loving.commmon.ServerResponse;
import cn.lovingliu.loving.enums.CommonCodeEnum;
import cn.lovingliu.loving.enums.ExceptionCodeEnum;
import cn.lovingliu.loving.exception.ProductException;
import cn.lovingliu.loving.model.GoodsCategory;
import cn.lovingliu.loving.model.GoodsInfo;
import cn.lovingliu.loving.service.GoodsCategoryService;
import cn.lovingliu.loving.service.GoodsInfoService;
import cn.lovingliu.loving.vo.GoodsCategoryVO;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author：LovingLiu
 * @Description:
 * @Date：Created in 2019-12-10
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserProductController {
    @Autowired
    private GoodsInfoService goodsInfoService;
    @Autowired
    private GoodsCategoryService goodsCategoryService;

    @GetMapping("/list")
    public ServerResponse<CommonPage<GoodsInfo>> list(
            @RequestParam(value = "pageNum",defaultValue = "1")
                    Integer pageNum,
            @RequestParam(value = "pageSize",defaultValue = "10")
                    Integer pageSize,
            @RequestParam(value = "orderBy",defaultValue = "create_time")
                    String orderBy,
            @RequestParam(value = "orderType",defaultValue = "desc")
                    String orderType,
            @RequestParam(value = "search",required = false)
                    String keyword){
        List<GoodsInfo> goodsInfoList = goodsInfoService.listByUser(pageNum,pageSize,orderBy,orderType,keyword);
        return ServerResponse.createBySuccess(CommonPage.restPage(goodsInfoList));
    }

    @GetMapping("/category")
    public ServerResponse<List<GoodsCategoryVO>> category(){
        List<GoodsInfo> goodsInfoList = goodsInfoService.listByGoodsSellStatus(CommonCodeEnum.PRODUCT_STATUS_ON_SELL.getCode());
        List<Long> categoryIdList = goodsInfoList.stream().map(e -> e.getGoodsCategoryId()).collect(Collectors.toList());

        List<GoodsCategory> goodsCategoryList = goodsCategoryService.findInIdListAndDeletedStatus(categoryIdList,CommonCodeEnum.PRODUCT_CATEGORY_NO_DELETED.getCode());

        List<GoodsCategoryVO> goodsCategoryVOList = Lists.newArrayList();

        for (GoodsCategory goodsCategory: goodsCategoryList) {
            Long categoryId = goodsCategory.getCategoryId();
            List<GoodsInfo> categoryGoodsInfoList = new ArrayList<>();
            for(GoodsInfo goodsInfo : goodsInfoList){
                if(categoryId == goodsInfo.getGoodsCategoryId()){
                    categoryGoodsInfoList.add(goodsInfo);
                }
            }
            GoodsCategoryVO goodsCategoryVO = new GoodsCategoryVO();
            BeanUtils.copyProperties(goodsCategory, goodsCategoryVO);
            goodsCategoryVO.setGoodsInfoList(categoryGoodsInfoList);

            goodsCategoryVOList.add(goodsCategoryVO);
        }

        return ServerResponse.createBySuccess(goodsCategoryVOList);
    }

    @GetMapping("/search/{goodsId}")
    public ServerResponse search(@PathVariable("goodsId") Long goodsId){
        GoodsInfo goodsInfo = goodsInfoService.findByGoodsId(goodsId);
        if(goodsInfo == null){
            throw new ProductException(ExceptionCodeEnum.PRODUCT_NOT_EXIT);
        }
        return ServerResponse.createBySuccess(goodsInfo);
    }

}
