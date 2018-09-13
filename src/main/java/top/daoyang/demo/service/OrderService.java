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

    OrderResponse createOrder(String id, Integer shippingId);

    OrderResponse getOrderByOrderNo(String userId, Long orderNo);

    PageInfo<OrderResponse> getOrderList(String userId, Integer page, Integer size);

    boolean cancelOrder(String userId, Long orderNo);

    void payOrder(HttpServletRequest httpRequest, HttpServletResponse httpResponse, String userId, Long orderNo) throws IOException;

    String aliPayNotify(HttpServletRequest httpServletRequest) throws AlipayApiException, ParseException;
}
