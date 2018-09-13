package top.daoyang.demo.mapper;

import org.apache.ibatis.annotations.Select;
import top.daoyang.demo.entity.ProductSpecifyItem;

import java.util.List;

public interface ProductSpecifyItemMapper {

    @Select("SELECT * FROM mmall_product_specify_item WHERE specify_id = #{specifyId}")
    List<ProductSpecifyItem> getProductSpecifyItemList(Integer specifyId);

    @Select("SELECT * FROM mmall_product_specify_item WHERE id = #{id}")
    ProductSpecifyItem getProductSpecifyItemById(Integer id);
}
