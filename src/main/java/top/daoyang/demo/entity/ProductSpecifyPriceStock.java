package top.daoyang.demo.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
public class ProductSpecifyPriceStock {
    private Integer id;

    private Integer productId;

    private String specifyIds;

    private Integer specifyLength;

    private BigDecimal price;

    private Integer stock;
}
