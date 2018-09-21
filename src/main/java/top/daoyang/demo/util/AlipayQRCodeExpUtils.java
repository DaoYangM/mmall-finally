package top.daoyang.demo.util;

import lombok.extern.slf4j.Slf4j;
import top.daoyang.demo.enums.ExceptionEnum;
import top.daoyang.demo.exception.QRCodeException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class AlipayQRCodeExpUtils {
    public static String qrCodeAddress(String body) {
        Pattern pattern = Pattern.compile("\"code\":\"(\\d+)\".*\"qr_code\":\"(.*)\"},\"sign\":");

        Matcher m = pattern.matcher(body);
        if (m.find( )) {
            log.info("QRCode value: {}", m.group(1));
            if (!m.group(1).equals("10000")) {
                throw new QRCodeException(ExceptionEnum.QRCODE_REXP_STADE_VALUE_ERROR);
            }

            return m.group(2);
        } else {
            System.out.println("NO MATCH");
        }

        return null;
    }
}
