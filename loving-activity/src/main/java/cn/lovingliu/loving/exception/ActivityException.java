package cn.lovingliu.loving.exception;

import cn.lovingliu.loving.enums.ExceptionCodeEnum;
import lombok.Data;

/**
 * @Author：LovingLiu
 * @Description: UserException 统一异常处理
 * @Date：Created in 2019-10-29
 */
@Data
public class ActivityException extends RuntimeException {
    private Integer code;

    public ActivityException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public ActivityException(ExceptionCodeEnum exceptionCodeEnum) {
        super(exceptionCodeEnum.getMsg());
        this.code = exceptionCodeEnum.getCode();
    }
}
