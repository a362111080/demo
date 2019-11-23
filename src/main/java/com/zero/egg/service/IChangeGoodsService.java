package com.zero.egg.service;

import com.zero.egg.model.ChangeGoods;
import com.zero.egg.responseDTO.ChangeGoodsReponse;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Hhaifeng
 * @since 2019-05-03
 */
public interface IChangeGoodsService extends IService<ChangeGoods> {

	
	ChangeGoodsReponse findById(QueryWrapper<ChangeGoods> wrapper);
}
