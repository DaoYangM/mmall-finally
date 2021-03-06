package top.daoyang.demo.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import top.daoyang.demo.entity.Shipping;
import top.daoyang.demo.enums.ExceptionEnum;
import top.daoyang.demo.exception.ShippingException;
import top.daoyang.demo.mapper.ShippingMapper;
import top.daoyang.demo.payload.request.ShippingCreateRequest;
import top.daoyang.demo.service.ShippingService;

import java.util.Optional;

@Service
public class ShippingServiceImpl implements ShippingService {

    @Autowired
    private ShippingMapper shippingMapper;

    @Override
    public PageInfo getShippingList(Long userId, Integer page, Integer size) {
        PageHelper.startPage(page, size);
        return PageInfo.of(shippingMapper.findByUserId(userId));
    }

    @Override
    public Shipping createShipping(Long userId, ShippingCreateRequest shippingCreateRequest) {
        Shipping shipping = new Shipping();
        BeanUtils.copyProperties(shippingCreateRequest, shipping);

        shipping.setUserId(userId);

        if (shippingMapper.insertSelective(shipping) == 1) {
            return shipping;
        }

        throw new ShippingException(ExceptionEnum.SHIPPING_CREATE_ERROR);
    }

    @Override
    public Shipping getShippingByShippingId(Long userId, Integer shippingId) {
        return Optional.ofNullable(shippingMapper.findShippingByUserId(userId, shippingId))
                .orElseThrow(() -> new ShippingException(ExceptionEnum.SHIPPING_DOES_NOT_EXIST));
    }

    @Override
    public boolean deleteShippingByShippingId(Long userId, Integer shippingId) {

        return shippingMapper.deleteByShippingIdAndUserId(userId, shippingId) == 1;
    }

    @Override
    public Shipping patchShippingByShippingId(Long userId, Integer shippingId, ShippingCreateRequest shippingCreateRequest) {
        Shipping shipping = getShippingByShippingId(userId, shippingId);

        BeanUtils.copyProperties(shippingCreateRequest, shipping);

        if (shippingMapper.updateByPrimaryKeySelective(shipping) == 1)
            return shipping;
        throw new ShippingException(ExceptionEnum.SHIPPING_UPDATE_ERROR);
    }
}
