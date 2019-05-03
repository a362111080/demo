package com.zero.egg.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zero.egg.model.BrokenGoods;
import com.zero.egg.requestDTO.BrokenGoodsRequest;
import com.zero.egg.responseDTO.BrokenGoodsReponse;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Hhaifeng
 * @since 2019-05-03
 */
public interface IBrokenGoodsService extends IService<BrokenGoods> {

	
	IPage<BrokenGoodsReponse> listByCondition(IPage<BrokenGoods> page,QueryWrapper<BrokenGoodsRequest> queryWrapper);
	
	
	BrokenGoodsReponse findById(QueryWrapper<BrokenGoodsRequest> queryWrapper);
	
}
