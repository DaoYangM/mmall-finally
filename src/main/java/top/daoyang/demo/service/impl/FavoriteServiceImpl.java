package top.daoyang.demo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.daoyang.demo.entity.Favorite;
import top.daoyang.demo.enums.ExceptionEnum;
import top.daoyang.demo.exception.FavoriteException;
import top.daoyang.demo.mapper.FavoriteMapper;
import top.daoyang.demo.service.FavoriteService;

@Service
public class FavoriteServiceImpl implements FavoriteService {

    @Autowired
    private FavoriteMapper favoriteMapper;

    @Override
    public boolean getFavoriteByUserIdAndProductId(String userId, Integer productId) {
        return favoriteMapper.findFavoriteByUserIdAndProductId(userId, productId) != null;
    }

    @Override
    public Integer createFavoriteByUserIdAndProductId(String userId, Integer productId) {
        if (getFavoriteByUserIdAndProductId(userId, productId))
            throw new FavoriteException(ExceptionEnum.FAVORITE_HAS_EXISTED);

        if (favoriteMapper.createFavoriteByUserIdAndProductId(userId, productId) == 1) {
           return 1;
        }
        throw new FavoriteException(ExceptionEnum.FAVORITE_CREATE_EXCEPTION);
    }

    @Override
    public Integer cancelFavoriteByUserIdAndProductId(String userId, Integer productId) {
        if (!getFavoriteByUserIdAndProductId(userId, productId))
            throw new FavoriteException(ExceptionEnum.FAVORITE_DOES_NOT_EXIST);

        if (favoriteMapper.cancelFavoriteByUserIdAndProductId(userId, productId) == 1) {
            return 1;
        }
        throw new FavoriteException(ExceptionEnum.FAVORITE_CREATE_EXCEPTION);
    }
}
