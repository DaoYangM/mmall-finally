package top.daoyang.demo.exception;

import top.daoyang.demo.enums.ExceptionEnum;

public class WXException extends GenericException {

    public WXException(ExceptionEnum exceptionEnum) {
        super(exceptionEnum);
    }
}
