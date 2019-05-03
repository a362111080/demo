package com.zero.egg.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.zero.egg.model.Stock;
import com.zero.egg.requestDTO.StockRequest;
import com.zero.egg.responseDTO.StockResponse;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Hhaifeng
 * @since 2019-03-26
 */
public interface StockMapper extends BaseMapper<Stock> {

	
	IPage<StockResponse> listByCondition(IPage<Stock> page,@Param(Constants.WRAPPER) QueryWrapper<StockRequest> wrapper);
	
	
	List<StockResponse> listByCondition(@Param(Constants.WRAPPER) QueryWrapper<StockRequest> wrapper);
	
	List<StockResponse> categoryListByCondition(@Param(Constants.WRAPPER) QueryWrapper<StockRequest> wrapper);
	
	List<StockResponse> categoryCountListByCondition(@Param(Constants.WRAPPER) QueryWrapper<StockRequest> wrapper);
}
