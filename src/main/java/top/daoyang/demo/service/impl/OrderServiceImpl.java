package top.daoyang.demo.service.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
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
import top.daoyang.demo.payload.request.PreCreateOrderRequest;
import top.daoyang.demo.service.CartService;
import top.daoyang.demo.service.OrderService;
import top.daoyang.demo.service.ProductService;
import top.daoyang.demo.service.ShippingService;
import top.daoyang.demo.util.AlipayQRCodeExpUtils;
import top.daoyang.demo.util.BigDecimalUtils;
import top.daoyang.demo.util.FtpUtils;
import top.daoyang.demo.util.MatrixToImageWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
    private ProductSpecifyPriceStockMapper productSpecifyPriceStockMapper;

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;

    @Value("${app.alipay.appId}")
    private String APP_ID;

    @Value("${app.alipay.privateKey}")
    private String APP_PRIVATE_KEY;

    @Value("${app.alipay.aliPublicKey}")
    private String ALIPAY_PUBLIC_KEY;

    @Value("${app.alipay.notifyUrl}")
    private String NOTIFY_URL;

    @Value("${app.natAppServer}")
    private String natAppServer;

    @Value("${app.ftp.username}")
    private String ftpUserName;

    @Value("${app.ftp.host}")
    private String ftpHost;

    @Value("${app.ftp.password}")
    private String ftpPassword;

    @Value("${app.ftp.uploadFold}")
    private String ftpUploadFold;

    @Value("${app.ftp.responseFold}")
    private String responseFold;

    @Value("${app.qrCode.height}")
    private String qrCodeHeight;

    @Value("${app.qrCode.width}")
    private String qrCodeWidth;

    @Value("${app.qrCode.format}")
    private String qrCodeFormat;

    @Value("${app.imageHost}")
    private String imageHost;

    @Override
    @Transactional
    public String createOrder(HttpServletRequest request, String userId, Integer shippingId) throws IOException {

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
                orderItem.setProductImage(cartItem.getProductImage());
                orderItem.setCurrentUnitPrice(cartItem.getPrice());
                orderItem.setQuantity(cartItem.getQuantity());
                orderItem.setTotalPrice(cartItem.getProductTotalPrice());
                orderItem.setSpecifyId(cartItem.getSpecifyId());

                orderTotalAmount[0] = orderTotalAmount[0].add(cartItem.getProductTotalPrice());

                orderItemMapper.insert(orderItem);

                Product product = Optional.ofNullable(productMapper.findProductByProductId(cartItem.getProductId(), ProductStatusEnum.ON_SALE.getValue()))
                        .orElseThrow(() -> new ProductException(ExceptionEnum.PRODUCT_DOES_NOT_EXIST));

                Integer stock = product.getStock();

                if (cartItem.getSpecifyId() != null) {
                    stock = Optional.ofNullable(productSpecifyPriceStockMapper.getProductSpecifyPASBySpecifyId(product.getId(), cartItem.getSpecifyId()))
                    .orElseThrow(() -> new ProductException(ExceptionEnum.PRODUCT_SPECIFY_DOES_EXIST)).getStock();
                }
                
                // delete product stock
                if (stock - cartItem.getQuantity() < 0)
                    throw new ProductException(ExceptionEnum.PRODUCT_OUT_OF_STOCK);

                log.info("Deleting product {} stock {} success", product.getName(), cartItem.getQuantity());
                product.setStock(stock - cartItem.getQuantity());
                if (productMapper.updateByPrimaryKeySelective(product) != 1)
                    throw new ProductException(ExceptionEnum.PRODUCT_UPDATE_STOCK_ERROR);

                if (cartMapper.deleteByUserIdAndProductIdAndSpecifyId(userId, cartItem.getProductId(), cartItem.getSpecifyId()) != 1) {
                    throw new CartException(ExceptionEnum.CART_CLEAN_ERROR);
                }
                log.info("Cleaning cart's product {} status {} success", product.getName(), product.getStatus());
            }
        });


        order.setPayment(orderTotalAmount[0]);
        //TODO Bulk insert

        orderMapper.insertSelective(order);
        log.info("Create order success");

        return payOrder(request, userId, order.getOrderNo());
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
            orderItemResponse.setSpecifyId(orderItem.getSpecifyId());
            orderItemResponse.setCreateTime(order.getCreateTime());

            totalCount.updateAndGet(v -> v + orderItem.getQuantity());

            orderResponse.orderItems.add(orderItemResponse);
        });

        orderResponse.setShipping(shippingService.getShippingByShippingId(userId, order.getShippingId()));
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
                payStatus = OrderStatusEnum.PAID.getCode();
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
    public String payOrder(HttpServletRequest httpRequest, String userId, Long orderNo) throws IOException {
        Order order = Optional.ofNullable(orderMapper.findOrderByUserIdAndOrderNo(userId, orderNo))
                .orElseThrow(() -> new OrderException(ExceptionEnum.ORDER_DOES_NOT_EXIST));

        switch (OrderStatusEnum.getStatus(order.getStatus())) {
            case ORDER_CANCEL:
                throw new OrderException(ExceptionEnum.ORDER_HAS_BEEN_CANCELED);
            case PAID:
                throw new OrderException(ExceptionEnum.ORDER_HAS_BEEN_PAID);
            case ORDER_SUCCESS:
                throw new OrderException(ExceptionEnum.ORDER_HAS_BEEN_SUCCESS);
        }

        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipaydev.com/gateway.do", APP_ID, APP_PRIVATE_KEY, "json", "utf-8", ALIPAY_PUBLIC_KEY, "RSA2"); //获得初始化的AlipayClient

        AlipayTradePrecreateRequest alipayRequest = new AlipayTradePrecreateRequest ();
        alipayRequest.setReturnUrl("http://daoyang.natapp1.cc/order/alipay/notify");
        alipayRequest.setNotifyUrl(NOTIFY_URL + "/order/alipay/notify");//在公共参数中设置回跳和通知地址
        alipayRequest.setBizContent("{" +
                "    \"out_trade_no\":\"" + order.getOrderNo()+ "\"," +
                "    \"total_amount\":" + order.getPayment() + "," +
                "    \"subject\":\"" + order.getOrderNo()+"\"" +  "," +
                "    \"store_id\":\"NJ_001\"," +
                "    \"timeout_express\":\"90m\"}");//
        AlipayTradePrecreateResponse response = null;
        try {
            response = alipayClient.execute(alipayRequest);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }

        String body = response.getBody();
        log.info(body);
        String localUploadPath = httpRequest.getServletContext().getRealPath("upload");
        File localFold = new File(localUploadPath);
        if (!localFold.exists()) {
            localFold.setWritable(true);
            localFold.mkdirs();
            log.info("Creating fold {}", localUploadPath);
        }
        String qrCodeAddress = AlipayQRCodeExpUtils.qrCodeAddress(body);
        log.info("qrCodeAddress {}", qrCodeAddress);

        String uuId = UUID.randomUUID().toString();

        File imgOutFile = MatrixToImageWriter.generateCode(qrCodeAddress, localFold.getAbsolutePath(), Integer.parseInt(qrCodeHeight), Integer.parseInt(qrCodeWidth), uuId, qrCodeFormat);

        try {
            FtpUtils.upload(ftpHost,imgOutFile.getName(), new FileInputStream(imgOutFile), ftpUserName, ftpPassword, ftpUploadFold);
            if (imgOutFile.setWritable(true)) {
                log.info("Setting imgFile writable true");
            }
            imgOutFile.setExecutable(true);
            if (imgOutFile.delete()) {
                log.info("Local image deleted");
            } else {
                log.info("Local image deleted failure");
            }
        } catch (SftpException | FileNotFoundException | JSchException e) {
            e.printStackTrace();
        }

        log.info("All processing of alipay has been finished");
        return imageHost + "/" +  responseFold +"/" + uuId + ".jpg";
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

    @Override
    public String preCreateOrder(HttpServletRequest httpRequest, HttpServletResponse httpResponse, String userId, PreCreateOrderRequest preCreateOrderRequest) throws IOException {
        Product product = productService.findProductByProductId(preCreateOrderRequest.getProductId(), ProductStatusEnum.ON_SALE.getValue());

        BigDecimal totalPrice = new BigDecimal("0");

        if (preCreateOrderRequest.getSpecifyId() != null) {
            ProductSpecifyPriceStock productSpecifyPriceStock = Optional.ofNullable(productSpecifyPriceStockMapper.getProductSpecifyPASBySpecifyId(preCreateOrderRequest.getProductId(), preCreateOrderRequest.getSpecifyId()))
                    .orElseThrow(() -> new ProductException(ExceptionEnum.PRODUCT_SPECIFY_DOES_EXIST));
            totalPrice = checkPriceAndStock(productSpecifyPriceStock.getPrice(), productSpecifyPriceStock.getStock(), preCreateOrderRequest.getCount());

            productSpecifyPriceStockMapper.updatePASStock(productSpecifyPriceStock.getStock() - preCreateOrderRequest.getCount(),
                    preCreateOrderRequest.getSpecifyId());
        } else {
            totalPrice = checkPriceAndStock(product.getPrice(), product.getStock(), preCreateOrderRequest.getCount());
            product.setStock(product.getStock() - preCreateOrderRequest.getCount());
            productMapper.updateByPrimaryKeySelective(product);
        }

        Shipping shipping = shippingService.getShippingByShippingId(userId, preCreateOrderRequest.getShippingId());

        Long orderNO = generateOrderNumber();
        Order order = new Order();

        order.setOrderNo(orderNO);
        order.setUserId(userId);
        order.setShippingId(shipping.getId());
        order.setPayment(totalPrice);
        order.setPaymentType(1);
        order.setStatus(OrderStatusEnum.NO_PAY.getCode());
        order.setPostage(0);

         if (orderMapper.insertSelective(order) == 1) {

             OrderItem orderItem = new OrderItem();

             orderItem.setUserId(userId);
             orderItem.setOrderNo(order.getOrderNo());
             orderItem.setSpecifyId(preCreateOrderRequest.getSpecifyId());
             orderItem.setProductId(product.getId());
             orderItem.setProductName(product.getName());
             orderItem.setProductImage(product.getMainImage());
             orderItem.setCurrentUnitPrice(product.getPrice());
             orderItem.setQuantity(preCreateOrderRequest.getCount());
             orderItem.setTotalPrice(totalPrice);

             if (orderItemMapper.insertSelective(orderItem) == 1) {
                 return payOrder(httpRequest, userId, orderNO);
             } else {
                 throw new OrderException(ExceptionEnum.ORDER_ITEM_CREATE_ERROR);
             }

         } else {
            throw new OrderException(ExceptionEnum.ORDER_CREATE_ERROR);
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

    private BigDecimal checkPriceAndStock(BigDecimal price, Integer stock, Integer count) {
        if (count > stock) {
            throw new OrderException(ExceptionEnum.ORDER_CREATE_OUT_OF_STOCK_ERROR);
        }
        return BigDecimalUtils.mul(price.doubleValue(), count);
    }
}
