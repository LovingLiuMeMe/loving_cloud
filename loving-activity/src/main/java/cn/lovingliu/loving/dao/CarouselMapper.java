package cn.lovingliu.loving.dao;

import cn.lovingliu.loving.model.Carousel;

import java.util.List;

public interface CarouselMapper {
    int deleteByPrimaryKey(Integer carouselId);

    int insert(Carousel record);

    int insertSelective(Carousel record);

    Carousel selectByPrimaryKey(Integer carouselId);

    int updateByPrimaryKeySelective(Carousel record);

    int updateByPrimaryKey(Carousel record);
    List<Carousel> selectByIsDeleted(Integer isDeleted);
}