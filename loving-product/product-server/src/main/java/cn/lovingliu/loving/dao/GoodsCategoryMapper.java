package cn.lovingliu.loving.dao;


import cn.lovingliu.loving.model.GoodsCategory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GoodsCategoryMapper {
    int deleteByPrimaryKey(Long categoryId);

    int insert(GoodsCategory record);

    int insertSelective(GoodsCategory record);

    GoodsCategory selectByPrimaryKey(Long categoryId);

    int updateByPrimaryKeySelective(GoodsCategory record);

    int updateByPrimaryKey(GoodsCategory record);

    List<GoodsCategory> selectInIdsAndIsDeleted(@Param("categoryIdList") List<Long> categoryIdList, @Param("deletedStatus") Integer deletedStatus);

    List<GoodsCategory> selectByDeletedStatus(Integer deletedStatus);
}