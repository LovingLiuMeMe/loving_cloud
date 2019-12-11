package cn.lovingliu.loving.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

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
