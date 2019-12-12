package cn.lovingliu.loving.dao;

import cn.lovingliu.loving.model.Welcome;

import java.util.List;

public interface WelcomeMapper {
    int deleteByPrimaryKey(Integer welcomeId);

    int insert(Welcome record);

    int insertSelective(Welcome record);

    Welcome selectByPrimaryKey(Integer welcomeId);

    int updateByPrimaryKeySelective(Welcome record);

    int updateByPrimaryKey(Welcome record);

    List<Welcome> list();
}