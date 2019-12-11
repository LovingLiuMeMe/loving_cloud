package cn.lovingliu.loving.controller;


import cn.lovingliu.loving.service.AuthCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author：LovingLiu
 * @Description: 验证码
 * @Date：Created in 2019-10-29
 */
@RestController
public class AuthCodeController {
    @Autowired
    private AuthCodeService authCodeService;

    @GetMapping("/getAuthCode")
    public ServerResponse getAuthCode(@RequestParam("userName") String userName){
        return authCodeService.generateAuthCode(userName);
    }

    @GetMapping("/verify")
    public ServerResponse verify(@RequestParam("userName") String userName,
                                 @RequestParam("authCode") String authCode){
        return authCodeService.verifyAuthCode(userName, authCode);
    }
}
