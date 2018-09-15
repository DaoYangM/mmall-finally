package top.daoyang.demo.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import top.daoyang.demo.entity.Favorite;

public interface FavoriteMapper {

    @Select("SELECT * FROM mmall_favorite WHERE user_id = #{param1} AND product_id = #{param2}")
    Favorite findFavoriteByUserIdAndProductId(String userId, Integer productId);

    @Insert("INSERT INTO mmall_favorite (user_id, product_id) VALUES (#{param1}, #{param2})")
    Integer createFavoriteByUserIdAndProductId(String userId, Integer productId);

    @Delete("DELETE FROM mmall_favorite WHERE user_id = #{param1} AND product_id = #{param2}")
    Integer cancelFavoriteByUserIdAndProductId(String userId, Integer productId);
}
