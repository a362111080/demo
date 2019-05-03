package com.zero.egg.dao;

import com.zero.egg.model.Goods;
import com.zero.egg.responseDTO.GoodsResponse;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Hhaifeng
 * @since 2019-04-23
 */
public interface GoodsMapper extends BaseMapper<Goods> {
	
	List<GoodsResponse> listByCondition(@Param(Constants.WRAPPER) QueryWrapper<Goods> queryWrapper);

}
