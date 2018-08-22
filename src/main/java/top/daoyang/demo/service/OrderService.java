package top.daoyang.demo.service;

import com.alipay.api.AlipayApiException;
import com.github.pagehelper.PageInfo;
import top.daoyang.demo.entity.Order;
import top.daoyang.demo.payload.reponse.OrderResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;

public interface OrderService {

    OrderResponse createOrder(Long id, Integer shippingId);

    OrderResponse getOrderByOrderNo(Long userId, Long orderNo);

    PageInfo<OrderResponse> getOrderList(Long userId, Integer page, Integer size);

    boolean cancelOrder(Long userId, Long orderNo);

    void payOrder(HttpServletRequest httpRequest, HttpServletResponse httpResponse, Long userId, Long orderNo) throws IOException;

    String aliPayNotify(HttpServletRequest httpServletRequest) throws AlipayApiException, ParseException;
}
