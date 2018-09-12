package top.daoyang.demo.payload.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ProductSpecifyPriceAndStockRequest {

    @NotNull
    private Integer productId;

    @NotBlank
    private String specifyIds;
}
