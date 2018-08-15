package top.daoyang.demo.payload.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CartCreateRequest {

    @NotNull
    private Integer productId;

    @NotNull
    private Integer count;

    public CartCreateRequest(@NotNull Integer productId, @NotNull Integer count) {
        this.productId = productId;
        this.count = count;
    }

    public CartCreateRequest() {
    }
}
