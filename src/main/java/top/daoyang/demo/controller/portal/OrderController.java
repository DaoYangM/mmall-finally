package top.daoyang.demo.controller.portal;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import top.daoyang.demo.payload.ServerResponse;
import top.daoyang.demo.security.UserPrincipal;
import top.daoyang.demo.service.OrderService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.text.ParseException;

import static com.alipay.api.AlipayConstants.*;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/{shippingId}")
    public ServerResponse createOrder(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                      @PathVariable Integer shippingId) {
        return ServerResponse.createBySuccess(orderService.createOrder(userPrincipal.getId(), shippingId));
    }
    @GetMapping("/pay/{orderNo}")
    public void pay(HttpServletRequest httpRequest, HttpServletResponse httpResponse,
//                    @AuthenticationPrincipal UserPrincipal userPrincipal,
                    @PathVariable Long orderNo) throws IOException {

        orderService.payOrder(httpRequest, httpResponse, 22L, orderNo);
    }

    @PostMapping("/alipay/notify")
    public String aliPayNotify(HttpServletRequest httpServletRequest) throws AlipayApiException, ParseException {
        return orderService.aliPayNotify(httpServletRequest);
    }

    @GetMapping("/{orderNo}")
    public ServerResponse getOrderByOrderNo(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                            @PathVariable Long orderNo) {
        return ServerResponse.createBySuccess(orderService.getOrderByOrderNo(userPrincipal.getId(), orderNo));
    }

    @GetMapping
    public ServerResponse getOrderList(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                       @RequestParam(value = "page", defaultValue = "0") Integer page,
                                       @RequestParam(value = "size", defaultValue = "10") Integer size) {
        return ServerResponse.createBySuccess(orderService.getOrderList(userPrincipal.getId(), page, size));
    }

    @PatchMapping("/cancel/{orderNo}")
    public ServerResponse cancelOrder(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                      @PathVariable Long orderNo) {
        return ServerResponse.createBySuccess(orderService.cancelOrder(userPrincipal.getId(), orderNo));
    }
}
