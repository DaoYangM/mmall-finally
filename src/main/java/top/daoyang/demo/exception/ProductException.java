package top.daoyang.demo.exception;

import top.daoyang.demo.enums.ExceptionEnum;

public class ProductException extends GenericException {
    public ProductException(ExceptionEnum exceptionEnum) {
        super(exceptionEnum);
    }
}
