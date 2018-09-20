package top.daoyang.demo.mapper;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductMapperTest {

    @Autowired
    private ProductMapper productMapper;

    @Test
    public void findSearBarKeyWord() {
        log.info(productMapper.findSearBarKeyWord("冰箱", 1).toString());
    }

    @Test
    public void searchByKeyword() {
        productMapper.searchByKeyword("iphone", 1, "price_asc");
    }

    @Test
    public void findProduct() {
        productMapper.findProduct(1, "price_asc");
    }
}