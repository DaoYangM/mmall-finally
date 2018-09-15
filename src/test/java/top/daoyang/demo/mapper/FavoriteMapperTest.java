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
public class FavoriteMapperTest {

    @Autowired
    private FavoriteMapper favoriteMapper;

    @Test
    public void findFavoriteByUserIdAndProductId() {
        log.info(favoriteMapper.findFavoriteByUserIdAndProductId("ozG-P4u4zWyrHs6TrSjgeqjN8tzY", 26).getUserId());
    }

    @Test
    public void createFavoriteByUserIdAndProductId() {
        log.info(favoriteMapper.createFavoriteByUserIdAndProductId("ozG-P4u4zWyrHs6TrSjgeqjN8tzY", 27).toString());
    }

    @Test
    public void cancelFavoriteByUserIdAndProductId() {
        log.info(favoriteMapper.cancelFavoriteByUserIdAndProductId("ozG-P4u4zWyrHs6TrSjgeqjN8tzY", 27).toString());
    }
}