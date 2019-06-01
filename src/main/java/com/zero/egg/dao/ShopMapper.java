package com.zero.egg.dao;

import com.zero.egg.model.Shop;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

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

}
