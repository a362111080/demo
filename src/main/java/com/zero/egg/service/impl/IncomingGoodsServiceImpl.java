package com.zero.egg.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zero.egg.dao.IncomingGoodsMapper;
import com.zero.egg.model.IncomingGoodsModel;
import com.zero.egg.requestDTO.IncomingGoodsDTO;
import com.zero.egg.service.IncomingGoodsService;
import com.zero.egg.tool.Message;
import com.zero.egg.tool.UtilConstants;

@Service
@Transactional
public class IncomingGoodsServiceImpl implements IncomingGoodsService{
	
	@Autowired
	private IncomingGoodsMapper mapper;
	
	@Override
	public Message saveGoods(IncomingGoodsDTO dto) {
		Message message = new Message();
		IncomingGoodsModel m = new IncomingGoodsModel();
		m.setSupplierId(dto.getSupplierId());
		m.setSupplierName(dto.getSupplierName());
		m.setPlanId(dto.getPlanId());
		m.setPlanName(dto.getPlanName());
		m.setEggTypeId(dto.getEggTypeId());
		m.setEggTypeName(dto.getEggTypeName());
		m.setPrice(dto.getPrice());
		m.setFlag(dto.getFlag());
		m.setWay(dto.getWay());
		m.setRealWeight(dto.getRealWeight());
		m.setAllocationId(dto.getAllocationId());
		m.setAllocationName(dto.getAllocationName());
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
	public Message stopIncomingGoods() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Message endIncomingGoods() {
		Message message = new Message();
		
		message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
		message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
		return message;
	}

	@Override
	public Message infoPrint() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Message incomingGoodsStatistical() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
