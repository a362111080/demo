package com.zero.egg.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zero.egg.model.OrderSecret;
import com.zero.egg.model.Shop;
import com.zero.egg.requestDTO.ShopRequest;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Hhaifeng
 * @since 2019-03-13
 */
public interface ShopMapper extends BaseMapper<Shop> {

    List<Shop> getShopListByCompanid(String companyId);

    Integer getClietnUseCountByShopid(String shopId, Integer type);

    int addsecret(OrderSecret se);

    List<OrderSecret> GetShopSecret(ShopRequest shop);
}
