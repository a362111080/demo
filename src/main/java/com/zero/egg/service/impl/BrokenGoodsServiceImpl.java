package com.zero.egg.service.impl;

import com.zero.egg.model.BrokenGoods;
import com.zero.egg.requestDTO.BrokenGoodsRequest;
import com.zero.egg.responseDTO.BrokenGoodsReponse;
import com.zero.egg.dao.BrokenGoodsMapper;
import com.zero.egg.service.IBrokenGoodsService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
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
public class BrokenGoodsServiceImpl extends ServiceImpl<BrokenGoodsMapper, BrokenGoods> implements IBrokenGoodsService {

	@Override
	public IPage<BrokenGoodsReponse> listByCondition(IPage<BrokenGoods> page,
			QueryWrapper<BrokenGoodsRequest> queryWrapper) {
		return baseMapper.listByCondition(page, queryWrapper);
	}

	@Override
	public BrokenGoodsReponse findById(QueryWrapper<BrokenGoodsRequest> queryWrapper) {
		return baseMapper.findById(queryWrapper);
	}

}
