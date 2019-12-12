package cn.lovingliu.loving.enums;

/**
 * @Author：LovingLiu
 * @Description: 异常的状态
 * @Date：Created in 2019-10-29
 */
public enum ExceptionCodeEnum implements CodeEnum {
    PARAM_ERROR(10001,"参数错误"),
    ACTIVITY_NOT_EXIT(1007,"活动不存在");

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
