package top.daoyang.demo.service;

import top.daoyang.demo.entity.Favorite;

public interface FavoriteService {
    boolean getFavoriteByUserIdAndProductId(String userId, Integer productId);

    Integer createFavoriteByUserIdAndProductId(String userId, Integer productId);

    Integer cancelFavoriteByUserIdAndProductId(String userId, Integer productId);
}
