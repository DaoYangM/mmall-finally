package top.daoyang.demo.payload.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartUpdateRequest {
    private Integer productId;

    private Integer count;

    private Integer specifyId;
}
