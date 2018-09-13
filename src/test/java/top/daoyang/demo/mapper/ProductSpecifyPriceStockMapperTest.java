package top.daoyang.demo.mapper;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import top.daoyang.demo.entity.ProductSpecifyPriceStock;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductSpecifyPriceStockMapperTest {

    @Autowired
    private ProductSpecifyPriceStockMapper productSpecifyPriceStockMapper;

    @Autowired
    private ProductSpecifyItemMapper productSpecifyItemMapper;

    @Test
    public void getProductSpecifyPriceAndStock() {

        log.info(productSpecifyPriceStockMapper.getProductSpecifyPriceAndStock(28, "1,4").getPrice().toString());
    }

    @Test
    public void getProductSpecifyPASBySpecifyId() {
        List<String> ss = Arrays.asList(productSpecifyPriceStockMapper.getProductSpecifyPASBySpecifyId(28, 1).getSpecifyIds().split(""))
            .stream().map(item -> productSpecifyItemMapper.getProductSpecifyItemById(Integer.parseInt(item)).getDescription()).collect(Collectors.toList());

        log.info(ss.toString());
    }
}