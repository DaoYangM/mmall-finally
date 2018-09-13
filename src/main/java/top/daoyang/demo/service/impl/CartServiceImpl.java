package top.daoyang.demo.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.daoyang.demo.entity.Cart;
import top.daoyang.demo.entity.Product;
import top.daoyang.demo.entity.ProductSpecifyItem;
import top.daoyang.demo.entity.ProductSpecifyPriceStock;
import top.daoyang.demo.enums.ExceptionEnum;
import top.daoyang.demo.enums.ProductStatusEnum;
import top.daoyang.demo.exception.CartException;
import top.daoyang.demo.exception.ProductException;
import top.daoyang.demo.mapper.CartMapper;
import top.daoyang.demo.mapper.ProductMapper;
import top.daoyang.demo.mapper.ProductSpecifyItemMapper;
import top.daoyang.demo.mapper.ProductSpecifyPriceStockMapper;
import top.daoyang.demo.payload.reponse.CartItemResponse;
import top.daoyang.demo.payload.reponse.CartResponse;
import top.daoyang.demo.payload.request.CartCreateRequest;
import top.daoyang.demo.service.CartService;
import top.daoyang.demo.util.BigDecimalUtils;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductSpecifyItemMapper productSpecifyItemMapper;

    @Autowired
    private ProductSpecifyPriceStockMapper productSpecifyPriceStockMapper;

    @Override
    public CartResponse getCartByUserId(String userId) {
        List<Cart> cartList = cartMapper.findByUserId(userId);
        CartResponse cartResponse = new CartResponse();
        AtomicBoolean allChecked = new AtomicBoolean(true);

        final BigDecimal[] cartTotalPrice = {new BigDecimal("0")};
        cartResponse.setCartItemResponse(cartList.stream().map(cart -> {
            CartItemResponse cartItemResponse = new CartItemResponse();
            cartItemResponse.setId(cart.getId());
            cartItemResponse.setUserId(userId);
            cartItemResponse.setQuantity(cart.getQuantity());

            assemblyProduct(cartItemResponse, cart.getProductId(), cart.getSpecifyId());

            cartItemResponse.setProductTotalPrice(
                    BigDecimalUtils.mul(cartItemResponse.getQuantity(), cartItemResponse.getProductPrice().doubleValue()));
            cartTotalPrice[0] = cartTotalPrice[0].add(cartItemResponse.getProductTotalPrice());
            cartItemResponse.setProductChecked(cart.getChecked());

            if (cartItemResponse.getProductChecked() != 1)
                allChecked.set(false);

            return cartItemResponse;
        }).collect(Collectors.toList()));

        cartResponse.setCartTotalPrice(cartTotalPrice[0]);
        cartResponse.setAllChecked(allChecked.get());

        return cartResponse;
    }

    @Override
    @Transactional
    public CartResponse createCart(String userId, CartCreateRequest cartCreateRequest) {
        Cart cart = new Cart();

        Product product = Optional.ofNullable(productMapper.findProductByProductId(cartCreateRequest.getProductId(),
                ProductStatusEnum.ON_SALE.getValue()))
                    .orElseThrow(() -> new ProductException(ExceptionEnum.PRODUCT_DOES_NOT_EXIST));


        Cart existCart = cartMapper.findByUserIdAndProductId(userId, product.getId());
        if (existCart != null) {
            existCart.setQuantity(existCart.getQuantity() + cartCreateRequest.getCount());
            BeanUtils.copyProperties(existCart, cart);
        } else {
            cart.setQuantity(cartCreateRequest.getCount());
        }

        if (product.getStock() < cart.getQuantity())
            throw new ProductException(ExceptionEnum.PRODUCT_OUT_OF_STOCK);

        if (existCart != null) {
            return updateCart(userId, new CartCreateRequest(cart.getProductId() ,cart.getQuantity()));
        }

        cart.setUserId(userId);
        cart.setProductId(product.getId());
        cart.setChecked(1);

        if (cartMapper.insertSelective(cart) == 1) {
            return getCartByUserId(userId);
        }
        throw new CartException(ExceptionEnum.CART_CREATE_ERROR);
    }

    @Override
    @Transactional
    public CartResponse updateCart(String userId, CartCreateRequest cartCreateRequest) {

        Product product = Optional.ofNullable(productMapper.findProductByProductId(cartCreateRequest.getProductId(),
                ProductStatusEnum.ON_SALE.getValue()))
                .orElseThrow(() -> new ProductException(ExceptionEnum.PRODUCT_DOES_NOT_EXIST));

        Cart cart = Optional.ofNullable(cartMapper.findByUserIdAndProductId(userId, product.getId()))
                .orElseThrow(() -> new CartException(ExceptionEnum.CART_DOES_NOT_EXIST));

        cart.setQuantity(cartCreateRequest.getCount());
        if (cartMapper.updateByPrimaryKeySelective(cart) == 1 ) {
            return getCartByUserId(userId);
        }

        throw new CartException(ExceptionEnum.CART_UPDATE_ERROR);
    }

    @Override
    @Transactional
    public CartResponse deleteCart(String userId, Integer productId) {
        cartMapper.deleteByUserIdAndProductId(userId, productId);
        return getCartByUserId(userId);
    }

    @Override
    @Transactional
    public CartResponse selectCart(String userId, Integer productId, Integer isChecked) {
        Cart cart = Optional.ofNullable(cartMapper.findByUserIdAndProductId(userId, productId))
                .orElseThrow(() -> new CartException(ExceptionEnum.CART_DOES_NOT_EXIST));

        cart.setChecked(isChecked);
        if (cartMapper.updateByPrimaryKeySelective(cart) == 1) {
            return getCartByUserId(userId);
        }
        throw new CartException(ExceptionEnum.CART_SELECT_ERROR);
    }

    @Override
    @Transactional
    public CartResponse selectAllCart(String userId, Integer isChecked) {
        List<Cart> cartList = cartMapper.findByUserId(userId);
        cartList.stream().forEach(cart -> {
            cart.setChecked(isChecked);
            if (cartMapper.updateByPrimaryKeySelective(cart) != 1)
                throw new CartException(ExceptionEnum.CART_SELECT_ALL_ERROR);
        });

        return getCartByUserId(userId);
    }

    @Override
    public Integer countOfCart(String userId) {
        return cartMapper.count(userId);
    }

    private void assemblyProduct(CartItemResponse cartItemResponse, Integer productId, Integer specifyId) {
        Product product = Optional.ofNullable(productMapper.findProductByProductId(productId, ProductStatusEnum.ON_SALE.getValue()))
                .orElseThrow(() -> new ProductException(ExceptionEnum.PRODUCT_DOES_NOT_EXIST));
        if (specifyId != null) {
            ProductSpecifyPriceStock productSpecifyPriceStock = Optional.ofNullable(productSpecifyPriceStockMapper.getProductSpecifyPASBySpecifyId(productId, specifyId))
                    .orElseThrow(() -> new CartException(ExceptionEnum.CART_SPECIFY_ID_DOES_NOT_EXIST));

            cartItemResponse.setSpecifyId(productSpecifyPriceStock.getId());
            productSpecifyPriceStock.getSpecifyIds().split("");
            cartItemResponse.setProductPrice(productSpecifyPriceStock.getPrice());
            cartItemResponse.setProductStock(productSpecifyPriceStock.getStock());

            List<String> ss = Arrays.stream(productSpecifyPriceStockMapper.getProductSpecifyPASBySpecifyId(productId, specifyId).getSpecifyIds().split("")).map(item -> productSpecifyItemMapper.getProductSpecifyItemById(Integer.parseInt(item)).getDescription()).collect(Collectors.toList());
            cartItemResponse.setSpecifyDescItems(ss);
            product.setStock(productSpecifyPriceStock.getStock());
        } else {
            cartItemResponse.setProductPrice(product.getPrice());
            cartItemResponse.setProductStock(product.getStock());

        }
        cartItemResponse.setProductId(product.getId());
        cartItemResponse.setProductName(product.getName());
        cartItemResponse.setProductSubtitle(product.getSubtitle());
        cartItemResponse.setProductMainImage(product.getMainImage());
        cartItemResponse.setProductStatus(product.getStatus());

        if (product.getStock() < cartItemResponse.getQuantity())
        {
            cartItemResponse.setLimitQuantity("LIMIT_NUM_FAIL");
            cartItemResponse.setQuantity(product.getStock());
        } else {
            cartItemResponse.setLimitQuantity("LIMIT_NUM_SUCCESS");
        }
    }
}
