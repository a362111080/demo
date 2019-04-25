package com.zero.egg.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zero.egg.dao.ShipmentGoodsMapper;
import com.zero.egg.model.ShipmentGoods;
import com.zero.egg.responseDTO.ShipmentGoodsResponse;
import com.zero.egg.service.IShipmentGoodsService;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Hhaifeng
 * @since 2019-04-23
 */
@Service
public class ShipmentGoodsServiceImpl extends ServiceImpl<ShipmentGoodsMapper, ShipmentGoods> implements IShipmentGoodsService {

	@Override
	public IPage<ShipmentGoodsResponse> listByCondition(IPage<ShipmentGoods> page, QueryWrapper<ShipmentGoods> queryWrapper) {
		return super.baseMapper.listByCondition(page, queryWrapper);
	}

	@Override
	public List<ShipmentGoodsResponse> listByCondition(QueryWrapper<ShipmentGoods> queryWrapper) {
		return super.baseMapper.listByCondition(queryWrapper);
	}
	
	@Override
	public List<ShipmentGoodsResponse> countProgram(QueryWrapper<ShipmentGoods> queryWrapper) {
		return super.baseMapper.countprogram(queryWrapper);
	}
	@Override
	public List<ShipmentGoodsResponse> countCategory(QueryWrapper<ShipmentGoods> queryWrapper) {
		return super.baseMapper.countcategory(queryWrapper);
	}
	@Override
	public List<ShipmentGoodsResponse> countSpecification(QueryWrapper<ShipmentGoods> queryWrapper) {
		return super.baseMapper.countspecification(queryWrapper);
	}

}
