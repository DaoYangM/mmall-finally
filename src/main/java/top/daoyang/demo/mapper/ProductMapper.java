package top.daoyang.demo.mapper;

import org.apache.ibatis.annotations.Param;
import top.daoyang.demo.entity.Product;

import java.util.List;

public interface ProductMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Product record);

    int insertSelective(Product record);

    Product selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Product record);

    int updateByPrimaryKey(Product record);

    List<Product> findProduct(@Param("status") Integer status);

    Product findProductByProductId(@Param(value = "productId")Integer productId,
                                   @Param(value = "status") Integer status);

    List<Product> searchByKeyword(@Param(value = "q") String q,
                                  @Param(value = "status") Integer status);
}