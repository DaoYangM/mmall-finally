package top.daoyang.demo.payload.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
public class ProductCreateRequest {

    @NotNull
    private Integer categoryId;

    @NotBlank
    private String  name;

    @NotBlank
    private String subtitle;

    private String subImages;

    @NotBlank
    private String detail;

    @NotNull
    private BigDecimal price;

    @NotNull
    private Integer stock;

    @NotNull
    private Integer status;
}
