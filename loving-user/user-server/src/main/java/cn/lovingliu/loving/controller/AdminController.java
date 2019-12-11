package cn.lovingliu.loving.controller;

import cn.lovingliu.loving.common.ServerResponse;
import cn.lovingliu.loving.config.RedisConfig;
import cn.lovingliu.loving.dto.AdminUserDTO;
import cn.lovingliu.loving.enums.ExceptionCodeEnum;
import cn.lovingliu.loving.exception.UserException;
import cn.lovingliu.loving.model.AdminUser;
import cn.lovingliu.loving.service.AdminUserService;
import cn.lovingliu.loving.service.RedisService;
import cn.lovingliu.loving.service.UserAddressService;
import cn.lovingliu.loving.util.CookieUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Map;
import java.util.UUID;

/**
 * @Author：LovingLiu
 * @Description:
 * @Date：Created in 2019-12-10
 */
@RestController
@RequestMapping("/admin")
@Slf4j
public class AdminController {

    @Autowired
    private RedisConfig redisConfig;

    @Autowired
    private AdminUserService adminUserService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private UserAddressService userAddressService;

    @PostMapping("/register")
    public ServerResponse register(@Valid @RequestBody AdminUserDTO adminUserDTO, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            String msg = bindingResult.getFieldError().getDefaultMessage();
            log.error("管理员用户创建时报错:{}",msg);
            throw new UserException(ExceptionCodeEnum.PARAM_ERROR.getCode(),msg);
        }
        int count = adminUserService.register(adminUserDTO);
        if (count > 0){
            return ServerResponse.createBySuccessMessage("创建成功");
        }else {
            return ServerResponse.createByErrorMessage("创建成功");
        }
    }

    @PostMapping("/login")
    public ServerResponse login(@RequestBody @Valid AdminUserDTO AdminUserDTO,BindingResult bindingResult,
                                HttpServletResponse response){
        if(bindingResult.hasErrors()){
            String msg = bindingResult.getFieldError().getDefaultMessage();
            log.error("用户登录时报错:{}",msg);
            throw new UserException(ExceptionCodeEnum.PARAM_ERROR.getCode(),msg);
        }
        AdminUser adminUser = adminUserService.findByUserNameAndPassword(AdminUserDTO);
        if(adminUser == null){
            throw new UserException(ExceptionCodeEnum.USER_NOT_EXIT);
        }
        // 1.设置redis
        String cvalue = UUID.randomUUID().toString();
        String rky = String.format(redisConfig.getLoginTokenPrefix(),cvalue);
        String rvalue = UUID.randomUUID().toString();

        redisService.setValue(rky,rvalue);
        redisService.setExpire(rky,redisConfig.getLoginTokenExpire());

        // 2.设置cookie
        CookieUtil.set(response,redisConfig.getAdminCookieKey(),cvalue,redisConfig.getLoginTokenExpire());
        adminUser.setLoginPassword("");
        return ServerResponse.createBySuccess("登录成功",adminUser);
    }

    @GetMapping("/logout")
    public ServerResponse logout(HttpServletRequest request,
                                 HttpServletResponse response,
                                 Map<String,Object> map){
        Cookie cookie = CookieUtil.get(request,redisConfig.getAdminCookieKey());
        if(cookie == null){
            throw new UserException(ExceptionCodeEnum.LOGIN_TIME_OUT);
        }
        String cvalue = cookie.getValue();
        // 1.清除redis
        String rkey = String.format(redisConfig.getLoginTokenPrefix(),cvalue);
        redisService.remove(rkey);

        // 2.清除cookie
        CookieUtil.set(response,redisConfig.getAdminCookieKey(),null,0);
        return ServerResponse.createBySuccessMessage("退出登录成功");
    }

    @PostMapping("/save")
    public ServerResponse save(@RequestBody AdminUser adminUser){
        if(adminUser.getAdminUserId() == null){
            throw new UserException(ExceptionCodeEnum.PARAM_ERROR);
        }
        int count = adminUserService.save(adminUser);
        if(count > 0){
            return ServerResponse.createBySuccessMessage("更新成功");
        }else{
            return ServerResponse.createByErrorMessage("更新失败");
        }
    }
}
