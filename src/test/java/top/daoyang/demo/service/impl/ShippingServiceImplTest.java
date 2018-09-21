package top.daoyang.demo.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.protocol.HttpClientContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ResourceUtils;
import top.daoyang.demo.service.ShippingService;

import javax.servlet.http.HttpServletRequest;
import java.io.FileNotFoundException;

import static org.junit.Assert.*;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class ShippingServiceImplTest {

    @Autowired
    private ShippingService shippingService;

    @Test
    public void changeChecked() {
////        shippingService.changeChecked("ozG-P4u4zWyrHs6TrSjgeqjN8tzY", 29);
//        try {
////            log.info(HttpClientContext.create().getServletContext().getRealPath("");
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
    }
}