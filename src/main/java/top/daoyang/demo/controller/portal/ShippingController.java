package top.daoyang.demo.controller.portal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import top.daoyang.demo.enums.ExceptionEnum;
import top.daoyang.demo.exception.ShippingException;
import top.daoyang.demo.payload.ServerResponse;
import top.daoyang.demo.payload.request.ShippingCreateRequest;
import top.daoyang.demo.security.WXUserDetails;
import top.daoyang.demo.service.ShippingService;

import javax.validation.Valid;

@RestController
@RequestMapping("/shipping")
public class ShippingController {

    @Autowired
    private ShippingService shippingService;

    @GetMapping
    public ServerResponse getShippingList(@AuthenticationPrincipal WXUserDetails userPrincipal,
                                          @RequestParam(value = "page", defaultValue = "1") Integer page,
                                          @RequestParam(value = "size", defaultValue = "10") Integer size) {

        return ServerResponse.createBySuccess(shippingService.getShippingList(userPrincipal.getId(), page, size));
    }

    @GetMapping("/checked")
    public ServerResponse getCheckShipping(@AuthenticationPrincipal WXUserDetails userPrincipal) {
        return ServerResponse.createBySuccess(shippingService.getCheckShipping(userPrincipal.getId()));
    }

    @PostMapping
    public ServerResponse createShipping(@AuthenticationPrincipal WXUserDetails userPrincipal,
                                         @Valid @RequestBody ShippingCreateRequest shippingCreateRequest) {
        return ServerResponse.createBySuccess(shippingService.createShipping(userPrincipal.getId(), shippingCreateRequest));
    }

    @GetMapping("/{shippingId}")
    public ServerResponse getShippingByShippingId(@PathVariable Integer shippingId,
                                                  @AuthenticationPrincipal WXUserDetails userPrincipal) {
        return ServerResponse.createBySuccess(shippingService.getShippingByShippingId(userPrincipal.getId(), shippingId));
    }

    @DeleteMapping("/{shippingId}")
    public ServerResponse deleteShippingByShippingId(
                                            @AuthenticationPrincipal WXUserDetails userPrincipal,
                                            @PathVariable Integer shippingId) {
        if (shippingService.deleteShippingByShippingId(userPrincipal.getId(), shippingId))
            return ServerResponse.createBySuccess(true);
        throw new ShippingException(ExceptionEnum.SHIPPING_DELETE_ERROR);
    }

    @PutMapping("/{shippingId}/checked")
    public ServerResponse changeChecked(@AuthenticationPrincipal WXUserDetails userPrincipal,
            @PathVariable Integer shippingId) {
        return ServerResponse.createBySuccess(shippingService.changeChecked(userPrincipal.getId(), shippingId));
    }

    @PutMapping("/{shippingId}")
    public ServerResponse patchShippingByShippingId(@PathVariable Integer shippingId,
                                                    @AuthenticationPrincipal WXUserDetails userPrincipal,
                                                    @Valid @RequestBody ShippingCreateRequest shippingCreateRequest) {
        return ServerResponse.createBySuccess(shippingService.patchShippingByShippingId(userPrincipal.getId(),shippingId, shippingCreateRequest));
    }
}
