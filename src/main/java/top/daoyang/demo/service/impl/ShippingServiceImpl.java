package top.daoyang.demo.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import top.daoyang.demo.entity.Shipping;
import top.daoyang.demo.enums.ExceptionEnum;
import top.daoyang.demo.enums.ShippingStatusEnum;
import top.daoyang.demo.exception.ShippingException;
import top.daoyang.demo.mapper.ShippingMapper;
import top.daoyang.demo.payload.request.ShippingCreateRequest;
import top.daoyang.demo.service.ShippingService;

import java.util.List;
import java.util.Optional;

@Service
public class ShippingServiceImpl implements ShippingService {

    @Autowired
    private ShippingMapper shippingMapper;

    @Override
    public PageInfo getShippingList(String userId, Integer page, Integer size) {
        PageHelper.startPage(page, size);
        return new PageInfo(shippingMapper.findByUserId(userId));
    }

    @Override
    public Shipping createShipping(String userId, ShippingCreateRequest shippingCreateRequest) {
        List<Shipping> shippingList = shippingMapper.findByUserId(userId);

        for (Shipping shipping1 : shippingList) {
            if (shipping1.getReceiverName().equals(shippingCreateRequest.getReceiverName()) &&
                    shipping1.getReceiverPhone().equals(shippingCreateRequest.getReceiverPhone()) &&
                    shipping1.getReceiverAddress().equals(shippingCreateRequest.getReceiverAddress())) {

                shippingMapper.changeAllUncheck(userId);
                if (shippingMapper.restoreShipping(userId, shipping1.getId()) == 1) {
                    return shipping1;
                } else {
                    throw new ShippingException(ExceptionEnum.SHIPPING_UPDATE_ERROR);
                }
            }
        }

        Shipping shipping = new Shipping();
        BeanUtils.copyProperties(shippingCreateRequest, shipping);

        shipping.setUserId(userId);
        shippingMapper.changeAllUncheck(userId);

        if (shippingMapper.insertSelective(shipping) == 1) {
            return shipping;
        }

        throw new ShippingException(ExceptionEnum.SHIPPING_CREATE_ERROR);
    }

    @Override
    public Shipping getShippingByShippingId(String userId, Integer shippingId) {
        return Optional.ofNullable(shippingMapper.findShippingByUserId(userId, shippingId))
                .orElseThrow(() -> new ShippingException(ExceptionEnum.SHIPPING_DOES_NOT_EXIST));
    }

    @Override
    public boolean deleteShippingByShippingId(String userId, Integer shippingId) {
        Shipping shipping = getShippingByShippingId(userId, shippingId);

        if (shipping.getChecked() == 1) {
            List<Shipping> shippingList = shippingMapper.findByUserId(userId);

            if (shippingList.size() > 1) {

                for (int i = 0; i < shippingList.size(); i++) {
                    if (! shippingList.get(i).getId().equals(shipping.getId())) {
                        shippingList.get(i).setChecked(1);
                        shippingMapper.updateByPrimaryKeySelective(shippingList.get(i));
                        break;
                    }
                }
            }
        }
        return shippingMapper.deletedByShippingIdAndUserId(userId, shippingId, ShippingStatusEnum.DELETED.getCode()) == 1;
    }

    @Override
    public Shipping patchShippingByShippingId(String userId, Integer shippingId, ShippingCreateRequest shippingCreateRequest) {
        Shipping shipping = getShippingByShippingId(userId, shippingId);

        BeanUtils.copyProperties(shippingCreateRequest, shipping);

        if (shippingMapper.updateByPrimaryKeySelective(shipping) == 1)
            return shipping;
        throw new ShippingException(ExceptionEnum.SHIPPING_UPDATE_ERROR);
    }

    @Override
    public Shipping getCheckShipping(String userId) {
        return Optional.ofNullable(shippingMapper.findCheckShipping(userId)).orElse(null);
    }

    @Override
    public PageInfo changeChecked(String userId, Integer shippingId) {
        Shipping shipping = getShippingByShippingId(userId, shippingId);

        shippingMapper.changeAllUncheck(userId);

        if (shippingMapper.changeChecked(userId, shipping.getId()) == 1)
            return getShippingList(userId, 1, 10);
        else
            throw new ShippingException(ExceptionEnum.SHIPPING_UPDATE_ERROR);
    }
}
