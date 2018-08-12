package top.daoyang.demo.payload;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;
import top.daoyang.demo.enums.ExceptionEnum;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServerResponse<T> implements Serializable {
    private int status;
    private String msg;
    private T data;

    private ServerResponse(int status) {
        this.status = status;
    }

    private ServerResponse(int status, T data) {
        this(status);
        this.data = data;
    }

    private ServerResponse(int status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    private ServerResponse(int status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    @JsonIgnore
    public boolean isSuccess() {
        return this.status == HttpStatus.OK.value();
    }

    public int getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }


    public static <T> ServerResponse<T> createBySuccess() {
        return new ServerResponse<T>(HttpStatus.OK.value(), "success");
    }

    public static <T> ServerResponse<T> createBySuccess(T data) {
        return new ServerResponse<T>(HttpStatus.OK.value(), "success",data);
    }

    public static <T> ServerResponse<T> createBySuccess(String msg, T data) {
        return new ServerResponse<T>(HttpStatus.OK.value(), msg, data);
    }

    public static <T> ServerResponse<T> createBySuccessMessage(String msg) {
        return new ServerResponse<T>(HttpStatus.OK.value(), msg);
    }

    public static <T> ServerResponse<T> createByErrorMessage(String msg) {
        return new ServerResponse<T>(HttpStatus.INTERNAL_SERVER_ERROR.value(), msg);
    }

    public static <T> ServerResponse<T> createByErrorCodeMessage(int code ,String msg) {
        return new ServerResponse<T>(code, msg);
    }

    public static <T> ServerResponse<T> createByErrorException(ExceptionEnum exceptionEnum) {
        return createByErrorCodeMessage(exceptionEnum.getCode(), exceptionEnum.getMsg());
    }
}
