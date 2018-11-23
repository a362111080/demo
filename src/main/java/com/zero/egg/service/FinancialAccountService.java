package com.zero.egg.service;

import java.util.List;

import com.zero.egg.model.FinancialAccountDetailModel;
import com.zero.egg.requestDTO.FinancialAccountRequestDTO;
import com.zero.egg.tool.Message;

/**
 * 财务账单
 * 
 * @author JC
 *
 */
public interface FinancialAccountService {

	/**
	 * 生成账单
	 * 
	 * @return
	 */
	public Message save(String name, String store, String type, List<FinancialAccountDetailModel> list, String note);

	/**
	 * 查询财务账单
	 * 
	 * @return
	 */
	public Message getList(FinancialAccountRequestDTO dto);

	/**
	 * 销账
	 * 
	 * @param accountId
	 * @return
	 */
	public Message cancelAccount(String accountId);

}
