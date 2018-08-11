package top.daoyang.demo.enums;

import lombok.Getter;

@Getter
public enum  ExceptionEnum {
    USER_DOES_NOT_EXIST("User doesn't exist", 201),

    EMPTY_EMAIL("Email can't be empty", 301),
    EMPTY_USERNAME("Email can't be empty", 302),
    EMPTY_PHONE("Mobile phone can't be empty", 303),

    VALIDATE_EMAIL("Incorrect mailbox format", 401),
    VALIDATE_PHONE("Incorrect phone number format", 402);


    private String msg;
    private int code;

    ExceptionEnum(String msg, int code) {
        this.msg = msg;
        this.code = code;
    }
}
