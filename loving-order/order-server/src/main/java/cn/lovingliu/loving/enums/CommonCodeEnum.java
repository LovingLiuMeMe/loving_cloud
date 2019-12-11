package cn.lovingliu.loving.enums;

/**
 * @Author：LovingLiu
 * @Description: 响应结果
 * @Date：Created in 2019-10-29
 */
public enum CommonCodeEnum implements CodeEnum{
    // 请求
    REQUEST_SUCCESS(200,"请求成功"),
    REQUEST_FAIL(500,"请求失败"),
    // 收货地址状态
    ORDER_STATUS_NO_PAY(0,"未付款"),
    ORDER_STATUS_PAYED(1,"已付款"),
    ORDER_STATUS_EXPRESS(2,"已发货"),
    ORDER_STATUS_BUYER_CANCEL(-1,"客户已取消"),
    ORDER_STATUS_TIME_OUT(-2,"订单超时"),
    ORDER_STATUS_SELLER_CANCEL(-3,"商家已取消"),
    ;




    private final int code;
    private final String msg;

    CommonCodeEnum(int code, String msg){
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
