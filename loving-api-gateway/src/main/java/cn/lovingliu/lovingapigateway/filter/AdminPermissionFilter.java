package cn.lovingliu.lovingapigateway.filter;

import cn.lovingliu.lovingapigateway.component.RedisConfig;
import cn.lovingliu.lovingapigateway.enums.ExceptionCodeEnum;
import cn.lovingliu.lovingapigateway.util.CookieUtil;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * @Author：LovingLiu
 * @Description:
 * @Date：Created in 2019-12-03
 */
@Slf4j
@Component
public class AdminPermissionFilter extends ZuulFilter {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedisConfig redisConfig;

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return FilterConstants.PRE_DECORATION_FILTER_ORDER - 1;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        if(request.getRequestURI().indexOf("/mallOrder/order/admin") != -1){
            return true;
        }
        return false;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        // 1.获取cookie
        Cookie cookie = CookieUtil.get(request, redisConfig.getAdminCookieKey());
        if(cookie == null){
            log.error("【管理员登陆校验】Cookie中查不到token");
            throw new RuntimeException(ExceptionCodeEnum.AUTHORIZE_FAIL.getMsg());
        }
        log.info("【管理员登陆校验】");
        // 2.redis查询
        String tokenValue = stringRedisTemplate.opsForValue().get(String.format(redisConfig.getLoginTokenPrefix(), cookie.getValue()));
        if(StringUtils.isBlank(tokenValue)){
            log.error("【登陆校验】Redis中查不到token");
            throw new RuntimeException(ExceptionCodeEnum.AUTHORIZE_FAIL.getMsg());
        }
        return null;
    }
}
