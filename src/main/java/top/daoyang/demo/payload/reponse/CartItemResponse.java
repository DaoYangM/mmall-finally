package top.daoyang.demo.payload.reponse;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class CartItemResponse {
    private Integer id;

    private String userId;

    private Integer productId;

    private Integer quantity;

    private String productName;

    private String productSubtitle;

    private String productMainImage;

    private BigDecimal productPrice;

    private Integer productStatus;

    private BigDecimal productTotalPrice;

    private Integer productStock;

    private Integer productChecked;

    private Integer specifyId;

    private List<String> specifyDescItems;

    private String limitQuantity;

    public static void main(String[] args) {
        System.out.println(0.01 + 0.05);
        BigDecimal bigDecimal = BigDecimal.valueOf(0.01);
        BigDecimal bigDecimal2 = BigDecimal.valueOf(0.05);

        System.out.println(bigDecimal.add(bigDecimal2).doubleValue());
    }
}
