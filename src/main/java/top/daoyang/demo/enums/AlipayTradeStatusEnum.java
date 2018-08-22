package top.daoyang.demo.enums;

import lombok.Getter;

@Getter
public enum AlipayTradeStatusEnum {
    TRADE_SUCCESS("TRADE_SUCCESS");

    private String msg;

    AlipayTradeStatusEnum(String msg) {
        this.msg = msg;
    }
}
