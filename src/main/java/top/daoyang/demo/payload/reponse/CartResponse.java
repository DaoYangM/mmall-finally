package top.daoyang.demo.payload.reponse;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class CartResponse {

    private List<CartItemResponse> cartItemResponse;

    private boolean allChecked;

    private BigDecimal cartTotalPrice;
}
