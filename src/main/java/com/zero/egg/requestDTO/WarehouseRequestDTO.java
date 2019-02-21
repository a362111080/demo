package com.zero.egg.requestDTO;

import java.io.Serializable;

import com.zero.egg.model.Warehouse;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ApiModel(value="warehouserequest对象")
@Data
@ToString
public class WarehouseRequestDTO extends Warehouse implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5082716555190247873L;
	/**
	 * 模糊查询内容
	 */
	@ApiModelProperty(name="searchValue",dataType="string",value="搜索条件")
	private String searchValue;

}
