package com.zero.egg.service;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zero.egg.model.ShipmentGoods;
import com.zero.egg.responseDTO.ShipmentGoodsResponse;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Hhaifeng
 * @since 2019-04-23
 */
public interface IShipmentGoodsService extends IService<ShipmentGoods> {

	
	public IPage<ShipmentGoodsResponse> listByCondition(IPage<ShipmentGoods> page,QueryWrapper<ShipmentGoods> queryWrapper);
	
	public List<ShipmentGoodsResponse> listByCondition(QueryWrapper<ShipmentGoods> queryWrapper);
	
	public List<ShipmentGoodsResponse> countProgram(QueryWrapper<ShipmentGoods> queryWrapper);
	
	public List<ShipmentGoodsResponse> countCategory(QueryWrapper<ShipmentGoods> queryWrapper);
	
	public List<ShipmentGoodsResponse> countSpecification(QueryWrapper<ShipmentGoods> queryWrapper);
	
	public List<ShipmentGoodsResponse> todaycountcategory(QueryWrapper<ShipmentGoods> queryWrapper);
	
	public List<ShipmentGoodsResponse> todaycountspecification(QueryWrapper<ShipmentGoods> queryWrapper);
	
}
