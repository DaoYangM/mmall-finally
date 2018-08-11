package top.daoyang.demo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.daoyang.demo.entity.User;
import top.daoyang.demo.mapper.UserMapper;
import top.daoyang.demo.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

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
}
