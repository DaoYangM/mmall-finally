package top.daoyang.demo.mapper;

import org.apache.ibatis.annotations.Param;
import top.daoyang.demo.entity.Cart;

import java.util.List;

public interface CartMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Cart record);

    int insertSelective(Cart record);

    Cart selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Cart record);

    int updateByPrimaryKey(Cart record);

    List<Cart> findByUserId(String userId);

    Cart findByUserIdAndProductId(@Param("userId") String userId,
                                  @Param("productId") Integer productId);

    int deleteByUserIdAndProductId(@Param("userId") String userId,
                                   @Param("productId") Integer productId);

    Integer count(String userId);
}