package top.daoyang.demo.controller.portal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.daoyang.demo.enums.ProductStatusEnum;
import top.daoyang.demo.mapper.ProductSpecifyItemMapper;
import top.daoyang.demo.mapper.ProductSpecifyPriceStockMapper;
import top.daoyang.demo.payload.ServerResponse;
import top.daoyang.demo.payload.request.ProductSpecifyPriceAndStockRequest;
import top.daoyang.demo.service.ProductService;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductSpecifyPriceStockMapper productSpecifyPriceStockMapper;

    @Autowired
    private ProductSpecifyItemMapper productSpecifyItemMapper;

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

    @GetMapping("/search/bar")
    public ServerResponse searchProduct(@RequestParam(value = "q") String q) {

        return ServerResponse.createBySuccess(productService.searchBarProduct(q, ProductStatusEnum.ON_SALE.getValue()));
    }

    @GetMapping("/{productId}/specify")
    public ServerResponse getProductSpecify(@PathVariable Integer productId) {
        return ServerResponse.createBySuccess(productService.getProductSpecify(productId));

    }

    @GetMapping("/{productId}/specify/{specifyId}")
    public ServerResponse getProductSpecifyItemList(@PathVariable Integer productId,
                                                    @PathVariable Integer specifyId) {
        List<String> ss = Arrays.stream(productSpecifyPriceStockMapper.getProductSpecifyPASBySpecifyId(productId, specifyId).getSpecifyIds().split(""))
                .map(item -> productSpecifyItemMapper.getProductSpecifyItemById(Integer.parseInt(item)).getDescription()).collect(Collectors.toList());

        return ServerResponse.createBySuccess(ss);
    }
    @PostMapping("/specify/pas")
    public ServerResponse getProductSpecifyPriceAndStock(@Valid @RequestBody ProductSpecifyPriceAndStockRequest productIdAndSpecifyIds) {
        return ServerResponse.createBySuccess(productService.getProductSpecifyPriceAndStock(productIdAndSpecifyIds));
    }

    @GetMapping("/{productId}/parameter")
    public ServerResponse getProductParameter(@PathVariable Integer productId) {
        return ServerResponse.createBySuccess(productService.getProductParameterByProductId(productId));
    }
}
