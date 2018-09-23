package top.daoyang.demo.payload.reponse;

import lombok.Getter;
import lombok.Setter;
import top.daoyang.demo.entity.Shipping;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class OrderResponse {

    private Long orderNo;

    private BigDecimal payment;

    private Integer paymentType;

    private BigDecimal postage;

    private Integer totalCount;

    private Integer status;

    private Shipping shipping;

    private Date paymentTime;

    private Date sendTime;

    private Date endTime;

    private Date closeTime;

    private Date createTime;

    public List<OrderItem> orderItems = new ArrayList<>();

    @Getter
    @Setter
    public class OrderItem {
        private Long orderNo;

        private Integer productId;

        private String productName;

        private String productImage;

        private BigDecimal currentUnitPrice;

        private Integer quantity;

        private BigDecimal totalPrice;

        private Integer specifyId;

        private Date createTime;
    }
}
