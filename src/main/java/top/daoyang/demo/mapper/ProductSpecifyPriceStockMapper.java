package top.daoyang.demo.mapper;

import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.type.JdbcType;
import top.daoyang.demo.entity.ProductSpecifyPriceStock;

public interface ProductSpecifyPriceStockMapper {

    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "product_id", property = "productId", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
            @Result(column = "specify_ids", property = "specifyIds", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(column = "specify_length", property = "specifyLength", javaType = Integer.class, jdbcType = JdbcType.INTEGER)
    })

    @Select("SELECT * FROM mmall_product_specify_price_stock WHERE product_id = #{param1} AND specify_ids = #{param2}")
    ProductSpecifyPriceStock getProductSpecifyPriceAndStock(Integer productId, String specifyIds);

    @Select("SELECT id,product_id,specify_ids,specify_length, price, stock  FROM mmall_product_specify_price_stock WHERE product_id = #{param1} AND id = #{param2}")
    ProductSpecifyPriceStock getProductSpecifyPASBySpecifyId(Integer productId, Integer specifyId);

    @Update("UPDATE mmall_product_specify_price_stock SET stock = #{param1} WHERE id = #{param2}")
    int updatePASStock(Integer newStock, Integer specifyId);
}
