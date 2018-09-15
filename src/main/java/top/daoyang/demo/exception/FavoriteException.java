package top.daoyang.demo.exception;

import top.daoyang.demo.enums.ExceptionEnum;

public class FavoriteException extends GenericException {

    public FavoriteException(ExceptionEnum exceptionEnum) {
        super(exceptionEnum);
    }
}
