package top.daoyang.demo.service;

import com.alipay.api.AlipayApiException;
import com.github.pagehelper.PageInfo;
import top.daoyang.demo.payload.reponse.OrderResponse;
import top.daoyang.demo.payload.request.PreCreateOrderRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;

public interface OrderService {

    String createOrder(HttpServletRequest request, String id, Integer shippingId) throws IOException;

    OrderResponse getOrderByOrderNo(String userId, Long orderNo);

    PageInfo getOrderList(String userId, Integer page, Integer size, String type);

    boolean cancelOrder(String userId, Long orderNo);

    String payOrder(HttpServletRequest httpRequest, String userId, Long orderNo) throws IOException;

    String aliPayNotify(HttpServletRequest httpServletRequest) throws AlipayApiException, ParseException;

    String preCreateOrder(HttpServletRequest httpRequest, HttpServletResponse httpResponse, String userId, PreCreateOrderRequest preCreateOrderRequest) throws IOException;

    boolean confirmReceipt(String userId, Long orderNo);

    boolean finishedOrder(String userId, Long orderNo);
}
