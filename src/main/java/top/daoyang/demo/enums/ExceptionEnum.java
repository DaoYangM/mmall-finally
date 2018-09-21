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
    PRODUCT_UPDATE_STOCK_ERROR("Product update stock error", 706),
    PRODUCT_SPECIFY_DOES_EXIST("Product specify does exist", 708),


    CART_CREATE_ERROR("Cart insert failure", 801),
    CART_DOES_NOT_EXIST("Cart doesn't exist", 802) ,
    CART_UPDATE_ERROR("Cart update error", 803),
    CART_SELECT_ERROR("Cart select error", 804),
    CART_UNSELECTED_ERROR("Cart unselected error", 805),
    CART_SELECT_ALL_ERROR("Cart select all error", 806),
    CART_UNSELECTED_ALL_ERROR("Cart unselected all error", 807),
    CART_IS_EMPTY("Cart is empty", 808),
    CART_CLEAN_ERROR("Cart clean error", 809),
    CART_SPECIFY_ID_DOES_NOT_EXIST("Cart specifyId doesn't exist", 810),

    SHIPPING_CREATE_ERROR("Shipping create error", 1001),
    SHIPPING_UPDATE_ERROR("Shipping update error", 1002),
    SHIPPING_DELETE_ERROR("Shipping delete error", 1003),
    SHIPPING_DOES_NOT_EXIST("Shipping doesn't exist", 1003),

    ORDER_DOES_NOT_EXIST("Order doesn't exist", 1101),
    ORDER_CANCEL_ERROR("Order cancel error", 1102),
    ORDER_HAS_BEEN_CANCELED("Order has been canceled", 1103),
    ORDER_HAS_BEEN_PAID("Order has been paid", 1104),
    ORDER_HAS_BEEN_CLOSED("Order has been closed", 1105),
    ORDER_HAS_BEEN_SUCCESS("Order has been success", 1106),
    ORDER_STATUS_ERROR("Order status error", 1107),
    ORDER_STATUS_UPDATE_ERROR("Order status update error", 1108),
    ORDER_CREATE_OUT_OF_STOCK_ERROR("Order create out of stock error", 1109),
    ORDER_CREATE_ERROR("Order create  error", 1110),
    ORDER_ITEM_CREATE_ERROR("OrderItem create error", 1111),


    ALIPAY_REPEATED_CALL("Alipay repeated call", 1201),
    ALIPAY_TRADE_STATUS_ERROR("Alipay trade status error", 1202),

    WX_GET_OPENID_FAILURE("WX get openid failure", 1301),

    FAVORITE_HAS_EXISTED("Favorite has existed", 1400),
    FAVORITE_CREATE_EXCEPTION("Favorite create exception", 1401),
    FAVORITE_DOES_NOT_EXIST("Favorite doesn't exist", 1402),

    QRCODE_REXP_STADE_VALUE_ERROR("QRCode value is not 10000", 1501);


    private String msg;
    private int code;

    ExceptionEnum(String msg, int code) {
        this.msg = msg;
        this.code = code;
    }
}
