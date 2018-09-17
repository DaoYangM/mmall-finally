package top.daoyang.demo.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import top.daoyang.demo.service.ManageCategoryService;

import static org.junit.Assert.*;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class ManageCategoryServiceImplTest {

    @Autowired
    private ManageCategoryService manageCategoryService;

    @Test
    public void searchCategory() {
        log.info(manageCategoryService.searchCategory("冰箱").toString());
    }
}