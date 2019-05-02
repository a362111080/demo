package com.zero.egg.requestDTO;

import com.zero.egg.tool.PageDTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ShopRequest extends PageDTO{

	 @ApiModelProperty(value = "编码",required=false)
	    private String code;

	    @ApiModelProperty(value = "名称",required=false)
	    private String name;

	    @ApiModelProperty(value = "地址",required=false)
	    private String address;

	    @ApiModelProperty(value = "公司主键",required=false)
	    private String companyId;

	    @ApiModelProperty(value = "联系方式",required=false)
	    private String phone;
	    
	    @ApiModelProperty(value = "状态",required=false)
	    private String status;
	
}
