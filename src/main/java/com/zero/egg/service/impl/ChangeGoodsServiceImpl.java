package com.zero.egg.service.impl;

import com.zero.egg.model.ChangeGoods;
import com.zero.egg.responseDTO.ChangeGoodsReponse;
import com.zero.egg.dao.ChangeGoodsMapper;
import com.zero.egg.service.IChangeGoodsService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Hhaifeng
 * @since 2019-05-03
 */
@Service
public class ChangeGoodsServiceImpl extends ServiceImpl<ChangeGoodsMapper, ChangeGoods> implements IChangeGoodsService {

	@Override
	public ChangeGoodsReponse findById(QueryWrapper<ChangeGoods> wrapper) {
		// TODO Auto-generated method stub
		return baseMapper.findById(wrapper);
	}

}
