package top.daoyang.demo.mapper;

import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import top.daoyang.demo.entity.ProductParamter;

import java.util.List;

public interface ProductParameterMapper {

    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "product_id", property = "productId"),
            @Result(column = "parameter_name", property = "parameterName"),
            @Result(column = "parameter_value", property = "parameterValue")
    })

    @Select("SELECT * FROM mmall_product_parameter WHERE product_id = #{productId}")
    List<ProductParamter> getProductParameterByProductId(Integer productId);
}
