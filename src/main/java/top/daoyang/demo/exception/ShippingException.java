package top.daoyang.demo.exception;

import top.daoyang.demo.enums.ExceptionEnum;

public class ShippingException extends GenericException {
    public ShippingException(ExceptionEnum exceptionEnum) {
        super(exceptionEnum);
    }
}
