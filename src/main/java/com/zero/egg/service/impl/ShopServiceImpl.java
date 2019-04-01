package com.zero.egg.service.impl;

import com.zero.egg.model.Shop;
import com.zero.egg.dao.ShopMapper;
import com.zero.egg.enums.CompanyUserEnums;
import com.zero.egg.service.IShopService;
import com.zero.egg.tool.UuidUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Hhaifeng
 * @since 2019-03-13
 */
@Service
public class ShopServiceImpl extends ServiceImpl<ShopMapper, Shop> implements IShopService {
	
	@Override
	public boolean save(Shop shop) {
		shop.setId(UuidUtil.get32UUID());
		shop.setCreatetime(LocalDateTime.now());
		shop.setModifytime(LocalDateTime.now());
		shop.setStatus(CompanyUserEnums.Status.Normal.index().toString());
		shop.setDr(false);
		return super.save(shop);
	}

}
