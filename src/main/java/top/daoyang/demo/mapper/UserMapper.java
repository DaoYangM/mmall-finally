package top.daoyang.demo.mapper;

import top.daoyang.demo.entity.User;

public interface UserMapper {
    int deleteByPrimaryKey(Long id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    User selectByUsername(String username);

    Integer checkEmail(String email);

    Integer checkPhone(String phone);
}