package top.daoyang.demo.controller.portal;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import top.daoyang.demo.payload.ServerResponse;
import top.daoyang.demo.payload.request.PreCreateOrderRequest;
import top.daoyang.demo.security.WXUserDetails;
import top.daoyang.demo.service.OrderService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import java.io.IOException;
import java.text.ParseException;

import static com.alipay.api.AlipayConstants.*;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/{shippingId}")
    public ServerResponse createOrder(@AuthenticationPrincipal WXUserDetails userPrincipal,
                                      @PathVariable Integer shippingId,
                                      HttpServletRequest request) throws IOException {
        return ServerResponse.createBySuccess(orderService.createOrder(request, userPrincipal.getId(), shippingId));
    }
    @PostMapping("/pay/{orderNo}")
    public ServerResponse pay(HttpServletRequest httpRequest,
                    @AuthenticationPrincipal WXUserDetails userPrincipal,
                    @PathVariable Long orderNo) throws IOException {

        return ServerResponse.createBySuccess(orderService.payOrder(httpRequest, userPrincipal.getId(), orderNo));
    }

    @PostMapping("/alipay/notify")
    public String aliPayNotify(HttpServletRequest httpServletRequest) throws AlipayApiException, ParseException {
        return orderService.aliPayNotify(httpServletRequest);
    }

    @GetMapping("/{orderNo}")
    public ServerResponse getOrderByOrderNo(@AuthenticationPrincipal WXUserDetails userPrincipal,
                                            @PathVariable Long orderNo) {
        return ServerResponse.createBySuccess(orderService.getOrderByOrderNo(userPrincipal.getId(), orderNo));
    }

    @GetMapping
    public ServerResponse getOrderList(@AuthenticationPrincipal WXUserDetails userPrincipal,
                                       @RequestParam(value = "page", defaultValue = "1") Integer page,
                                       @RequestParam(value = "size", defaultValue = "10") Integer size,
                                       @RequestParam(value = "type", defaultValue = "all") String type ) {
        return ServerResponse.createBySuccess(orderService.getOrderList(userPrincipal.getId(), page, size, type));
    }

    @PatchMapping("/cancel/{orderNo}")
    public ServerResponse cancelOrder(@AuthenticationPrincipal WXUserDetails userPrincipal,
                                      @PathVariable Long orderNo) {
        return ServerResponse.createBySuccess(orderService.cancelOrder(userPrincipal.getId(), orderNo));
    }

    @PostMapping("/pre/create")
    public ServerResponse preCreateOrder(@AuthenticationPrincipal WXUserDetails userDetails,
                                         @Valid @RequestBody PreCreateOrderRequest preCreateOrderRequest,
                                         HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws IOException {

        return ServerResponse.createBySuccess(orderService.preCreateOrder(httpRequest, httpResponse, userDetails.getId(), preCreateOrderRequest));
    }

    @PutMapping("/confirm/receipt/{orderNo}")
    public ServerResponse confirmReceipt(@AuthenticationPrincipal WXUserDetails userDetails,
                                         @PathVariable Long orderNo) {
        return ServerResponse.createBySuccess(orderService.confirmReceipt(userDetails.getId(), orderNo));
    }
}
