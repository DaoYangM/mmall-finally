package top.daoyang.demo.exception;

import top.daoyang.demo.enums.ExceptionEnum;

public class CategoryExistedException extends GenericException {

    public CategoryExistedException(ExceptionEnum exceptionEnum) {
        super(exceptionEnum);
    }
}
