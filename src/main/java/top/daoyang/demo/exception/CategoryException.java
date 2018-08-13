package top.daoyang.demo.exception;

import top.daoyang.demo.enums.ExceptionEnum;

public class CategoryException extends GenericException {


    public CategoryException(ExceptionEnum exceptionEnum) {
        super(exceptionEnum);
    }
}
