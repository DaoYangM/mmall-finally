package top.daoyang.demo.mapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import top.daoyang.demo.entity.User;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void insertSelective() {
        User user = new User();
        user.setId(22L);
        user.setUsername("daoyang");
        user.setEmail("498721777@qq.com");
        user.setPassword(passwordEncoder.encode("yedeyang"));
        user.setAnswer("498721777@qq.com");
        user.setPhone("18359056538");
        user.setQuestion("498721777@qq.com");
        user.setRole(1);

        userMapper.updateByPrimaryKeySelective(user);
    }
}