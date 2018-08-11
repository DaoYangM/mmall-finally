package top.daoyang.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import top.daoyang.demo.payload.ServerResponse;

@RestControllerAdvice(basePackages = "top.daoyang.demo.controller")
public class GlobalExceptionHandler {

    @ExceptionHandler(GenericException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ServerResponse genericException(GenericException ex) {
        return ServerResponse.createByErrorException(ex.getExceptionEnum());
    }
}
