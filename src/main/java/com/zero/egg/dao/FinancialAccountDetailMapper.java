package com.zero.egg.dao;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zero.egg.model.FinancialAccountDetailModel;

/**
 * 账单明细
 * @author JC
 *
 */
public interface FinancialAccountDetailMapper extends BaseMapper<FinancialAccountDetailModel> {
	
	public List<FinancialAccountDetailModel> findByAccountId(String accountId);
}
