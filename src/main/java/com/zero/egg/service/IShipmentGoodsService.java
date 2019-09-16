package com.zero.egg.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zero.egg.model.ShipmentGoods;
import com.zero.egg.requestDTO.LoginUser;
import com.zero.egg.requestDTO.ShipmentGoodsRequest;
import com.zero.egg.requestDTO.ShipmentStaticRequestDTO;
import com.zero.egg.responseDTO.ShipmentGoodsResponse;
import com.zero.egg.tool.Message;

import java.util.List;

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
	
	public List<ShipmentGoodsResponse> todaycountcategory(ShipmentStaticRequestDTO shipmentStaticRequestDTO, QueryWrapper<ShipmentGoods> queryWrapper);
	
	public List<ShipmentGoodsResponse> todaycountspecification(ShipmentStaticRequestDTO shipmentStaticRequestDTO,QueryWrapper<ShipmentGoods> queryWrapper);

	/**
	 * 出货列表和整理归类
	 * @return
	 */
	public Message<Object> listBySortType(LoginUser loginUser, ShipmentGoodsRequest shipmentGoodsRequest);
}
