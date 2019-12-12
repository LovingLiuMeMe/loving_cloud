package cn.lovingliu.loving.controller;

import cn.lovingliu.loving.model.GoodsInfo;
import cn.lovingliu.loving.product.common.DecreaseStockInput;
import cn.lovingliu.loving.product.common.GoodsInfoOutPut;
import cn.lovingliu.loving.service.GoodsCategoryService;
import cn.lovingliu.loving.service.GoodsInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author：LovingLiu
 * @Description:
 * @Date：Created in 2019-11-28
 */
@RestController
@RequestMapping("/client")
@Slf4j
public class ClientProductController {
    @Autowired
    private GoodsInfoService goodsInfoService;
    @Autowired
    private GoodsCategoryService goodsCategoryService;

    @PostMapping("/getByIdList")
    public List<GoodsInfoOutPut> ListByGoodsIdList(@RequestBody  List<Long> goodsInfoIdList) {
        List<GoodsInfo> goodsInfoList = goodsInfoService.ListByGoodsIdList(goodsInfoIdList);
        List<GoodsInfoOutPut>  goodsInfoOutPutList = goodsInfoList.stream().map(e -> {
            GoodsInfoOutPut goodsInfoOutPut = new GoodsInfoOutPut();
            goodsInfoOutPut.setGoodsId(e.getGoodsId());
            goodsInfoOutPut.setGoodsName(e.getGoodsName());
            goodsInfoOutPut.setGoodsCoverImg(e.getGoodsCoverImg());
            goodsInfoOutPut.setGoodsIntro(e.getGoodsIntro());
            goodsInfoOutPut.setSellingPrice(e.getSellingPrice());
            return goodsInfoOutPut;
        }).collect(Collectors.toList());

        return goodsInfoOutPutList;
    }

    @PostMapping("/decreaseStock")
    public Integer decreaseStock (@RequestBody List<DecreaseStockInput> decreaseStockInputList) {
        return goodsInfoService.decreaseStock(decreaseStockInputList);
    }
}
