package cn.lovingliu.loving.handle;

import cn.lovingliu.loving.commmon.ServerResponse;
import cn.lovingliu.loving.exception.ProductException;
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
    @ExceptionHandler(value = ProductException.class)
    @ResponseBody
    public ServerResponse resolveException(Exception e){
        if(e instanceof ProductException){
            ProductException lovingMallException = (ProductException) e;
            return ServerResponse.createByErrorCodeMessage(lovingMallException.getCode(),lovingMallException.getMessage());
        }
        return ServerResponse.createByErrorMessage(e.getMessage());
    }
}
