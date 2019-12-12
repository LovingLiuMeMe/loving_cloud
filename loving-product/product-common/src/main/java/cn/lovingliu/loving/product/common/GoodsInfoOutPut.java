package cn.lovingliu.loving.product.common;

import lombok.Data;

/**
 * @Author：LovingLiu
 * @Description: 对外提供的商品公共类
 * @Date：Created in 2019-11-28
 */
@Data
public class GoodsInfoOutPut {

    /**
     * 关联商品id
     *
     * @mbg.generated
     */
    private Long goodsId;

    /**
     * 下单时商品的名称(订单快照)
     *
     * @mbg.generated
     */
    private String goodsName;

    /**
     * 下单时商品的主图(订单快照)
     *
     * @mbg.generated
     */
    private String goodsCoverImg;

    /**
     * 下单时商品的价格(订单快照)
     *
     * @mbg.generated
     */
    private Integer sellingPrice;


    /**
     * 商品描述
     *
     * @mbg.generated
     */
    private String goodsIntro;
}
