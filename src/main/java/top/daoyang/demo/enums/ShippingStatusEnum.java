package top.daoyang.demo.enums;

import lombok.Getter;

@Getter
public enum ShippingStatusEnum {
    UNDELETED(0),
    DELETED(1);

    private Integer code;

    ShippingStatusEnum(Integer code) {
        this.code = code;
    }
}
