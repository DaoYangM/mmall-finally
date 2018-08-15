package top.daoyang.demo.service;

import top.daoyang.demo.payload.reponse.CartResponse;
import top.daoyang.demo.payload.request.CartCreateRequest;

public interface CartService {
    CartResponse getCartByUserId(Long userId);

    CartResponse createCart(Long userId, CartCreateRequest cartCreateRequest);

    CartResponse updateCart(Long userId, CartCreateRequest cartCreateRequest);

    CartResponse deleteCart(Long userId, Integer productId);

    CartResponse selectCart(Long userId, Integer productId, Integer isChecked);

    CartResponse selectAllCart(Long userId, Integer isChecked);

    Integer countOfCart(Long userId);
}
