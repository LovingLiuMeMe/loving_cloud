package cn.lovingliu.loving.exception;

import cn.lovingliu.loving.enums.ExceptionCodeEnum;
import lombok.Data;

/**
 * @Author：LovingLiu
 * @Description: AuthException 订单服务异常处理
 * @Date：Created in 2019-10-29
 */
@Data
public class AuthException extends RuntimeException {
    private Integer code;

    public AuthException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public AuthException(ExceptionCodeEnum exceptionCodeEnum) {
        super(exceptionCodeEnum.getMsg());
        this.code = exceptionCodeEnum.getCode();
    }
}
