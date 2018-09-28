package top.daoyang.demo.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import weixin.popular.api.TokenAPI;
import weixin.popular.api.UserAPI;
import weixin.popular.bean.token.Token;
import weixin.popular.bean.user.User;

import java.util.Arrays;

@Slf4j
@Component
public class WXUtils {

    String getAccessToken() {
        Token token = TokenAPI.token("wx1d4c16ae601dbd58", "yedeyang5201314");
        String accessToken = token.getAccess_token();
        User userInfo = UserAPI.userInfo(accessToken, "ozG-P4u4zWyrHs6TrSjgeqjN8tzY");
        log.info(userInfo.getNickname(), userInfo.getHeadimgurl());
        return userInfo.toString();
    }
}
