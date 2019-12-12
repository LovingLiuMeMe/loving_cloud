package cn.lovingliu.loving.handle;


import cn.lovingliu.loving.common.ServerResponse;
import cn.lovingliu.loving.exception.ActivityException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author：LovingLiu
 * @Description: 统一异常处理（适用于微服务架构）
 * @Date：Created in 2019-10-30
 */
@ControllerAdvice
public class ExceptionHandle {
    @ExceptionHandler(value = ActivityException.class)
    @ResponseBody
    public ServerResponse resolveException(Exception e){
        if(e instanceof ActivityException){
            ActivityException lovingMallException = (ActivityException) e;
            return ServerResponse.createByErrorCodeMessage(lovingMallException.getCode(),lovingMallException.getMessage());
        }
        return ServerResponse.createByErrorMessage(e.getMessage());
    }
}
