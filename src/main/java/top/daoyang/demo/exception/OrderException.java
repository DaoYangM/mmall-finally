package top.daoyang.demo.exception;

import top.daoyang.demo.enums.ExceptionEnum;

public class OrderException extends GenericException {

    public OrderException(ExceptionEnum exceptionEnum) {
        super(exceptionEnum);
    }
}
