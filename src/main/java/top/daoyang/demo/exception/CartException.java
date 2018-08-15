package top.daoyang.demo.exception;

import top.daoyang.demo.enums.ExceptionEnum;

public class CartException extends GenericException {
    public CartException(ExceptionEnum exceptionEnum) {
        super(exceptionEnum);
    }
}
