package com.zero.egg.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

/**
 * 入货/卸货model
 * 
 * @author JC
 *
 */
@Data
@TableName(value = "incoming_goods")
public class IncomingGoodsModel {

	// 主键id
	@TableId(type = IdType.UUID)
	private String id;

	// 供应商id
	private String supplierId;

	// 供应商名称
	private String supplierName;

	// 方案id
	private String planId;

	// 方案名称
	private String planName;

	// 品种Id
	private String eggTypeId;

	// 品种名称
	private String eggTypeName;

	// 单价
	private double price;

	// 标记
	private String flag;

	// 方式
	private String way;

	// 实重
	private double realWeight;

	// 设备Id
	private String allocationId;

	// 设备名称
	private String allocationName;
}
