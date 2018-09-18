package top.daoyang.demo.service;

import com.github.pagehelper.PageInfo;
import top.daoyang.demo.entity.Product;
import top.daoyang.demo.entity.ProductParamter;
import top.daoyang.demo.entity.ProductSpecifyPriceStock;
import top.daoyang.demo.payload.reponse.ProductSpecifyResponse;
import top.daoyang.demo.payload.request.ProductCreateRequest;
import top.daoyang.demo.payload.request.ProductSpecifyPriceAndStockRequest;

import java.util.List;

public interface ProductService {

    PageInfo<Product> getProducts(int page, int size, Integer status, String sort);

    Product findProductByProductId(Integer productId, Integer status);

    PageInfo<Product> searchProduct(int page, int size, String q, Integer status, String sort);

    Product createProduct(ProductCreateRequest productCreateRequest);

    Product updateProduct(ProductCreateRequest productCreateRequest, Integer productId);

    List<ProductSpecifyResponse> getProductSpecify(Integer productId);

    ProductSpecifyPriceStock getProductSpecifyPriceAndStock(ProductSpecifyPriceAndStockRequest productIdAndSpecifyIds);

    List<ProductParamter> getProductParameterByProductId(Integer productId);

    List<String> searchBarProduct(String q, int saleStatus);

    List<String> getProductSpecifyBySpecifyId(Integer productId, Integer specifyId);
}
