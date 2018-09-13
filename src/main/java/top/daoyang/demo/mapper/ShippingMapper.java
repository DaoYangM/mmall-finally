package top.daoyang.demo.mapper;

import org.apache.ibatis.annotations.Param;
import top.daoyang.demo.entity.Shipping;

import java.util.List;

public interface ShippingMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Shipping record);

    int insertSelective(Shipping record);

    Shipping selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Shipping record);

    int updateByPrimaryKey(Shipping record);

    List<Shipping> findByUserId(String userId);

    int deleteByShippingIdAndUserId(@Param("userId") String userId,
                                    @Param("shippingId") Integer shippingId);

    Shipping findShippingByUserId(@Param("userId")String userId,
                                  @Param("shippingId")Integer shippingId);
}