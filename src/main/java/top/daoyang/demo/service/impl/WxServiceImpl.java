package top.daoyang.demo.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import top.daoyang.demo.entity.WxUser;
import top.daoyang.demo.mapper.WxUserMapper;
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

    @Autowired
    private WxUserMapper wxUserMapper;

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

    @Override
    public WxUser setInfo(String userId, WxUser wxUser) {
        WxUser userInfo = wxUserMapper.getByOpenId(userId);

        if (userInfo != null) {
            userInfo.setNickName(wxUser.getNickName());
            userInfo.setAvatar(wxUser.getAvatar());
            userInfo.setGender(wxUser.getGender());

            wxUserMapper.updateByPrimaryKeySelective(userInfo);

        } else {
            wxUser.setUserId(userId);
            wxUserMapper.insertSelective(wxUser);
        }

        return wxUser;
    }
}
