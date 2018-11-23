package com.zero.egg.service;

import com.zero.egg.model.FinancialAccountDetailModel;
import com.zero.egg.tool.Message;

/**
 * 财务账单明细
 * @author JC
 *
 */
public interface FinancialAccountDetailService {
	
	/**
	 * 保存账单明细
	 * @param m
	 * @return
	 */
	public Message save(FinancialAccountDetailModel m);
	
	/**
	 * 查询财务账单明细
	 * @return
	 */
	public Message getListByAccountId(String id);
}
