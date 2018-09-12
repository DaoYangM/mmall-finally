package top.daoyang.demo.mapper;

import org.apache.ibatis.annotations.Select;
import top.daoyang.demo.entity.ProductSpecify;

import java.util.List;

public interface ProductSpecifyMapper {

    @Select("SELECT * FROM mmall_product_specify WHERE product_id = #{productId}")
    List<ProductSpecify> getSpecifyByProductId(Integer productId);
}
