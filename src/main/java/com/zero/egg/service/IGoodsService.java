package com.zero.egg.service;

import com.zero.egg.model.Goods;
import com.zero.egg.responseDTO.GoodsResponse;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Hhaifeng
 * @since 2019-04-23
 */
public interface IGoodsService extends IService<Goods> {
	
	List<GoodsResponse> listByCondition(QueryWrapper<Goods> queryWrapper);

}
