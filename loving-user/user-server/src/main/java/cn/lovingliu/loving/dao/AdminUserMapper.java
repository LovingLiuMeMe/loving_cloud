package cn.lovingliu.loving.dao;


import cn.lovingliu.loving.model.AdminUser;
import org.apache.ibatis.annotations.Param;

public interface AdminUserMapper {
    int deleteByPrimaryKey(Integer adminUserId);

    int insert(AdminUser record);

    int insertSelective(AdminUser record);

    AdminUser selectByPrimaryKey(Integer adminUserId);

    int updateByPrimaryKeySelective(AdminUser record);

    int updateByPrimaryKey(AdminUser record);

    AdminUser selectByUserNameAndPassword(@Param("loginUserName") String loginUserName, @Param("loginPassword") String loginPassword);
}