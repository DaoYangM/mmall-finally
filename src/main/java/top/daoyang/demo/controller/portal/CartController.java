package top.daoyang.demo.controller.portal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import top.daoyang.demo.enums.ProductStatusEnum;
import top.daoyang.demo.payload.ServerResponse;
import top.daoyang.demo.payload.request.CartCreateRequest;
import top.daoyang.demo.security.WXUserDetails;
import top.daoyang.demo.service.CartService;

import javax.validation.Valid;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping
    public ServerResponse getCart(@AuthenticationPrincipal WXUserDetails userPrincipal) {
        return ServerResponse.createBySuccess(cartService.getCartByUserId(userPrincipal.getId()));
    }

    @PostMapping
    public ServerResponse createCart(@AuthenticationPrincipal WXUserDetails userPrincipal,
                                     @Valid @RequestBody CartCreateRequest cartCreateRequest) {
        return ServerResponse.createBySuccess(cartService.createCart(userPrincipal.getId(), cartCreateRequest));
    }

    @PutMapping
    public ServerResponse updateCart(@AuthenticationPrincipal WXUserDetails userPrincipal,
                                    @Valid @RequestBody CartCreateRequest cartCreateRequest) {
        return ServerResponse.createBySuccess(cartService.updateCart(userPrincipal.getId(), cartCreateRequest));
    }

    @GetMapping("/count")
    public ServerResponse countOfCart(@AuthenticationPrincipal WXUserDetails userPrincipal) {
        return ServerResponse.createBySuccess(cartService.countOfCart(userPrincipal.getId()));
    }

    @DeleteMapping("/{productId}")
    public ServerResponse deleteCart(@AuthenticationPrincipal WXUserDetails userPrincipal,
                                     @PathVariable Integer productId) {
        return ServerResponse.createBySuccess(cartService.deleteCart(userPrincipal.getId(), productId));
    }

    @PatchMapping("/select/{productId}")
    public ServerResponse selectCart(@AuthenticationPrincipal WXUserDetails userPrincipal,
                                     @PathVariable Integer productId) {
        return ServerResponse.createBySuccess(cartService.selectCart(userPrincipal.getId(), productId, ProductStatusEnum.CHECKED.getValue()));
    }

    @PatchMapping("/unselected/{productId}")
    public ServerResponse unselectedCart(@AuthenticationPrincipal WXUserDetails userPrincipal,
                                     @PathVariable Integer productId) {
        return ServerResponse.createBySuccess(cartService.selectCart(userPrincipal.getId(), productId, ProductStatusEnum.UNCHECKED.getValue()));
    }

    @PutMapping("/selects")
    public ServerResponse selectAllCart(@AuthenticationPrincipal WXUserDetails userPrincipal) {
        return ServerResponse.createBySuccess(cartService.selectAllCart(userPrincipal.getId(), ProductStatusEnum.CHECKED.getValue()));
    }

    @PutMapping("/unselects")
    public ServerResponse unselectAllCart(@AuthenticationPrincipal WXUserDetails userPrincipal) {
        return ServerResponse.createBySuccess(cartService.selectAllCart(userPrincipal.getId(), ProductStatusEnum.UNCHECKED.getValue()));
    }
}
