package top.daoyang.demo.service;

import top.daoyang.demo.entity.User;

public interface UserService {

    User findUserByUsername(String username);

    User findUserById(Long userId);

    boolean checkUsername(String username);

    boolean checkEmail(String email);

    boolean checkPhone(String phone);
}
