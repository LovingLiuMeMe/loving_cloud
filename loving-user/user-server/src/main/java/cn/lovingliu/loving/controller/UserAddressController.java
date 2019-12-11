package cn.lovingliu.loving.controller;


import cn.lovingliu.loving.common.ServerResponse;
import cn.lovingliu.loving.model.UserAddress;
import cn.lovingliu.loving.service.UserAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author：LovingLiu
 * @Description: 普通用户的
 * @Date：Created in 2019-11-17
 */
@RestController
@RequestMapping("/address")
public class UserAddressController {

    @Autowired
    private UserAddressService userAddressService;

    @GetMapping("/list")
    public ServerResponse list(@RequestParam("userId") Long userId){
        List<UserAddress> userAddressList = userAddressService.listByUserId(userId);
        return ServerResponse.createBySuccess(userAddressList);
    }

    @PostMapping("/save")
    public ServerResponse save(@RequestBody UserAddress userAddress){
        int count = userAddressService.save(userAddress);
        if(count > 0){
            return ServerResponse.createBySuccessMessage("操作成功");
        }else {
            return ServerResponse.createByErrorMessage("操作失败");
        }
    }
    @PostMapping("/delete/{addressId}")
    public ServerResponse delete(@PathVariable Long addressId){
        int count = userAddressService.deleteById(addressId);
        if(count > 0){
            return ServerResponse.createBySuccessMessage("删除成功");
        }else{
            return ServerResponse.createBySuccessMessage("删除失败");
        }
    }
}
