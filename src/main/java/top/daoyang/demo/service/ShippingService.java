package top.daoyang.demo.service;

import com.github.pagehelper.PageInfo;
import top.daoyang.demo.entity.Shipping;
import top.daoyang.demo.payload.request.ShippingCreateRequest;

public interface ShippingService {
    PageInfo getShippingList(Long userId, Integer page, Integer size);

    Shipping createShipping(Long userId, ShippingCreateRequest shippingCreateRequest);

    Shipping getShippingByShippingId(Long userId, Integer shippingId);

    boolean deleteShippingByShippingId(Long userId, Integer shippingId);

    Shipping patchShippingByShippingId(Long userId, Integer shippingId, ShippingCreateRequest shippingCreateRequest);
}
