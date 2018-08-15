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
    CATEGORY_CREATE_ERROR("Category create error", 604),

    PRODUCT_DOES_NOT_EXIST("Product doesn't exist", 701),
    PRODUCT_CREATE_ERROR("Product create error", 702),
    PRODUCT_UPDATE_ERROR("Product update error", 703),
    PRODUCT_SET_STATUS_ERROR("Product set status error", 704),
    PRODUCT_OUT_OF_STOCK("Product Insufficient inventory", 705),

    CART_CREATE_ERROR("Cart insert failure", 801),
    CART_DOES_NOT_EXIST("Cart doesn't exist", 802) ,
    CART_UPDATE_ERROR("Cart update error", 803),
    CART_SELECT_ERROR("Cart select error", 804),
    CART_UNSELECTED_ERROR("Cart unselected error", 805),
    CART_SELECT_ALL_ERROR("Cart select all error", 806),
    CART_UNSELECTED_ALL_ERROR("Cart unselected all error", 807);

    private String msg;
    private int code;

    ExceptionEnum(String msg, int code) {
        this.msg = msg;
        this.code = code;
    }
}
