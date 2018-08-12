package top.daoyang.demo.exception;

import top.daoyang.demo.enums.ExceptionEnum;

public class UserRegisterException extends GenericException {

    public UserRegisterException(ExceptionEnum exceptionEnum) {
        super(exceptionEnum);
    }
}
