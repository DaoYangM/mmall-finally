package top.daoyang.demo.service;

import com.github.pagehelper.PageInfo;
import top.daoyang.demo.entity.Shipping;
import top.daoyang.demo.payload.request.ShippingCreateRequest;

public interface ShippingService {
    PageInfo getShippingList(String userId, Integer page, Integer size);

    Shipping createShipping(String userId, ShippingCreateRequest shippingCreateRequest);

    Shipping getShippingByShippingId(String userId, Integer shippingId);

    boolean deleteShippingByShippingId(String userId, Integer shippingId);

    Shipping patchShippingByShippingId(String userId, Integer shippingId, ShippingCreateRequest shippingCreateRequest);

    Shipping getCheckShipping(String userId);

    PageInfo changeChecked(String userId, Integer shippingId);
}
