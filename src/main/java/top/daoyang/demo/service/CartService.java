package top.daoyang.demo.service;

import top.daoyang.demo.payload.reponse.CartResponse;
import top.daoyang.demo.payload.request.CartCreateRequest;
import top.daoyang.demo.payload.request.CartDeleteRequest;
import top.daoyang.demo.payload.request.CartUpdateRequest;

public interface CartService {
    CartResponse getCartByUserId(String userId);

    CartResponse createCart(String userId, CartCreateRequest cartCreateRequest);

    CartResponse updateCart(String userId, CartUpdateRequest cartUpdateRequest);

    CartResponse deleteCart(String userId, CartDeleteRequest cartDeleteRequest);

    CartResponse selectCart(String userId, Integer productId, Integer isChecked);

    CartResponse selectAllCart(String userId, Integer isChecked);

    Integer countOfCart(String userId);

    CartResponse deleteAllSelectCart(String id);
}
