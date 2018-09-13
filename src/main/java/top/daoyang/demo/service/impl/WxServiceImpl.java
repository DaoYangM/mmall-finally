package top.daoyang.demo.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import top.daoyang.demo.security.JwtTokenProvider;
import top.daoyang.demo.service.WxService;
import top.daoyang.demo.util.MD5Utils;

import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
public class WxServiceImpl implements WxService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Override
    public String wxLogin(Map<String, String> map) {
//        String md5 = MD5Utils.crypt(map.toString());
//
//        stringRedisTemplate.opsForHash().putAll(md5, map);
        System.out.println(map.get("openid"));
        String jwtToken = jwtTokenProvider.generateToken(map.get("openid"));
        log.info("jwtToken {}", jwtToken);
        return jwtToken;
    }
}