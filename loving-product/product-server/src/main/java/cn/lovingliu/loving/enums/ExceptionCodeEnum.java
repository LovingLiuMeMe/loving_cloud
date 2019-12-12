package cn.lovingliu.loving.enums;

/**
 * @Author：LovingLiu
 * @Description: 异常的状态
 * @Date：Created in 2019-10-29
 */
public enum ExceptionCodeEnum implements CodeEnum {
    PARAM_ERROR(10001,"参数错误"),
    AUTH_TIMES_LIMIT(1002,"获取验证码过于频繁,请稍后再试"),
    AUTH_CODE_TIME_OUT(1003,"验证码过期"),
    USER_REGISTER_ERROR(1004,"用户注册失败"),
    LOGIN_TIME_OUT(1005,"登录超时,请重新登录"),
    USER_NOT_EXIT(1006,"用户不存在"),
    PRODUCT_NOT_EXIT(1007,"商品不存在"),
    ORDER_NOT_EXIT(1008,"订单不存在"),
    ACTIVITY_NOT_EXIT(1009,"活动不存在"),
    AUTHORIZE_FAIL(1010, "权限不足"),
    STOCK_WARN(1011, "库存警告"),
    SYSTEM_BUSY(1012, "系统正忙");

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
