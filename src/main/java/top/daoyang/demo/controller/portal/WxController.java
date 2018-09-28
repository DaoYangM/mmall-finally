package top.daoyang.demo.controller.portal;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jdk.nashorn.internal.objects.annotations.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import top.daoyang.demo.entity.WxUser;
import top.daoyang.demo.payload.ServerResponse;
import top.daoyang.demo.security.WXUserDetails;
import top.daoyang.demo.service.WxService;
import top.daoyang.demo.util.SignUtil;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/wx")
public class WxController {

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

    @PostMapping("/setInfo")
    public ServerResponse setInfo(@AuthenticationPrincipal WXUserDetails userDetails,
                          @Valid @RequestBody WxUser wxUser) {

        return ServerResponse.createBySuccess(wxService.setInfo(userDetails.getId(), wxUser));
    }

    @GetMapping("/info")
    public String info(@AuthenticationPrincipal WXUserDetails userDetails) {
        return userDetails.getId();
    }

    @GetMapping("/grant")
    public void grant(@RequestParam(value = "signature", required = true) String signature,
                        @RequestParam(value = "timestamp", required = true) String timestamp,
                        @RequestParam(value = "nonce", required = true) String nonce,
                        @RequestParam(value = "echostr", required = true) String echostr,
                        HttpServletResponse response) {
        try {
            if (SignUtil.checkSignature(signature, timestamp, nonce)) {
                PrintWriter out = response.getWriter();
                out.print(echostr);
                out.close();
            } else {
                log.info("这里存在非法请求！");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
