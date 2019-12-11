package cn.lovingliu.loving.service;


import cn.lovingliu.loving.model.UserAddress;

import java.util.List;

/**
 * @Author：LovingLiu
 * @Description:
 * @Date：Created in 2019-11-17
 */
public interface UserAddressService {
    List<UserAddress> listByUserId(Long userId);
    Integer save(UserAddress userAddress);
    Integer deleteById(Long addressId);
    int addNewUserAddress(UserAddress userAddress);
}
