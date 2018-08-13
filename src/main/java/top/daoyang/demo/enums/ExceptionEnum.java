package top.daoyang.demo.enums;

import lombok.Getter;

@Getter
public enum  ExceptionEnum {

    USERNAME_EXISTED("Username has benn registration", 101),
    PHONE_EXISTED("Phone has benn registration", 102),
    EMAIL_EXISTED("Email has benn registration", 103),

    USER_DOES_NOT_EXIST("User doesn't exist", 201),
    USER_REGISTER_INSERT_DB_FAILED("User register insert database failure", 202),

    EMPTY_EMAIL("Email can't be empty", 301),
    EMPTY_USERNAME("Email can't be empty", 302),
    EMPTY_PHONE("Mobile phone can't be empty", 303),

    VALIDATE_EMAIL("Incorrect mailbox format", 401),
    VALIDATE_PHONE("Incorrect phone number format", 402),

    SMS_CODE_INTERVAL_LESS_THAN_1_MINUTE("Sms smsCode interval is less than 1 minute", 501),
    SMS_CODE_DOES_NOT_MATCH("Sms smsCode doesn't match", 502),

    CATEGORY_EXISTED("Category has already existed", 601),
    CATEGORY_DOES_NOT_EXIST("Category doesn't exist", 602),
    CATEGORY_UPDATE_ERROR("Category update error", 603),

    CATEGORY_CREATE_ERROR("Category create error", 604);
    private String msg;
    private int code;

    ExceptionEnum(String msg, int code) {
        this.msg = msg;
        this.code = code;
    }
}
