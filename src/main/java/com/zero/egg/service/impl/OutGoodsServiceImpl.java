package com.zero.egg.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zero.egg.dao.OutGoodsMapper;
import com.zero.egg.model.OutGoodsModel;
import com.zero.egg.requestDTO.OutGoodsDTO;
import com.zero.egg.service.OutGoodsService;
import com.zero.egg.tool.Message;
import com.zero.egg.tool.UtilConstants;

@Service
@Transactional
public class OutGoodsServiceImpl implements OutGoodsService{

	@Autowired
	private OutGoodsMapper mapper;
	
	@Override
	public Message outGoods(OutGoodsDTO dto) {
		Message message = new Message();
		OutGoodsModel m = new OutGoodsModel();
		m.setEggTypeId(dto.getEggTypeId());
		m.setEggTypeName(dto.getEggTypeName());
		m.setPrice(dto.getPrice());
		m.setFlag(dto.getFlag());
		m.setRealWeight(dto.getRealWeight());
		m.setEmployeeId(dto.getEmployeeId());
		m.setEmployeeName(dto.getEmployeeName());
		mapper.insert(m);
		message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
		message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
		return message;
	}

	@Override
	public Message showGoods() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Message endOutGoods() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Message infoPrint() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Message outGoodsStatistical() {
		// TODO Auto-generated method stub
		return null;
	}

}
