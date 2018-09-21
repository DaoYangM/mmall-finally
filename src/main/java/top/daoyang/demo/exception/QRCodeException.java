package top.daoyang.demo.exception;

import top.daoyang.demo.enums.ExceptionEnum;

public class QRCodeException extends GenericException {

    public QRCodeException(ExceptionEnum exceptionEnum) {
        super(exceptionEnum);
    }
}
