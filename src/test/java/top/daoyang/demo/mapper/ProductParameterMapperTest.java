package top.daoyang.demo.mapper;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import top.daoyang.demo.entity.ProductParamter;

import static org.junit.Assert.*;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductParameterMapperTest {

    @Autowired
    private ProductParameterMapper productParameterMapper;
    @Test
    public void getProductParameterByProductId() {

        for (ProductParamter productParamter :productParameterMapper.getProductParameterByProductId(26)) {
            log.info(productParamter.getParameterName());
        };
    }
}