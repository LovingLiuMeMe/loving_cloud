package cn.lovingliu.lovingapigateway.component;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * @Author：LovingLiu
 * @Description:
 * @Date：Created in 2019-12-03
 */
@Data
@Component
@ConfigurationProperties("redis")
@RefreshScope
public class RedisConfig {
    private String authCodePrefix;
    private String loginTokenPrefix;
    private Integer authCodeExpire;
    private Integer loginTokenExpire;
    private Integer orderLock;
    private String userCookieKey;
    private String adminCookieKey;
}
