package com.zero.egg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zero.egg.dao.BrokenGoodsMapper;
import com.zero.egg.model.BrokenGoods;
import com.zero.egg.model.Goods;
import com.zero.egg.requestDTO.BrokenGoodsRequest;
import com.zero.egg.responseDTO.BrokenGoodsReponse;
import com.zero.egg.service.IBrokenGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

	@Autowired
	private BrokenGoodsMapper mapper;
	@Override
	public IPage<BrokenGoodsReponse> listByCondition(IPage<BrokenGoods> page,
			QueryWrapper<BrokenGoodsRequest> queryWrapper) {
		return baseMapper.listByCondition(page, queryWrapper);
	}

	@Override
	public BrokenGoodsReponse findById(QueryWrapper<BrokenGoodsRequest> queryWrapper) {
		return baseMapper.findById(queryWrapper);
	}

	@Override
	public Goods GetBrokenInfo(String goodsNo) {

		return mapper.GetBrokenInfo(goodsNo);
	}

	@Override
	public Goods GetStoBrokenInfo(String brokenGoodsNo) {

		return mapper.GetStoBrokenInfo(brokenGoodsNo);
	}

	@Override
	public Boolean updateGoodsDr(String brokenGoodsNo) {
		return mapper.updateGoodsDr(brokenGoodsNo);
	}

	@Override
	public List<BrokenGoods> GetBrokenTask(BrokenGoodsRequest request) {
		return mapper.GetBrokenTask(request);
	}

}
