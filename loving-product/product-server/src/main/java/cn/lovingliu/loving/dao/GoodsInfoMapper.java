package cn.lovingliu.loving.dao;


import cn.lovingliu.loving.dto.OrderItemDTO;
import cn.lovingliu.loving.model.GoodsInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GoodsInfoMapper {
    int deleteByPrimaryKey(Long goodsId);

    int insert(GoodsInfo record);

    int insertSelective(GoodsInfo record);

    GoodsInfo selectByPrimaryKey(Long goodsId);

    int updateByPrimaryKeySelective(GoodsInfo record);

    int updateByPrimaryKeyWithBLOBs(GoodsInfo record);

    int updateByPrimaryKey(GoodsInfo record);

    List<GoodsInfo> selectAllByKeywordAndSellStatus(@Param("keyword") String keyword, @Param("sellStatus") Integer sellStatus);
    List<GoodsInfo> selectByCategoryId(@Param("categoryId") String categoryId);
    int updateSellStatusByPrimaryKey(Long goodsId);
    int deleteByPrimaryKeyList(@Param("goodsInfoIdList") List<Long> goodsInfoIdList);
    int updateGoodsInfoStockNum(@Param("list") List<OrderItemDTO> orderItemDTOList);
    List<GoodsInfo> selectByPrimaryKeyList(@Param("goodsInfoIdList") List<Long> goodsInfoIdList);

}