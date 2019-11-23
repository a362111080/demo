package com.zero.egg.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

/**
 * 财务账单统计
 * 
 * @author JC
 *
 */
@Data
@TableName(value = "financial_account")
public class FinancialAccountModel {

	// 主键id
	@TableId(type = IdType.UUID)
	private String id;

	// 创建人
	private String creater;

	// 创建时间
	private String createTime;

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

	// 数量
	private int number;

	// 金额
	private double amt;

	// 状态 1未结清 2已结清
	private String status;

	// 备注
	private String note;

}
