package com.zero.egg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zero.egg.dao.StockMapper;
import com.zero.egg.model.Stock;
import com.zero.egg.requestDTO.StockRequest;
import com.zero.egg.responseDTO.StockMarkerListResponseDTO;
import com.zero.egg.responseDTO.StockResponse;
import com.zero.egg.service.IStockService;
import com.zero.egg.tool.Message;
import com.zero.egg.tool.ServiceException;
import com.zero.egg.tool.UtilConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Hhaifeng
 * @since 2019-03-26
 */
@Service
@Transactional
@Slf4j
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
	@Override
	public List<StockResponse> categoryListByCondition(QueryWrapper<StockRequest> queryWrapper) {
		
		return super.baseMapper.categoryListByCondition(queryWrapper);
	}

    @Override
    public Message<StockMarkerListResponseDTO> markerListByCondition(QueryWrapper<Stock> queryWrapper) {
        Message<StockMarkerListResponseDTO> message = new Message<>();
        StockMarkerListResponseDTO stockMarkerListResponseDTO = new StockMarkerListResponseDTO();
        try {
            List<String> markerList = super.baseMapper.markerListByCondition(queryWrapper);
            stockMarkerListResponseDTO.setMarkerList(markerList);
            message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
            message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
            message.setData(stockMarkerListResponseDTO);
            return message;
        } catch (Exception e) {
            log.error("markerListByCondition service error:" + e);
            throw new ServiceException("markerListByCondition service error");
        }
    }
}
