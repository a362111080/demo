package com.zero.egg.requestDTO;

import com.zero.egg.tool.PageDTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TaskRequest extends PageDTO{

	 @ApiModelProperty(value = "店铺主键",required=false)
	    private String shopId;

	    @ApiModelProperty(value = "企业主键",required=false)
	    private String companyId;

	    @ApiModelProperty(value = "客商主键",required=false)
	    private String supplierId;

	    @ApiModelProperty(value = "状态",required=false)
	    private String status;
	    
	    @ApiModelProperty(value = "类型",required=false)
	    private String type;
	    
	    @ApiModelProperty(value = "设备号",required=false)
	    private String  equipmentNo;
}
