package cn.lovingliu.loving.service;


import cn.lovingliu.loving.dto.AdminUserDTO;
import cn.lovingliu.loving.model.AdminUser;

/**
 * @Author：LovingLiu
 * @Description:
 * @Date：Created in 2019-11-21
 */
public interface AdminUserService {
    int register(AdminUserDTO adminUserDTO);
    AdminUser findByUserNameAndPassword(AdminUserDTO adminUserDTO);
    int save(AdminUser adminUser);
}
