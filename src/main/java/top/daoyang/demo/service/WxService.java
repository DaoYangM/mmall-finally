package top.daoyang.demo.service;

import top.daoyang.demo.entity.WxUser;

import java.util.Map;

public interface WxService {
    String wxLogin(Map<String, String> map);

    WxUser setInfo(String userId, WxUser wxUser);
}
