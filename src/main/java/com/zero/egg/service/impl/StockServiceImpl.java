package com.zero.egg.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zero.egg.dao.StockMapper;
import com.zero.egg.model.Stock;
import com.zero.egg.requestDTO.StockRequest;
import com.zero.egg.responseDTO.StockResponse;
import com.zero.egg.service.IStockService;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Hhaifeng
 * @since 2019-03-26
 */
@Service
public class StockServiceImpl extends ServiceImpl<StockMapper, Stock> implements IStockService {

	@Override
	public IPage<StockResponse> listByCondition(IPage<Stock> page,QueryWrapper<StockRequest> queryWrapper) {
		
		return super.baseMapper.listByCondition(page,queryWrapper);
	}
	
	@Override
	public List<StockResponse> listByCondition(QueryWrapper<StockRequest> queryWrapper) {
		
		return super.baseMapper.listByCondition(queryWrapper);
	}
	@Override
	public List<StockResponse> categoryCountListByCondition(QueryWrapper<StockRequest> queryWrapper) {
		
		return super.baseMapper.categoryCountListByCondition(queryWrapper);
	}
	
	

}
