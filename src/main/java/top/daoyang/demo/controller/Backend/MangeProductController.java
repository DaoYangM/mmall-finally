package top.daoyang.demo.controller.Backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.daoyang.demo.payload.ServerResponse;
import top.daoyang.demo.payload.request.ProductCreateRequest;
import top.daoyang.demo.service.ProductService;

import javax.validation.Valid;

@RestController
@RequestMapping("/manage/products")
public class MangeProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public ServerResponse getProducts(@RequestParam(value = "page", defaultValue = "0")  Integer page,
                                      @RequestParam(value = "size", defaultValue = "10")  Integer size) {
        return ServerResponse.createBySuccess(productService.getProducts(page, size, null));
    }

    @GetMapping("/{productId}")
    public ServerResponse getProductByProductId(@PathVariable Integer productId) {
        return ServerResponse.createBySuccess(productService.findProductByProductId(productId, null));
    }

    @GetMapping("/search")
    public ServerResponse searchProduct(@RequestParam(value = "page", defaultValue = "0")  Integer page,
                                        @RequestParam(value = "size", defaultValue = "10")  Integer size,
                                        @RequestParam(value = "q") String q) {

        return ServerResponse.createBySuccess(productService.searchProduct(page, size, q, null));
    }

    @PostMapping()
    public ServerResponse createProduct(@Valid @RequestBody ProductCreateRequest productCreateRequest) {
        return ServerResponse.createBySuccess(productService.createProduct(productCreateRequest));
    }

    @PutMapping("/{productId}")
    public ServerResponse updateProduct(@Valid @RequestBody ProductCreateRequest productCreateRequest,
                                        @PathVariable Integer productId) {
        return ServerResponse.createBySuccess(productService.updateProduct(productCreateRequest, productId));
    }
}
