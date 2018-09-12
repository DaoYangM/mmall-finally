package top.daoyang.demo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import top.daoyang.demo.service.WxService;
import top.daoyang.demo.util.MD5Utils;

import java.util.Map;
import java.util.Objects;

@Service
public class WxServiceImpl implements WxService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public String wxLogin(Map<String, String> map) {
        String md5 = MD5Utils.crypt(map.toString());

        stringRedisTemplate.opsForHash().putAll(md5, map);
        return md5;
    }
}
