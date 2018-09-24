package top.daoyang.demo.payload.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CartDeleteRequest {

    @NotNull
    private Integer productId;

    private Integer specifyId;
}