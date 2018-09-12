package top.daoyang.demo.mapper;

import org.apache.ibatis.annotations.Select;
import top.daoyang.demo.entity.ProductSpecifyPriceStock;

public interface ProductSpecifyPriceStockMapper {

    @Select("SELECT * FROM mmall_product_specify_price_stock WHERE product_id = #{param1} AND specify_ids = #{param2}")
    ProductSpecifyPriceStock getProductSpecifyPriceAndStock(Integer productId, String specifyIds);
}
