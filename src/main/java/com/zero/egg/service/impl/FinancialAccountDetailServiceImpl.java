
package com.zero.egg.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zero.egg.dao.FinancialAccountDetailMapper;
import com.zero.egg.model.FinancialAccountDetailModel;
import com.zero.egg.service.FinancialAccountDetailService;
import com.zero.egg.tool.Message;
import com.zero.egg.tool.UtilConstants;

@Service
@Transactional
public class FinancialAccountDetailServiceImpl implements FinancialAccountDetailService{

	@Autowired
	FinancialAccountDetailMapper mappper;
	
	@Override
	public Message save(FinancialAccountDetailModel m) {
		Message message = new Message();
		mappper.insert(m);
		message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
        message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
		return message;
	}
	
	@Override
	public Message getListByAccountId(String id) {
		Message message = new Message();
		List<FinancialAccountDetailModel> list = mappper.findByAccountId(id);
		message.setData(list);
        message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
        message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
		return message;
	}
}
