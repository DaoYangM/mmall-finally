package top.daoyang.demo.service;

import com.github.pagehelper.PageInfo;
import top.daoyang.demo.entity.Product;
import top.daoyang.demo.payload.request.ProductCreateRequest;

public interface ProductService {

    PageInfo<Product> getProducts(int page, int size, Integer status);

    Product findProductByProductId(Integer productId, Integer status);

    PageInfo<Product> searchProduct(int page, int size, String q, Integer status);

    Product createProduct(ProductCreateRequest productCreateRequest);

    Product updateProduct(ProductCreateRequest productCreateRequest, Integer productId);
}
