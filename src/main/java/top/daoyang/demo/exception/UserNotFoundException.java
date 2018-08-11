package top.daoyang.demo.exception;

import lombok.Getter;
import top.daoyang.demo.enums.ExceptionEnum;

@Getter
public class UserNotFoundException extends GenericException {

    public UserNotFoundException(ExceptionEnum exceptionEnum) {
        super(exceptionEnum);
    }
}
