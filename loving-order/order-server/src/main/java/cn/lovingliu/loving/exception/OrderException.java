package cn.lovingliu.loving.exception;

import cn.lovingliu.loving.enums.ExceptionCodeEnum;
import lombok.Data;

/**
 * @Author：LovingLiu
 * @Description: OrderException 订单服务异常处理
 * @Date：Created in 2019-10-29
 */
@Data
public class OrderException extends RuntimeException {
    private Integer code;

    public OrderException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public OrderException(ExceptionCodeEnum exceptionCodeEnum) {
        super(exceptionCodeEnum.getMsg());
        this.code = exceptionCodeEnum.getCode();
    }
}
