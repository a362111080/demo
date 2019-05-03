package com.zero.egg.service.impl;

import com.zero.egg.model.Goods;
import com.zero.egg.responseDTO.GoodsResponse;
import com.zero.egg.dao.GoodsMapper;
import com.zero.egg.service.IGoodsService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Hhaifeng
 * @since 2019-04-23
 */
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements IGoodsService {

	@Override
	public List<GoodsResponse> listByCondition(QueryWrapper<Goods> queryWrapper) {
		// TODO Auto-generated method stub
		return baseMapper.listByCondition(queryWrapper);
	}

}
