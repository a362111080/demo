package com.zero.egg.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

/**
 * 出货model
 * 
 * @author JC
 *
 */
@Data
@TableName(value = "out_goods")
public class OutGoodsModel {
	// 主键id
	@TableId(type = IdType.UUID)
	private String id;

	// 品种Id
	private String eggTypeId;

	// 品种名称
	private String eggTypeName;

	// 单价
	private double price;

	// 标记
	private String flag;

	// 实重
	private double realWeight;

	// 员工id
	private String employeeId;

	// 员工名称
	private String employeeName;
}
