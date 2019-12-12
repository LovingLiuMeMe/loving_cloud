package cn.lovingliu.loving.product.common;

import lombok.Data;

/**
 * @Author：LovingLiu
 * @Description: 减少库存的
 * @Date：Created in 2019-11-28
 */
@Data
public class DecreaseStockInput {
    private Long goodsId;
    private Integer goodsStock;
}
