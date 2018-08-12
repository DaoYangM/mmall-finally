package top.daoyang.demo.exception;

import com.aliyuncs.exceptions.ClientException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import top.daoyang.demo.payload.ServerResponse;

@RestControllerAdvice(basePackages = {"top.daoyang.demo.controller", "top.daoyang.demo.utils"})
public class GlobalExceptionHandler {

    @ExceptionHandler(GenericException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ServerResponse genericException(GenericException ex) {
        return ServerResponse.createByErrorException(ex.getExceptionEnum());
    }

    @ExceptionHandler(ClientException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ServerResponse aliyunSmsException(ClientException ex) {
        return ServerResponse.createByErrorCodeMessage(Integer.parseInt(ex.getErrCode()), ex.getMessage());
    }
}
