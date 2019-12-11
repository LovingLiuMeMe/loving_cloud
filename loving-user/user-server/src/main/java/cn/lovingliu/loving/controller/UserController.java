package cn.lovingliu.loving.controller;

import cn.lovingliu.loving.common.ServerResponse;
import cn.lovingliu.loving.config.RedisConfig;
import cn.lovingliu.loving.dto.UserDTO;
import cn.lovingliu.loving.enums.ExceptionCodeEnum;
import cn.lovingliu.loving.exception.UserException;
import cn.lovingliu.loving.model.User;
import cn.lovingliu.loving.service.RedisService;
import cn.lovingliu.loving.service.UserAddressService;
import cn.lovingliu.loving.service.UserService;
import cn.lovingliu.loving.util.CookieUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @Author：LovingLiu
 * @Description: 普通用户
 * @Date：Created in 2019-10-29
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private UserAddressService userAddressService;

    @Autowired
    private RedisConfig redisConfig;

    @PostMapping("/register")
    public ServerResponse register(@Valid @RequestBody UserDTO userDTO, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            String msg = bindingResult.getFieldError().getDefaultMessage();
            log.error("用户注册时报错:{}",msg);
            throw new UserException(ExceptionCodeEnum.PARAM_ERROR.getCode(),msg);
        }
        int count = userService.register(userDTO);
        if(count > 0){
            return ServerResponse.createBySuccessMessage("注册成功");
        }else {
            return ServerResponse.createBySuccessMessage("注册失败");
        }
    }

    @PostMapping("/login")
    public ServerResponse login(@RequestBody @Valid UserDTO userDTO, BindingResult bindingResult,
                                HttpServletResponse response){
        if(bindingResult.hasErrors()){
            String msg = bindingResult.getFieldError().getDefaultMessage();
            log.error("用户登录时报错:{}",msg);
            throw new UserException(ExceptionCodeEnum.PARAM_ERROR.getCode(),msg);
        }
        User user = userService.findByUserNameAndPassword(userDTO);
        if(user == null){
            throw new UserException(ExceptionCodeEnum.USER_NOT_EXIT);
        }
        // 1.设置redis
        String cvalue = UUID.randomUUID().toString();
        String rky = String.format(redisConfig.getLoginTokenPrefix() ,cvalue);
        String rvalue = UUID.randomUUID().toString();

        redisService.setValue(rky,rvalue);
        redisService.setExpire(rky,redisConfig.getLoginTokenExpire());

        // 2.设置cookie
        CookieUtil.set(response,redisConfig.getUserCookieKey(),cvalue,redisConfig.getLoginTokenExpire());
        return ServerResponse.createBySuccess(user);
    }

    @GetMapping("/logout")
    public ServerResponse logout(HttpServletRequest request,
                                 HttpServletResponse response,
                                 Map<String,Object> map){
        Cookie cookie = CookieUtil.get(request, redisConfig.getUserCookieKey());
        if(cookie == null){
            throw new UserException(ExceptionCodeEnum.LOGIN_TIME_OUT);
        }
        String cvalue = cookie.getValue();
        // 1.清除redis
        String rkey = String.format(redisConfig.getLoginTokenPrefix() ,cvalue);
        redisService.remove(rkey);

        // 2.清除cookie
        CookieUtil.set(response,redisConfig.getLoginTokenPrefix(),null,0);
        return ServerResponse.createBySuccessMessage("退出登录");
    }

    @PostMapping("/save")
    public ServerResponse save(@RequestBody User user){
        if(user.getUserId() == null){
            throw new UserException(ExceptionCodeEnum.PARAM_ERROR);
        }
        int count = userService.save(user);
        if(count > 0){
            return ServerResponse.createBySuccessMessage("更新成功");
        }else{
            return ServerResponse.createByErrorMessage("更新失败");
        }
    }

    @GetMapping("/list")
    public ServerResponse list() {
        List<User> userList = userService.list();
        return ServerResponse.createBySuccess("获取成功", userList);
    }

}
