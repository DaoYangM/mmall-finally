package top.daoyang.demo.controller.portal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.daoyang.demo.enums.ProductStatusEnum;
import top.daoyang.demo.payload.ServerResponse;
import top.daoyang.demo.service.ProductService;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public ServerResponse getProducts(@RequestParam(value = "page", defaultValue = "0")  Integer page,
                                      @RequestParam(value = "size", defaultValue = "10")  Integer size) {
        return ServerResponse.createBySuccess(productService.getProducts(page, size, ProductStatusEnum.ON_SALE.getValue()));
    }

    @GetMapping("/{productId}")
    public ServerResponse getProductByProductId(@PathVariable Integer productId) {
        return ServerResponse.createBySuccess(productService.findProductByProductId(productId, ProductStatusEnum.ON_SALE.getValue()));
    }

    @GetMapping("/search")
    public ServerResponse searchProduct(@RequestParam(value = "page", defaultValue = "0")  Integer page,
                                        @RequestParam(value = "size", defaultValue = "10")  Integer size,
                                        @RequestParam(value = "q") String q) {

        return ServerResponse.createBySuccess(productService.searchProduct(page, size, q, ProductStatusEnum.ON_SALE.getValue()));
    }

    @GetMapping("/{productId}/specify")
    public ServerResponse getProductSpecify(@PathVariable Integer productId) {
        return ServerResponse.createBySuccess(productService.getProductSpecify(productId));

    }
}
