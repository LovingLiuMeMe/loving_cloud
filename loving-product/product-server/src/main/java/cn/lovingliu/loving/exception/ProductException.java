package cn.lovingliu.loving.exception;


import cn.lovingliu.loving.enums.ExceptionCodeEnum;
import lombok.Data;

/**
 * @Author：LovingLiu
 * @Description: ProductException 统一异常处理
 * @Date：Created in 2019-10-29
 */
@Data
public class ProductException extends RuntimeException {
    private Integer code;

    public ProductException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public ProductException(ExceptionCodeEnum exceptionCodeEnum) {
        super(exceptionCodeEnum.getMsg());
        this.code = exceptionCodeEnum.getCode();
    }
}
