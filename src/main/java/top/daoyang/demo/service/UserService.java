package top.daoyang.demo.service;

import top.daoyang.demo.entity.User;
import top.daoyang.demo.payload.request.UserPasswordUpdateRequest;

public interface UserService {

    User findUserByUsername(String username);

    User findUserById(Long userId);

    boolean checkUsername(String username);

    boolean checkEmail(String email);

    boolean checkPhone(String phone);

    boolean updatePassword(UserPasswordUpdateRequest userPasswordUpdateRequest, User user);
}
