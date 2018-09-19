package top.daoyang.demo.service.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.daoyang.demo.entity.*;
import top.daoyang.demo.enums.*;
import top.daoyang.demo.exception.CartException;
import top.daoyang.demo.exception.OrderException;
import top.daoyang.demo.exception.ProductException;
import top.daoyang.demo.mapper.*;
import top.daoyang.demo.payload.reponse.CartResponse;
import top.daoyang.demo.payload.reponse.OrderResponse;
import top.daoyang.demo.service.CartService;
import top.daoyang.demo.service.OrderService;
import top.daoyang.demo.service.ShippingService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static top.daoyang.demo.enums.OrderStatusEnum.*;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private PayInfoMapper payInfoMapper;

    @Autowired
    private ShippingService shippingService;

    @Autowired
    private CartService cartService;

    @Value("${app.alipay.appId}")
    private String APP_ID;

    @Value("${app.alipay.privateKey}")
    private String APP_PRIVATE_KEY;

    @Value("${app.alipay.aliPublicKey}")
    private String ALIPAY_PUBLIC_KEY;

    @Value("${app.alipay.notifyUrl}")
    private String NOTIFY_URL;

    @Override
    @Transactional
    public OrderResponse createOrder(String userId, Integer shippingId) {

        Shipping shipping = shippingService.getShippingByShippingId(userId, shippingId);

        CartResponse cartResponse = cartService.getCartByUserId(userId);
        if (cartResponse.getCartItemResponse().size() == 0)
            throw new CartException(ExceptionEnum.CART_IS_EMPTY);

        Order order = new Order();

        final BigDecimal[] orderTotalAmount = {new BigDecimal("0")};

        // generate order number
        order.setOrderNo(generateOrderNumber());
        order.setUserId(userId);
        order.setShippingId(shipping.getId());
        order.setPaymentType(PaymentTypeEnum.ALIPAY.getValue());
        order.setPostage(0);

        order.setStatus(NO_PAY.getCode());

        // set order item
        cartResponse.getCartItemResponse().forEach(cartItem -> {
            if (cartItem.getProductChecked() == ProductStatusEnum.CHECKED.getValue()) {
                OrderItem orderItem = new OrderItem();
                orderItem.setUserId(userId);
                orderItem.setOrderNo(order.getOrderNo());
                orderItem.setProductId(cartItem.getProductId());
                orderItem.setProductName(cartItem.getProductName());
                orderItem.setProductImage(cartItem.getProductMainImage());
                orderItem.setCurrentUnitPrice(cartItem.getProductPrice());
                orderItem.setQuantity(cartItem.getQuantity());
                orderItem.setTotalPrice(cartItem.getProductTotalPrice());

                orderTotalAmount[0] = orderTotalAmount[0].add(cartItem.getProductTotalPrice());

                orderItemMapper.insert(orderItem);

                Product product = Optional.ofNullable(productMapper.findProductByProductId(cartItem.getProductId(), ProductStatusEnum.ON_SALE.getValue()))
                        .orElseThrow(() -> new ProductException(ExceptionEnum.PRODUCT_DOES_NOT_EXIST));

                // delete product stock
                if (product.getStock() - cartItem.getQuantity() < 0)
                    throw new ProductException(ExceptionEnum.PRODUCT_OUT_OF_STOCK);

                log.info("Deleting product {} stock {} success", product.getName(), cartItem.getQuantity());
                product.setStock(product.getStock() - cartItem.getQuantity());
                if (productMapper.updateByPrimaryKeySelective(product) != 1)
                    throw new ProductException(ExceptionEnum.PRODUCT_UPDATE_STOCK_ERROR);

                if (cartMapper.deleteByUserIdAndProductId(userId, cartItem.getProductId()) != 1) {
                    throw new CartException(ExceptionEnum.CART_CLEAN_ERROR);
                }
                log.info("Cleaning cart's product {} status {} success", product.getName(), product.getStatus());
            }
        });

        order.setPayment(orderTotalAmount[0]);
        //TODO Bulk insert

        orderMapper.insertSelective(order);
        log.info("Create order success");

        return getOrderByOrderNo(userId, order.getOrderNo());
    }

    @Override
    public OrderResponse getOrderByOrderNo(String userId, Long orderNo) {
        Order order = Optional.ofNullable(orderMapper.findOrderByUserIdAndOrderNo(userId, orderNo))
                .orElseThrow(() -> new OrderException(ExceptionEnum.ORDER_DOES_NOT_EXIST));

        OrderResponse orderResponse = new OrderResponse();

        BeanUtils.copyProperties(order, orderResponse);

        AtomicReference<Integer> totalCount = new AtomicReference<>(0);

        List<OrderItem> orderItemList = orderItemMapper.findOrderItemListByUserIdANDOrderNo(userId, orderNo);
        orderItemList.forEach(orderItem -> {
            OrderResponse.OrderItem orderItemResponse = orderResponse.new OrderItem();
            orderItemResponse.setOrderNo(orderNo);
            orderItemResponse.setProductId(orderItem.getProductId());
            orderItemResponse.setProductName(orderItem.getProductName());
            orderItemResponse.setProductImage(orderItem.getProductImage());
            orderItemResponse.setQuantity(orderItem.getQuantity());
            orderItemResponse.setCurrentUnitPrice(orderItem.getCurrentUnitPrice());
            orderItemResponse.setTotalPrice(orderItem.getTotalPrice());
            orderItemResponse.setCreateTime(order.getCreateTime());

            totalCount.updateAndGet(v -> v + orderItem.getQuantity());

            orderResponse.orderItems.add(orderItemResponse);
        });

//        orderResponse.setShipping(shippingService.getShippingByShippingId(userId, order.getShippingId()));
        orderResponse.setTotalCount(totalCount.get());
        return orderResponse;
    }

    @Override
    public PageInfo getOrderList(String userId, Integer page, Integer size, String type) {
        Integer payStatus;

        switch (type) {
            case "wait_pay":
                payStatus = OrderStatusEnum.NO_PAY.getCode();
                break;
            case "wait_delivery":
                payStatus = OrderStatusEnum.WAIT_DELIVERY.getCode();
                break;
            case "wait_receipt":
                payStatus = OrderStatusEnum.WAIT_RECEIPT.getCode();
                break;
            case "wait_comment":
                payStatus = OrderStatusEnum.WAIT_COMMENT.getCode();
                break;

            default:
                payStatus = null;
        }
        PageHelper.startPage(page, size);
        List<Order> orderList = orderMapper.findOrderListByUserId(userId, payStatus);
        PageInfo pageInfo = new PageInfo<>(orderList);
        List<OrderResponse> orderResponseList = orderList.stream().map(order -> getOrderByOrderNo(userId, order.getOrderNo())).collect(Collectors.toList());
        pageInfo.setList(orderResponseList);

        return pageInfo;
    }

    @Override
    @Transactional
    public boolean cancelOrder(String userId, Long orderNo) {
        Order order = Optional.ofNullable(orderMapper.findOrderByUserIdAndOrderNo(userId, orderNo))
                .orElseThrow(() -> new OrderException(ExceptionEnum.ORDER_DOES_NOT_EXIST));


        //TODO status condition
        if (order.getStatus().equals(ORDER_CANCEL.getCode()))
            throw new OrderException(ExceptionEnum.ORDER_HAS_BEEN_CANCELED);

        order.setStatus(ORDER_CANCEL.getCode());
        if (orderMapper.updateByPrimaryKeySelective(order) != 1) {
            throw new OrderException(ExceptionEnum.ORDER_DOES_NOT_EXIST);
        }

        return true;
    }

    @Override
    public void payOrder(HttpServletRequest httpRequest, HttpServletResponse httpResponse, String userId, Long orderNo) throws IOException {
        Order order = Optional.ofNullable(orderMapper.findOrderByUserIdAndOrderNo(userId, orderNo))
                .orElseThrow(() -> new OrderException(ExceptionEnum.ORDER_DOES_NOT_EXIST));

        switch (OrderStatusEnum.getStatus(order.getStatus())) {
            case ORDER_CANCEL:
                throw new OrderException(ExceptionEnum.ORDER_HAS_BEEN_CANCELED);
            case PAID:
                throw new OrderException(ExceptionEnum.ORDER_HAS_BEEN_PAID);
            case ORDER_CLOSE:
                throw new OrderException(ExceptionEnum.ORDER_HAS_BEEN_CANCELED);
            case ORDER_SUCCESS:
                throw new OrderException(ExceptionEnum.ORDER_HAS_BEEN_SUCCESS);
        }

        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipaydev.com/gateway.do", APP_ID, APP_PRIVATE_KEY, "json", "utf-8", ALIPAY_PUBLIC_KEY, "RSA2"); //获得初始化的AlipayClient
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();//创建API对应的request
//        AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();
        alipayRequest.setReturnUrl("http://daoyang.natapp1.cc/order/alipay/notify");
        alipayRequest.setNotifyUrl(NOTIFY_URL + "/order/alipay/notify");//在公共参数中设置回跳和通知地址
        alipayRequest.setBizContent("{" +
                "    \"out_trade_no\":\"" + order.getOrderNo()+ "\"," +
                "    \"product_code\":\"FAST_INSTANT_TRADE_PAY\"," +
                "    \"total_amount\":" + order.getPayment() + "," +
                "    \"subject\":\"" + order.getOrderNo()+"\"" +
                "  }");//填充业务参数
        String form = "";
        try {
            form = alipayClient.pageExecute(alipayRequest).getBody(); //调用SDK生成表单
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        httpResponse.setContentType("text/html;charset=" + "utf-8");
        httpResponse.getWriter().write(form);//直接将完整的表单html输出到页面
        httpResponse.getWriter().flush();
        httpResponse.getWriter().close();
    }

    @Override
    @Transactional
    public String aliPayNotify(HttpServletRequest httpServletRequest) throws AlipayApiException, ParseException {
        Map<String, String[]> _paramsMap = httpServletRequest.getParameterMap(); //将异步通知中收到的所有参数都存放到map中
        Map<String, String> paramsMap = new HashMap<>();

        log.error("ININININININININININI");
        _paramsMap.keySet().forEach(key -> {
            String[] values = _paramsMap.get(key);

            StringBuilder result = new StringBuilder();
            for (int i = 0; i < values.length; i++) {
                result = (i != values.length - 1)  ? result.append(values[i]).append( ",") :  result.append(values[i]);
            }

            paramsMap.put(key, result.toString());
        });
        paramsMap.remove("sign_type");
        boolean signVerified = AlipaySignature.rsaCheckV2(paramsMap, ALIPAY_PUBLIC_KEY, "UTF-8", "RSA2"); //调用SDK验证签名
        if(signVerified){
            // TODO 验签成功后，按照支付结果异步通知中的描述，对支付结果中的业务内容进行二次校验，校验成功后在response中返回success并继续商户自身业务处理，校验失败返回failure
            // TODO modify order status
            String outTradeNo = paramsMap.get("trade_no");
            String trade_status = paramsMap.get("trade_status");
            String orderNo = paramsMap.get("out_trade_no");

            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date paymentTime = format.parse(paramsMap.get("gmt_create"));

            Order order = Optional.ofNullable(orderMapper.findOrderByOrderNo(Long.parseLong(orderNo)))
                    .orElseThrow(() -> new OrderException(ExceptionEnum.ORDER_DOES_NOT_EXIST));

            if (order.getStatus() != OrderStatusEnum.NO_PAY.getCode())
                throw new OrderException(ExceptionEnum.ALIPAY_REPEATED_CALL);

            if (! trade_status.equals(AlipayTradeStatusEnum.TRADE_SUCCESS.getMsg()))
                throw new OrderException(ExceptionEnum.ALIPAY_TRADE_STATUS_ERROR);

            order.setStatus(OrderStatusEnum.PAID.getCode());
            order.setPaymentTime(paymentTime);
            if (orderMapper.updateByPrimaryKeySelective(order) != 1)
                throw new OrderException(ExceptionEnum.ORDER_STATUS_UPDATE_ERROR);

            PayInfo payInfo = new PayInfo();
            payInfo.setOrderNo(order.getOrderNo());
            payInfo.setUserId(order.getUserId());
            payInfo.setPayPlatform(1);
            payInfo.setPlatformNumber(outTradeNo);
            payInfo.setPlatformStatus(trade_status);

            payInfo.setCreateTime(paymentTime);
            if (payInfoMapper.insertSelective(payInfo) == 1) {
                log.info("Alipay notify callback success orderNo {}", order.getOrderNo());
                return "success";
            }

            return "failure";
        }else{
            // TODO 验签失败则记录异常日志，并在response中返回failure.
            return "failure";
        }
    }

    private Long generateOrderNumber() {
        Long currentTime = System.currentTimeMillis();
        return currentTime + new Random().nextInt(100);
    }

    private Order getOrderByOrderId(String userId, Long orderNo) {
         return Optional.ofNullable(orderMapper.findOrderByUserIdAndOrderNo(userId, orderNo))
                .orElseThrow(() -> new OrderException(ExceptionEnum.ORDER_DOES_NOT_EXIST));
    }
}
