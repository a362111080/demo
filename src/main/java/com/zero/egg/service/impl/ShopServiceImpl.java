package com.zero.egg.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zero.egg.dao.ShopMapper;
import com.zero.egg.enums.CompanyUserEnums;
import com.zero.egg.model.Shop;
import com.zero.egg.requestDTO.LoginUser;
import com.zero.egg.service.IShopService;
import com.zero.egg.tool.Message;
import com.zero.egg.tool.UtilConstants;
import com.zero.egg.tool.UuidUtil;

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
		shop.setCreatetime(new Date());
		shop.setModifytime(new Date());
		shop.setStatus(CompanyUserEnums.Status.Normal.index().toString());
		shop.setDr(false);
		return super.save(shop);
	}

	@Override
	public Message<Object> save(Shop shop, LoginUser loginUser) {
		Message<Object> message = new Message<>();
		shop.setId(UuidUtil.get32UUID());
		shop.setCreatetime(new Date());
		shop.setModifytime(new Date());
		shop.setStatus(CompanyUserEnums.Status.Normal.index().toString());
		shop.setDr(false);
		shop.setModifier(loginUser.getId());
		shop.setCreator(loginUser.getId());
		if (save(shop)) {
			message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
			message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
		}else {
			message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
			message.setMessage(UtilConstants.ResponseMsg.FAILED);
		}
		return null;
		
	}


	@Autowired
	private ShopMapper mapper;

	@Override
	public List<Shop> getShopListByCompanid(String companyId) {
		return mapper.getShopListByCompanid(companyId);
	}

	@Override
	public Integer getClietnUseCountByShopid(String shopId, Integer type) {
		return mapper.getClietnUseCountByShopid(shopId,type);
	}

}
