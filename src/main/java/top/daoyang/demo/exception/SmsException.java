package top.daoyang.demo.exception;

import top.daoyang.demo.enums.ExceptionEnum;

public class SmsException extends GenericException {

    public SmsException(ExceptionEnum exceptionEnum) {
        super(exceptionEnum);
    }
}
