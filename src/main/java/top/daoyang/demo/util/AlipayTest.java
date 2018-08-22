package top.daoyang.demo.util;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayOpenPublicTemplateMessageIndustryModifyRequest;
import com.alipay.api.request.AlipayTradePayRequest;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.response.AlipayOpenPublicTemplateMessageIndustryModifyResponse;
import com.alipay.api.response.AlipayTradePayResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

//@Component
public class AlipayTest {

    @Autowired
    private AlipayField alipayField;

    private String appId = "2016082100302525";

    private String privateKey = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCpe0jBNSc/AJw3R1r8diwW4Xnr9PxD3W71OS9UE87wYTkr02aQPFm4VmDNh8UtDK75Gng4aa+MKoWvu73hUt0r/qM4JwcN4aZExcIjzCXVQdMuMpeC6+Yb4lHsDEp8+P3YH/b/Xvg0RNCfq8MsQpRp86kXwyYhra9gTZnjdWaS0+9v853HmGSMpPDHGd5aNZGqhsGAv2xjwlnZF6XOINCXeEKR9Lr3GlEUx8pJtlykRFpO+QILQr63j7WMGNuNcY6DijoqpEon3ppyl9pNzJEo2Wg4zNDMsEcLWZ0TTydBujQw5BZzqAKPxddlXt2j1s/wpuqfBaBx/Mm1pn//2jKlAgMBAAECggEAH0M/i2w7nhKFvmiqLvG5dksHS3A4bGhXRGLVCSRSQuMcQapQPAD159v/JUqLI6E4UXkBfh54pFlfqafvAbZgV/Izt3LziSr7maoFAsdwnyLySo5IhnzvSGt6AxnNT7o6UPisfjTNgg7DkQCFExABAn5QzbJQwVuhgxktbtjEIAcr0tUXYGomuI3QbEGA+971CVxwrCXOW2X1hmK4AgIUkvKJ6ewxtwKkuX8ewX+r38jKXb9oXFnFv+flPb5t/rUOHz7QD2zWSQYIODXrZLKCW9PaPmgh8qF0FqNyyTz4ei2fy2C2nhRtkJbkFpim/+5WmMH3y6zdOBCuyhH2QksqzQKBgQD84qnzKAX1tUkp/N/fEVFPBHvo62tIz4oc27dqN3cv6seTDDo0LbywmCnGehheVHyWw1sHA43pmVw2sdaCz2BjRs/TWTsLXsk5IV+/ij39HAk7vseAKZn3nr1/86LexWHDWnALBgSAo7bkCXLJVYn/xKXoZ7tg+Ioa1xV7tGcYZwKBgQCrkabhSi2Ws4RUX3/qi5O1LzDmKoFhymHEPD48jBgB2hb4UQ+ZatkyMqFwqkY7JDTnN0xHvFZSFgNxhOeRydcLK4NhEv8H+/aghfdghtNQuTnXgw4Ye4rkP8oZglwNYSyV1SaaZMk64BWLE6cuJlVQmp3pw9KwXdt80FzKAx+lEwKBgQCFBRGSZuYjpfVTUcHMy4Yyg1BOZ68qQIkMvwJOq9hU6zGJhvACzGApoBCxZz30OIrEnwP9v3/hdZsM5iTI8B5qejANoy0swPIlzAlabnRG9H1i4Op93uQvXV+es2Bjv/25zZOMKOErbXJhpxqrDwSk9iKzCgWFb5btoV+aj31/VwKBgBYtGmvuYZwkEgux2F/hsLLvqZtmjRitxxtNSiIbXZMHtIZeO+zxoadaYgGlELTeRjRIsoRnNKUhkUpgCmqOs0wPUMkAYpSOnd870bDZXIEHoBFaMD074sUD9cR8VBeqos+vIHE+a8A/bKlwXeCaNdbYVWb818PSLeehKw01gCafAoGBANNgYgUq3NPR2B954Gv16s4C071OJL7QwqAkMjJhvEZCphE0SnPlU/wm6hWYaOFH+4LPkcPTisz6bsDbQy3Z1ErNCBoy0pzC1gj6dzOlhjHaBOHM9MGhEK5y5HmiSdrkLafjpZ75Jv65G5AJOYdPGAKcuQ2nD2yRSF7B8tX9cCmr";

    private String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA0bVCeLj5eJ3ksLPLDwTeu37yhBnq/XDtHf5ElY21jqJMUdr8Lm6mgpa63U2cENoOmY/Ob31nMGes8yHWdwC0vBvkNMYqHoto1s2Z2apXn6SwXgq+cXNQPClxu0c2aCj44TPuVloj9gn+ENyPUingTP4dg0OPPu1Zg8fDlCWLRkBf9rKNg3xezjuppiLc9cu5Wn0nvWSgVTMRzFGaclY1QFjpA0XVK9LpfhJuBvBtAaMADW+hN9QRRLfDucrxy9Y/oqwNeLBsdmI6MrPz6sc70NWC1jUab7MwdDYhyu0hCpbZYCxfUBuyqwrLSfqnhc4VY7VJFnMZsgX300sB0QGvmQIDAQAB";
    public void pay() {
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipaydev.com/gateway.do", appId, privateKey, "json", "utf8", publicKey, "RSA2");
//实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.open.public.template.message.industry.modify
        AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();//创建API对应的request类
        request.setBizContent("{" +
                "    \"out_trade_no\":\"20180320010101002\"," +
                "    \"total_amount\":\"88.88\"," +
                "    \"subject\":\"Iphone6 16G\"," +
                "    \"store_id\":\"NJ_001\"," +
                "    \"timeout_express\":\"90m\"}");//设置业务参数
        AlipayTradePrecreateResponse response = null;
        try {
            response = alipayClient.execute(request);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        System.out.print("123" + response.getBody());
    }
    public static void main(String[] args) {
        AlipayTest alipayTest = new AlipayTest();
        alipayTest.pay();
    }
}
