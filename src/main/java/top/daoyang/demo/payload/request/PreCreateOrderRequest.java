package top.daoyang.demo.payload.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class PreCreateOrderRequest {

    @NotNull
    private Integer productId;

    @NotNull
    private Integer count;

    private Integer specifyId;

    @NotNull
    private Integer shippingId;
}
