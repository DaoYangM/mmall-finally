package top.daoyang.demo.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import top.daoyang.demo.entity.User;
import top.daoyang.demo.enums.ExceptionEnum;
import top.daoyang.demo.exception.SmsException;
import top.daoyang.demo.mapper.UserMapper;
import top.daoyang.demo.payload.request.UserPasswordUpdateRequest;
import top.daoyang.demo.service.UserService;
import top.daoyang.demo.utils.SmsSenderUtils;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User findUserByUsername(String username) {
        return userMapper.selectByUsername(username);
    }

    public User findUserById(Long userId) {
        return userMapper.selectByPrimaryKey(userId);
    }

    @Override
    public boolean checkUsername(String username) {
        return userMapper.selectByUsername(username) != null;
    }

    @Override
    public boolean checkEmail(String email) {
        return userMapper.checkEmail(email) > 0;
    }

    @Override
    public boolean checkPhone(String phone) {
        return userMapper.checkPhone(phone) > 0;
    }

    @Override
    public boolean updatePassword(UserPasswordUpdateRequest userPasswordUpdateRequest, User user) {
        String resCode = stringRedisTemplate.opsForValue().get(SmsSenderUtils.SMS_PREFIX + user.getPhone());
        if (StringUtils.hasText(resCode) && resCode.equals(userPasswordUpdateRequest.getSmsCode())) {
            User updateUser = new User();
            BeanUtils.copyProperties(user, updateUser);

            user.setPassword(passwordEncoder.encode(userPasswordUpdateRequest.getPassword()));

            return userMapper.updateByPrimaryKeySelective(user) == 1 ;
        }

        throw new SmsException(ExceptionEnum.SMS_CODE_DOES_NOT_MATCH);
    }
}
