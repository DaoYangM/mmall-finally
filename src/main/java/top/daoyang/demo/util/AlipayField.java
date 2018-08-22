package top.daoyang.demo.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
//
//@Component
public class AlipayField {

    @Value("${app.jwtSecret}")
    public String appId;

    @Value("${app.alipay.privateKey}")
    public String privateKey;

    @Value("${app.alipay.publicKey}")
    public String publicKey;
}
