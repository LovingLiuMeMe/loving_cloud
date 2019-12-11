package cn.lovingliu.lovingapigateway.filter;

import com.google.common.util.concurrent.RateLimiter;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

/**
 * @Author：LovingLiu
 * @Description: 限流过滤器（令牌桶的形式）
 * @Date：Created in 2019-12-03
 */
@Component
public class RateLimitFilter extends ZuulFilter {
    /**
     * @Desc 创建令牌桶 guava已实现(1秒投放100个令牌)
     * @Author LovingLiu
     */
    private static final RateLimiter RATE_LIMITER = RateLimiter.create(100);

    @Override
    public String filterType() {
        /**
         * 类型是一个前置过滤器
         */
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        /**
         * 优先级应该高于 所有框架自带的Filter
         */
        return FilterConstants.SERVLET_DETECTION_FILTER_ORDER - 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        if (!RATE_LIMITER.tryAcquire(1)){ // 取令牌
            // 未取到
            throw new RuntimeException("当前系统繁忙，请稍后再试");
        }
        return null;
    }
}
