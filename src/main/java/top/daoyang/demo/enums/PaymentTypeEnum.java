package top.daoyang.demo.enums;

import lombok.Getter;

@Getter
public enum PaymentTypeEnum {

    ALIPAY(1);

    private Integer value;

    PaymentTypeEnum(Integer value) {

        this.value = value;
    }
}
