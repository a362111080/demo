package com.zero.egg.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zero.egg.model.FinancialAccountModel;

/**
 * 账单
 * @author JC
 *
 */
public interface FinancialAccountMapper extends BaseMapper<FinancialAccountModel> {
	
	public FinancialAccountModel findById(String id);
}
