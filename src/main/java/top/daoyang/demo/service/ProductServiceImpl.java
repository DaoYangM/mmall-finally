package top.daoyang.demo.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import top.daoyang.demo.entity.Product;
import top.daoyang.demo.enums.ExceptionEnum;
import top.daoyang.demo.enums.ProductStatusEnum;
import top.daoyang.demo.exception.ProductException;
import top.daoyang.demo.mapper.CategoryMapper;
import top.daoyang.demo.mapper.ProductMapper;
import top.daoyang.demo.payload.request.ProductCreateRequest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public PageInfo<Product> getProducts(int page, int size, Integer status) {
        PageHelper.startPage(page, size);
        return new PageInfo<>(productMapper.findProduct(status));
    }

    @Override
    public Product findProductByProductId(Integer productId, Integer status) {
        return Optional.ofNullable(productMapper.findProductByProductId(productId, status))
            .orElseThrow(() -> new ProductException(ExceptionEnum.PRODUCT_DOES_NOT_EXIST));
    }

    @Override
    public PageInfo<Product> searchProduct(int page, int size, String q, Integer status) {
        if (!StringUtils.hasText(q))
            throw new ProductException(ExceptionEnum.PRODUCT_DOES_NOT_EXIST);
        PageHelper.startPage(page, size);
        return new PageInfo<>(productMapper.searchByKeyword(q, status));
    }

    @Override
    public Product createProduct(ProductCreateRequest productCreateRequest) {
        Product product = new Product();

        checkAvailable(productCreateRequest);

        BeanUtils.copyProperties(productCreateRequest, product);
        if (StringUtils.hasText(product.getSubImages())) {
            String[] subImages = product.getSubImages().split(",");
            product.setMainImage(subImages[0]);
        }
        if (productMapper.insertSelective(product) == 1)
            return product;
        throw new ProductException(ExceptionEnum.PRODUCT_CREATE_ERROR);
    }

    @Override
    public Product updateProduct(ProductCreateRequest productCreateRequest, Integer productId) {

        Product product = Optional.ofNullable(productMapper.selectByPrimaryKey(productId))
                .orElseThrow(() -> new ProductException(ExceptionEnum.PRODUCT_DOES_NOT_EXIST));

        checkAvailable(productCreateRequest);

        BeanUtils.copyProperties(productCreateRequest, product);
        if (productMapper.updateByPrimaryKeySelective(product) == 1)
            return product;
        throw new ProductException(ExceptionEnum.PRODUCT_UPDATE_ERROR);
    }

    private boolean checkAvailable(ProductCreateRequest productCreateRequest) {
        if (categoryMapper.selectByPrimaryKey(productCreateRequest.getCategoryId()) == null)
            throw new ProductException(ExceptionEnum.CATEGORY_DOES_NOT_EXIST);

        List<Integer> productAllStatus = Arrays.stream(ProductStatusEnum.values())
                .map(ProductStatusEnum::getValue).collect(Collectors.toList());

        if (!productAllStatus.contains(productCreateRequest.getStatus()))
            throw new ProductException(ExceptionEnum.PRODUCT_SET_STATUS_ERROR);

        return true;
    }
}
