package top.daoyang.demo.utils;

import com.aliyuncs.exceptions.ClientException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SmsSenderUtilsTest {

    @Autowired
    private SmsSenderUtils smsSenderUtils;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Test
    public void send() throws ClientException {
        smsSenderUtils.send("18359056538", smsSenderUtils.generateCode());
    }

    @Test
    public void generateCode() {

        redisTemplate.opsForValue().set("SMS_18359056538", smsSenderUtils.generateCode(), 60L, TimeUnit.SECONDS);
        String res = redisTemplate.opsForValue().get("SMS_18359056538");
        System.out.println(res);
    }
}