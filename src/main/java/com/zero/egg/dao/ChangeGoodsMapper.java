package com.zero.egg.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zero.egg.model.ChangeGoods;
import com.zero.egg.responseDTO.ChangeGoodsReponse;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Hhaifeng
 * @since 2019-05-03
 */
public interface ChangeGoodsMapper extends BaseMapper<ChangeGoods> {
	
	
	ChangeGoodsReponse findById(QueryWrapper<ChangeGoods> wrapper);
}
