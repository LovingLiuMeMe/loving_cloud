package cn.lovingliu.loving.enums;

/**
 * @Author：LovingLiu
 * @Description: 异常的状态
 * @Date：Created in 2019-10-29
 */
public enum ExceptionCodeEnum implements CodeEnum {
    AUTH_TIMES_LIMIT(1002,"获取验证码过于频繁,请稍后再试"),
    AUTH_CODE_TIME_OUT(1003,"验证码过期");

    private final int code;
    private final String msg;

    ExceptionCodeEnum(int code, String msg){
        this.code = code;
        this.msg = msg;
    }

    @Override
    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
