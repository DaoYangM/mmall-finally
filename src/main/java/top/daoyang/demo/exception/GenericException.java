package top.daoyang.demo.exception;

import lombok.Getter;
import top.daoyang.demo.enums.ExceptionEnum;

@Getter
public class GenericException extends RuntimeException {

    private int code;

    private ExceptionEnum exceptionEnum;

    public GenericException(ExceptionEnum exceptionEnum) {
        this.exceptionEnum = exceptionEnum;
    }
}
