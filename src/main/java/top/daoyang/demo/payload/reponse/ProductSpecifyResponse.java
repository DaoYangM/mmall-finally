package top.daoyang.demo.payload.reponse;

import lombok.Getter;
import lombok.Setter;
import top.daoyang.demo.entity.ProductSpecify;
import top.daoyang.demo.entity.ProductSpecifyItem;

import java.util.List;

@Getter
@Setter
public class ProductSpecifyResponse {

    private ProductSpecify productSpecify;

    private List<ProductSpecifyItem> productSpecifyItemList;
}
