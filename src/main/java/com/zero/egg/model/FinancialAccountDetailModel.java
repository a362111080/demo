package com.zero.egg.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

/**
 * 财务账单明细
 * 
 * @author JC
 *
 */
@Data
@TableName(value = "financial_account_detail")
public class FinancialAccountDetailModel {

	// 主键id
	@TableId(type = IdType.UUID)
	private String id;

	// 创建人
	private String creater;

	// 创建时间
	private String createTime;

	// 账单id
	private String accountId;

	// 品种编码
	private String eggTypeNo;

	// 品种名称
	private String eggTypeName;

	// 标记
	private String tag;

	// 实重
	private double realWeight;

	// 单价
	private double price;

	// 数量
	private int number;

	// 小计金额
	private double amt;

}
