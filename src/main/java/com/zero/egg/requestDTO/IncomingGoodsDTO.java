package com.zero.egg.requestDTO;

import lombok.Data;

@Data
public class IncomingGoodsDTO {
	//供应商id
	private String supplierId;
	
	//供应商名称
	private String supplierName;
	
	//方案id
	private String planId;
	
	//方案名称
	private String planName;
	
	//品种Id
	private String eggTypeId;
	
	//品种名称
	private String eggTypeName;
	
	//单价
	private double price;
	
	//标记
	private String flag;
	
	//方式
	private String way;
	
	//实重
	private double realWeight;
	
	//设备Id
	private String allocationId;
	
	//设备名称
	private String allocationName;
}
