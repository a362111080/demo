package com.zero.egg.dao;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.zero.egg.model.BrokenGoods;
import com.zero.egg.requestDTO.BrokenGoodsRequest;
import com.zero.egg.responseDTO.BrokenGoodsReponse;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Hhaifeng
 * @since 2019-05-03
 */
public interface BrokenGoodsMapper extends BaseMapper<BrokenGoods> {

	
	IPage<BrokenGoodsReponse> listByCondition(IPage<BrokenGoods> page,@Param(Constants.WRAPPER) QueryWrapper<BrokenGoodsRequest> wrapper);
	
	
	BrokenGoodsReponse findById(QueryWrapper<BrokenGoodsRequest> wrapper);
	
}