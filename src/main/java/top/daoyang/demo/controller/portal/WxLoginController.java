package top.daoyang.demo.controller.portal;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import top.daoyang.demo.security.WXUserDetails;
import top.daoyang.demo.service.WxService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/wx")
public class WxLoginController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private WxService wxService;

    @GetMapping("/login")
    public String wxLogin(@RequestParam(value = "code") String code) throws IOException {
        System.out.println(code);
        String result = restTemplate.getForObject("https://api.weixin.qq.com/sns/jscode2session?appid=wx6088fd4f9debccbb&secret=c50c4eaf06e80a0d6c47b1a4a0ab1dac&js_code={JSCODE}&grant_type=authorization_code",
                String.class, code);

        Map<String, String> mapObj = new ObjectMapper().readValue(result, new TypeReference<HashMap<String,String>>(){});

        return wxService.wxLogin(mapObj);
    }

    @GetMapping("/info")
    public String info(@AuthenticationPrincipal WXUserDetails userDetails) {
        return userDetails.getId();
    }
}
