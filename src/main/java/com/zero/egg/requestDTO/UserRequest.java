package com.zero.egg.requestDTO;

import com.zero.egg.tool.PageDTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
@Data
public class UserRequest extends PageDTO{
	 @ApiModelProperty(value = "店铺主键",required=false)
	    private String shopId;

	    @ApiModelProperty(value = "企业主键",required=false)
	    private String companyId;

	    @ApiModelProperty(value = "编码",required=false)
	    private String code;
	    
	    @ApiModelProperty(value = "状态",required=false)
	    private String status;
	    
	    @ApiModelProperty(value = "姓名",required=false)
	    private String name;

	    @ApiModelProperty(value = "性别",required=false)
	    private Integer sex;
}
