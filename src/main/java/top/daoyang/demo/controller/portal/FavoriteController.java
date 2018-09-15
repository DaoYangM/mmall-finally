package top.daoyang.demo.controller.portal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import top.daoyang.demo.payload.ServerResponse;
import top.daoyang.demo.security.WXUserDetails;
import top.daoyang.demo.service.FavoriteService;

@RestController
@RequestMapping("/favorite")
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    @GetMapping("/{productId}")
    public ServerResponse getFavoriteByUserIdAndProductId(@AuthenticationPrincipal WXUserDetails wxUserDetails, @PathVariable Integer productId) {
        return ServerResponse.createBySuccess(favoriteService.getFavoriteByUserIdAndProductId(wxUserDetails.getId(), productId));
    }

    @PostMapping("/{productId}")
    public ServerResponse createFavoriteByUserIdAndProductId(@AuthenticationPrincipal WXUserDetails wxUserDetails, @PathVariable Integer productId) {
        return ServerResponse.createBySuccess(favoriteService.createFavoriteByUserIdAndProductId(wxUserDetails.getId(), productId));
    }

    @PutMapping("/{productId}")
    public ServerResponse cancelFavoriteByUserIdAndProductId(@AuthenticationPrincipal WXUserDetails wxUserDetails, @PathVariable Integer productId) {
        return ServerResponse.createBySuccess(favoriteService.cancelFavoriteByUserIdAndProductId(wxUserDetails.getId(), productId));
    }
}
