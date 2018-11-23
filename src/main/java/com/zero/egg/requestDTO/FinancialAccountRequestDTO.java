package com.zero.egg.requestDTO;

import com.zero.egg.tool.PageDTO;

import lombok.Data;

/**
 * 账单查询条件封装model
 * @author JC
 *
 */
@Data
public class FinancialAccountRequestDTO extends PageDTO{
	
	private String id;

	// 编号
	private String accountNo;

	// 名称
	private String name;

	// 合作商
	private String store;

	// 进/出货 1进 2出
	private String type;

	// 品种名称
	private String eggTypeNames;

	// 状态 1未结清 2已结清
	private String status;

	// 备注
	private String note;
	
	private String beginTime;
	
	private String endTime;
}
