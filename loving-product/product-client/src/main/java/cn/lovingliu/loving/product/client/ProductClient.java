package cn.lovingliu.loving.product.client;

import cn.lovingliu.loving.product.common.DecreaseStockInput;
import cn.lovingliu.loving.product.common.GoodsInfoOutPut;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @Author：LovingLiu
 * @Description:
 * @Date：Created in 2019-11-28
 */
@FeignClient(name = "product", path = "/client")
public interface ProductClient {
    @PostMapping("/getByIdList")
    List<GoodsInfoOutPut> ListByGoodsIdList(@RequestBody  List<Long> goodsInfoIdList);

    @PostMapping("/decreaseStock")
    Integer decreaseStock (@RequestBody List<DecreaseStockInput> decreaseStockInputList);
}
