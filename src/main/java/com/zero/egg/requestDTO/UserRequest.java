package com.zero.egg.requestDTO;

import com.zero.egg.tool.PageDTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
@Data
public class UserRequest extends PageDTO{
	
		@ApiModelProperty(value = "员工主键",required=false)
	    private String id;
		
		@ApiModelProperty(value = "员工主键字符串(逗号隔开)",required=false)
		private String ids;
		
		@ApiModelProperty(value = "店铺主键",required=false)
		private String shopId;

	    @ApiModelProperty(value = "企业主键",required=false)
	    private String companyId;

	    @ApiModelProperty(value = "编码",required=false)
	    private String code;
	    
	    @ApiModelProperty(value = "状态（1正常,-1离职）",required=false)
	    private String status;
	    
	    @ApiModelProperty(value = "身份类型（1-PC端，2-Boss端，3-员工端，4-设备端）",required=false)
	    private Integer type;
	    
	    @ApiModelProperty(value = "姓名",required=false)
	    private String name;

	    @ApiModelProperty(value = "性别（0男，1女）",required=false)
	    private Integer sex;
}
