package com.zero.egg.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zero.egg.model.Stock;
import com.zero.egg.requestDTO.StockRequest;
import com.zero.egg.responseDTO.StockMarkerListResponseDTO;
import com.zero.egg.responseDTO.StockResponse;
import com.zero.egg.tool.Message;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Hhaifeng
 * @since 2019-03-26
 */
public interface IStockService extends IService<Stock> {


    public IPage<StockResponse> listByCondition(IPage<Stock> page, QueryWrapper<StockRequest> queryWrapper);


    public List<StockResponse> listByCondition(QueryWrapper<StockRequest> queryWrapper);

    public List<StockResponse> categoryCountListByCondition(QueryWrapper<StockRequest> queryWrapper);

    public List<StockResponse> categoryListByCondition(QueryWrapper<StockRequest> queryWrapper);

    Message<StockMarkerListResponseDTO> markerListByCondition(QueryWrapper<Stock> queryWrapper);
}
