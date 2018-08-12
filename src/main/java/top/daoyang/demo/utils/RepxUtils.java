package top.daoyang.demo.utils;

import java.util.regex.Pattern;

public class RepxUtils {
    private static final Pattern VALID_EMAIL_ADDRESS_REGEX  =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    private static final Pattern VALID_PHONE_NUMBER_REGEX  =
            Pattern.compile("^((13[0-9])|(15[^4])|(18[0-9])|(17[0-9])|(147))\\d{8}$", Pattern.CASE_INSENSITIVE);


    public static boolean checkEmail(String email) {
        return VALID_EMAIL_ADDRESS_REGEX.matcher(email).find();
    }

    public static boolean checkPhone(String phone) {
        return VALID_PHONE_NUMBER_REGEX.matcher(phone).find();
    }
}
