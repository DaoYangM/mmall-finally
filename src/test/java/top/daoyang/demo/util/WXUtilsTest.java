package top.daoyang.demo.util;

import net.bytebuddy.asm.Advice;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WXUtilsTest {

    @Autowired
    private WXUtils wxUtils;

    @Test
    public void getAccessToken() {
        wxUtils.getAccessToken();
    }
}