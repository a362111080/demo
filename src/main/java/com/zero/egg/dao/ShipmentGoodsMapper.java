package com.zero.egg.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.zero.egg.model.ShipmentGoods;
import com.zero.egg.responseDTO.ShipmentGoodsResponse;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Hhaifeng
 * @since 2019-04-23
 */
public interface ShipmentGoodsMapper extends BaseMapper<ShipmentGoods> {

	IPage<ShipmentGoodsResponse> listByCondition(IPage<ShipmentGoods> page,@Param(Constants.WRAPPER) QueryWrapper<ShipmentGoods> wrapper);
	
	
	List<ShipmentGoodsResponse> listByCondition(@Param(Constants.WRAPPER) QueryWrapper<ShipmentGoods> wrapper);
	
	
	List<ShipmentGoodsResponse> countprogram(@Param(Constants.WRAPPER) QueryWrapper<ShipmentGoods> wrapper);
	
	List<ShipmentGoodsResponse> countcategory(@Param(Constants.WRAPPER) QueryWrapper<ShipmentGoods> wrapper);
	
	List<ShipmentGoodsResponse> countspecification(@Param(Constants.WRAPPER) QueryWrapper<ShipmentGoods> wrapper);
	
	
	
	
}
