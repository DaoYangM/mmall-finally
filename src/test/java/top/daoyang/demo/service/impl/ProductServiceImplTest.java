package top.daoyang.demo.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import top.daoyang.demo.mapper.ProductMapper;
import top.daoyang.demo.mapper.ProductSpecifyPriceStockMapper;
import top.daoyang.demo.service.ProductService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductServiceImplTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductSpecifyPriceStockMapper productSpecifyPriceStockMapper;

    @Test
    public void getProductSpecifyBySpecifyId() {
        System.out.println(productService.getProductSpecifyBySpecifyId(31, 15));
    }

    @Test
    public void getProductSpecifyPriceAndStock() {
        productSpecifyPriceStockMapper.getProductSpecifyPriceAndStock(28, "1,4");
    }
}