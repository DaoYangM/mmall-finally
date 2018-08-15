package top.daoyang.demo.enums;

import lombok.Getter;

@Getter
public enum  ProductStatusEnum {
    ON_SALE(1),
    OFF_SALE(2),
    DELETED(3),

    CHECKED(1),
    UNCHECKED(0);
    private int value;

    ProductStatusEnum(int value) {
        this.value = value;
    }
}
