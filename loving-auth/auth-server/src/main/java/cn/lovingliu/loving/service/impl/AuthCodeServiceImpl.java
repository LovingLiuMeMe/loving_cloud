package cn.lovingliu.loving.service.impl;

import cn.lovingliu.loving.config.RedisConfig;
import cn.lovingliu.loving.controller.ServerResponse;
import cn.lovingliu.loving.enums.ExceptionCodeEnum;
import cn.lovingliu.loving.exception.AuthException;
import cn.lovingliu.loving.service.AuthCodeService;
import cn.lovingliu.loving.service.EmailService;
import cn.lovingliu.loving.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * @Author：LovingLiu
 * @Description:
 * @Date：Created in 2019-10-30
 */

@Service
@Slf4j
public class AuthCodeServiceImpl implements AuthCodeService {
    @Autowired
    private RedisConfig redisConfig;
    @Autowired
    private RedisService redisService;

    @Autowired
    private EmailService emailService;

    @Override
    public ServerResponse generateAuthCode(String userName) {
        String redisKey = String.format(redisConfig.getAuthCodePrefix(),userName);
        if(StringUtils.isBlank(redisService.getValue(redisKey))){
            StringBuilder sb = new StringBuilder();
            Random random = new Random();
            for(int i = 0; i<6 ;i++){
                sb.append(random.nextInt(10));
            }
            redisService.setValue(redisKey,sb.toString());
            redisService.setExpire(redisKey,redisConfig.getAuthCodeExpire());
            // 发送邮件
            emailService.sendSimpleMail(userName,"LovingMall注册",sb.toString());
            return ServerResponse.createBySuccessMessage("验证码发送成功,请前往邮箱查看");
        }else{
            throw new AuthException(ExceptionCodeEnum.AUTH_TIMES_LIMIT);
        }
    }

    @Override
    public ServerResponse verifyAuthCode(String userName, String authCode) {
        String redisKey = String.format(redisConfig.getAuthCodePrefix(),userName);
        String realAuthCode = redisService.getValue(redisKey);
        if(StringUtils.isBlank(realAuthCode)){
            log.error("验证码超时");
            throw new AuthException(ExceptionCodeEnum.AUTH_CODE_TIME_OUT);
        }
        if(realAuthCode.equals(authCode)){
            log.info("username:{} 验证成功",userName);
            return ServerResponse.createBySuccessMessage("验证成功");
        }
        return ServerResponse.createBySuccessMessage("验证成功");
    }
}
