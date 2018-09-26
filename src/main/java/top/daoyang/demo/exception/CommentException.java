package top.daoyang.demo.exception;

import top.daoyang.demo.enums.ExceptionEnum;

public class CommentException extends GenericException {

    public CommentException(ExceptionEnum exceptionEnum) {
        super(exceptionEnum);
    }
}
