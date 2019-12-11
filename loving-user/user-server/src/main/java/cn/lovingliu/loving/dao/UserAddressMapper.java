package cn.lovingliu.loving.dao;


import cn.lovingliu.loving.model.UserAddress;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserAddressMapper {
    int deleteByPrimaryKey(Long addressId);

    int insert(UserAddress record);

    int insertSelective(UserAddress record);

    UserAddress selectByPrimaryKey(Long addressId);

    int updateByPrimaryKeySelective(UserAddress record);

    int updateByPrimaryKey(UserAddress record);

    List<UserAddress> findWithUserId(@Param("userId") Long userId);

    int updateIsDeletedByUserId(@Param("userId") Long userId, @Param("isDefault") Integer isDefault);

}