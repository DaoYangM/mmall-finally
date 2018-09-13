package top.daoyang.demo.mapper;

import org.apache.ibatis.annotations.Param;
import top.daoyang.demo.entity.Order;

import java.util.List;

public interface OrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Order record);

    int insertSelective(Order record);

    Order selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Order record);

    int updateByPrimaryKey(Order record);

    Order findOrderByUserIdAndOrderNo(@Param("userId") String userId,
                                      @Param("orderNo") Long orderNo);

    List<Order> findOrderListByUserId(@Param("userId") String userId);

    Order findOrderByOrderNo(@Param("orderNo") Long orderNo);

    List<Order> getAll();
}