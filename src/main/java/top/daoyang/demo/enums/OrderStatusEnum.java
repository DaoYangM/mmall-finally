package top.daoyang.demo.enums;

import lombok.Getter;
import top.daoyang.demo.exception.OrderException;

import java.util.Arrays;

@Getter
public enum OrderStatusEnum {
    NO_PAY(0, "未支付"),
    PAID(10, "已付款"),
    ORDER_SUCCESS(20, "订单完成"),
    ORDER_CANCEL(40, "订单取消"),
    WAIT_DELIVERY(50, "代发货"),
    WAIT_RECEIPT(60, "待收货"),
    WAIT_COMMENT(70, "待评价");


    private int code;

    private String msg;

    OrderStatusEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static OrderStatusEnum getStatus(int status) {
        for (OrderStatusEnum orderStatusEnum: values()) {
            if (orderStatusEnum.getCode() == status)
                return orderStatusEnum;
        }

        throw new OrderException(ExceptionEnum.ORDER_HAS_BEEN_CANCELED);
    }
}
