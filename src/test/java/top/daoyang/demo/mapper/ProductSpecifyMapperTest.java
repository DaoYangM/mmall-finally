package top.daoyang.demo.mapper;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import top.daoyang.demo.entity.ProductSpecify;

import static org.junit.Assert.*;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductSpecifyMapperTest {

    @Autowired
    private ProductSpecifyMapper productSpecifyMapper;

    @Test
    public void getSpecifyByProductId() {
        log.info(productSpecifyMapper.getSpecifyByProductId(28).toString());
    }
}