package com.zero.egg.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.StringUtil;
import com.zero.egg.dao.FinancialAccountMapper;
import com.zero.egg.model.FinancialAccountDetailModel;
import com.zero.egg.model.FinancialAccountModel;
import com.zero.egg.requestDTO.FinancialAccountRequestDTO;
import com.zero.egg.service.FinancialAccountDetailService;
import com.zero.egg.service.FinancialAccountService;
import com.zero.egg.tool.Message;
import com.zero.egg.tool.UtilConstants;
import com.zero.egg.tool.UuidUtil;

@Service
@Transactional
public class FinancialAccountServiceImpl implements FinancialAccountService {

	@Autowired
	FinancialAccountMapper mapper;
	@Autowired
	FinancialAccountDetailService financialAccountDetailService;

	@Override
	public Message save(String name, String store, String type, List<FinancialAccountDetailModel> list, String note) {
		Message message = new Message();
		if (list != null && list.size() > 0) {
			FinancialAccountModel m = new FinancialAccountModel();
			m.setAccountNo("FA-" + UuidUtil.getRandomNum());
			m.setName(name);
			m.setStore(store);
			m.setType(type);
			m.setStatus("1");
			m.setNote(note);
			String eggTypeNames = "";
			int number = 0;
			double amt = 0;
			for (FinancialAccountDetailModel dm : list) {
				eggTypeNames = eggTypeNames + dm.getEggTypeName() + "/";
				number = number + dm.getNumber();
				amt = amt + dm.getNumber() * dm.getPrice();
			}
			m.setEggTypeNames(eggTypeNames);
			m.setNumber(number);
			m.setAmt(amt);
			mapper.insert(m);

			for (FinancialAccountDetailModel dm : list) {
				dm.setAccountId(m.getId());
				dm.setAmt(dm.getNumber() * dm.getPrice());
				financialAccountDetailService.save(dm);
			}
			message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
			message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
			return message;
		}
		message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
		message.setMessage(UtilConstants.ResponseMsg.FAILED);
		return message;
	}

	@Override
	public Message getList(FinancialAccountRequestDTO dto) {
		Message message = new Message();
		Page<FinancialAccountModel> page = new Page<>();
		page.setCurrent(dto.getCurrent());
		page.setSize(dto.getSize());

		QueryWrapper qw = new QueryWrapper<FinancialAccountModel>();
		if (StringUtil.isEmpty(dto.getName())) {
			qw.like("name", "%" + dto.getName() + "%");
		}
		if (StringUtil.isEmpty(dto.getAccountNo())) {
			qw.eq("account", dto.getAccountNo());
		}
		if (StringUtil.isEmpty(dto.getStore())) {
			qw.like("stoer", "%" + dto.getStore() + "%");
		}
		if (StringUtil.isEmpty(dto.getBeginTime())) {
			qw.ge("createTime", dto.getBeginTime());
		}
		if (StringUtil.isEmpty(dto.getEndTime())) {
			qw.le("createTime", dto.getEndTime());
		}
		page = (Page<FinancialAccountModel>)mapper.selectPage(page, qw);
		message.setData(page);
        message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
        message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
		return message;
	}

	@Override
	public Message cancelAccount(String accountId) {
		Message message = new Message();
		FinancialAccountModel model = mapper.findById(accountId);
		model.setStatus("2");
		mapper.updateById(model);
		message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
		message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
		return message;
	}

}
