package top.daoyang.demo.service.impl;

import com.aliyuncs.exceptions.ClientException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import top.daoyang.demo.entity.User;
import top.daoyang.demo.enums.ExceptionEnum;
import top.daoyang.demo.exception.SmsException;
import top.daoyang.demo.exception.UserRegisterException;
import top.daoyang.demo.mapper.UserMapper;
import top.daoyang.demo.service.AuthService;
import top.daoyang.demo.util.SmsSenderUtils;
import top.daoyang.demo.payload.request.RegisterRequest;

import java.util.concurrent.TimeUnit;

import static top.daoyang.demo.enums.ExceptionEnum.USER_REGISTER_INSERT_DB_FAILED;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private SmsSenderUtils smsSenderUtils;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public boolean sendSms(String phone) {
        String res = stringRedisTemplate.opsForValue().get(SmsSenderUtils.SMS_PREFIX + phone);
        if (res != null) {
            throw new SmsException(ExceptionEnum.SMS_CODE_INTERVAL_LESS_THAN_1_MINUTE);
        }
        else{
            String generateCode = smsSenderUtils.generateCode();
            try {
                if (smsSenderUtils.send(phone, generateCode)) {
                    stringRedisTemplate.opsForValue().set(SmsSenderUtils.SMS_PREFIX + phone, generateCode, 60L, TimeUnit.SECONDS);
                    return true;
                }
                return false;
            } catch (ClientException e) {
                log.error(e.getMessage());
                return false;
            }
        }
    }

    @Override
    public boolean register(RegisterRequest registerRequest) {
        String resCode = stringRedisTemplate.opsForValue().get(SmsSenderUtils.SMS_PREFIX + registerRequest.getPhone());
        if (StringUtils.hasText(resCode) && resCode.equals(registerRequest.getSmsCode())) {
            User user = new User();
            BeanUtils.copyProperties(registerRequest, user);
            user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
            user.setRole(1);

            if (userMapper.insertSelective(user) != 1)
                throw new UserRegisterException(USER_REGISTER_INSERT_DB_FAILED);

            return true;

        } else {
            throw new SmsException(ExceptionEnum.SMS_CODE_DOES_NOT_MATCH);
        }
    }
}
