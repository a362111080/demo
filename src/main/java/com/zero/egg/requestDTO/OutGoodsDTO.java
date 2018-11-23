package com.zero.egg.requestDTO;

import lombok.Data;

@Data
public class OutGoodsDTO {

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
