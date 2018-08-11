package top.daoyang.demo.exception;

import top.daoyang.demo.enums.ExceptionEnum;

public class ValidationException extends GenericException {
    public ValidationException(ExceptionEnum exceptionEnum) {
        super(exceptionEnum);
    }
}
