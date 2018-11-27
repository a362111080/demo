package com.zero.egg.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zero.egg.dao.WhGoodsMapper;
import com.zero.egg.model.DamageGoods;
import com.zero.egg.model.WhGoods;
import com.zero.egg.service.WhGoodsService;
import com.zero.egg.tool.PageData;

/**
 * @author hhfeng
 * @Title: WhProductServiceImpl 
 * @Description:  仓库产品service
 * @date 2018年11月13日
 */
@Service
@Transactional
public class WhGoodsServiceImpl implements WhGoodsService{

	@Autowired
	private WhGoodsMapper whGoodsMapper;

	@Override
	public WhGoods getGoodsInfoById(String id) {
		// TODO Auto-generated method stub
		return whGoodsMapper.getGoodsInfoById(id);
	}

	@Override
	public List<WhGoods> GoodsList(WhGoods whGoods) {
		return whGoodsMapper.GoodsList(whGoods);
	}

	@Override
	public int addGoods(WhGoods whGoods) {
		// TODO Auto-generated method stub
		return whGoodsMapper.addGoods(whGoods);
	}

	@Override
	public int updateGoods(WhGoods whGoods) {
		return whGoodsMapper.updateGoods(whGoods);
	}

	@Override
	public List<PageData> listDamageGoods(DamageGoods damageGoods) {
		return whGoodsMapper.listDamageGoods(damageGoods);
	}

	@Override
	public int addDamageGoods(DamageGoods damageGoods) {
		return whGoodsMapper.addDamageGoods(damageGoods);
	}
	  
	
}
