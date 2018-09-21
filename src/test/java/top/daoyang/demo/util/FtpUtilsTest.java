package top.daoyang.demo.util;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

public class FtpUtilsTest {

    @Test
    public void upload() throws FileNotFoundException, JSchException, SftpException {
        InputStream inputStream = new FileInputStream("D:\\12345.txt");
//        FtpUtils.upload("12345.txt", inputStream);
    }

    @Test
    public void qrExp() {

        String test = "{\"code\":\"10000\",\"msg\":\"Success\",\"out_trade_no\":\"1537500027031\",\"qr_code\":\"https:\\/\\/qr.alipay.com\\/bax013442eani9z11fae007a\"}";
        Pattern pattern = Pattern.compile("\"code\":\"(\\d+)\".*\"qr_code\":\"(.*)\"}");

        Matcher m = pattern.matcher(test);
        if (m.find( )) {
            System.out.println("Found value: " + m.group(0) );
            System.out.println("Found value: " + m.group(1) );
            System.out.println("Found value: " + m.group(2) );
        } else {
            System.out.println("NO MATCH");
        }
    }
}