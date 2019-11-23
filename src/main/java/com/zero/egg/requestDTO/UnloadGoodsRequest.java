package com.zero.egg.requestDTO;

import com.zero.egg.tool.PageDTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UnloadGoodsRequest extends PageDTO{
	 @ApiModelProperty(value = "店铺主键",required=false)
	    private String shopId;

	    @ApiModelProperty(value = "企业主键",required=false)
	    private String companyId;

	    @ApiModelProperty(value = "供应商主键",required=false)
	    private String supplierId;

	    @ApiModelProperty(value = "规格方案主键",required=false)
	    private String programId;

	    @ApiModelProperty(value = "任务主键",required=false)
	    private String taskId;
	    
	    @ApiModelProperty(value = "是否预警",required=false)
	    private Boolean warn;
}
