package cn.lovingliu.loving.service.impl;

import cn.lovingliu.loving.dao.AdminUserMapper;
import cn.lovingliu.loving.dto.AdminUserDTO;
import cn.lovingliu.loving.model.AdminUser;
import cn.lovingliu.loving.service.AdminUserService;
import cn.lovingliu.loving.util.MD5Util;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author：LovingLiu
 * @Description: 管理端客户的请求
 * @Date：Created in 2019-11-21
 */
@Service
public class AdminUserServiceImpl implements AdminUserService {
    @Autowired
    private AdminUserMapper adminUserMapper;

    @Override
    public int register(AdminUserDTO adminUserDTO) {
        AdminUser adminUser = new AdminUser();
        BeanUtils.copyProperties(adminUserDTO, adminUser);
        String noMd5Password = adminUser.getLoginPassword();
        adminUser.setLoginPassword(MD5Util.MD5EncodeUtf8(noMd5Password));

        return adminUserMapper.insertSelective(adminUser);
    }

    @Override
    public AdminUser findByUserNameAndPassword(AdminUserDTO adminUserDTO) {
        String md5Password = MD5Util.MD5EncodeUtf8(adminUserDTO.getLoginPassword());
        return  adminUserMapper.selectByUserNameAndPassword(adminUserDTO.getLoginUserName(),md5Password);
    }

    @Override
    public int save(AdminUser adminUser) {
        return  adminUserMapper.updateByPrimaryKey(adminUser);
    }
}
