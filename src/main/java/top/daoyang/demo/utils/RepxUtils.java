package top.daoyang.demo.utils;

import java.util.regex.Pattern;

public class RepxUtils {
    private static final Pattern VALID_EMAIL_ADDRESS_REGEX  =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    private static final Pattern VALID_PHONE_NUMBER_REGEX  =
            Pattern.compile("\\d{10}|(?:\\d{3}-){2}\\d{4}|\\(\\d{3}\\)\\d{3}-?\\d{4}", Pattern.CASE_INSENSITIVE);


    public static boolean checkEmail(String email) {
        return VALID_EMAIL_ADDRESS_REGEX.matcher(email).find();
    }

    public static boolean checkPhone(String phone) {
        return VALID_PHONE_NUMBER_REGEX.matcher(phone).find();
    }
}
