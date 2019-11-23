package com.zero.egg.requestDTO;

import java.io.Serializable;

import com.zero.egg.tool.PageDTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class StockRequest extends PageDTO implements Serializable{
	
	 /**
	 * 
	 */
	private static final long serialVersionUID = 5974812890795365264L;
	
	@ApiModelProperty(value = "店铺主键",required=false)
    private String shopId;

    @ApiModelProperty(value = "企业主键",required=false)
    private String companyId;
	
	@ApiModelProperty(value = "库存主键",required=false)
	private String stockId;
	
	@ApiModelProperty(value = "库存数量",required=false)
	private String quantity;
	
	@ApiModelProperty(value = "规格主键",required=false)
	private String specificationId;
	
	@ApiModelProperty(value = "规格方案主键",required=false)
	private String programId;
	
	@ApiModelProperty(value = "品种主键",required=false)
	private String categoryId;
	
	@ApiModelProperty(value = "判定最小称重",required=false)
	private String weightMin;
	
	@ApiModelProperty(value = "判定最大称重",required=false)
	private String weightMax;
	
	@ApiModelProperty(value = "标记",required=false)
	private String marker;
	
	@ApiModelProperty(value = "方式",required=false)
	private String mode;
	
	@ApiModelProperty(value = "数值（去皮对应的数值）",required=false)
	private String numerical;
	
	@ApiModelProperty(value = "是否预警(0:否,1:是)",required=false)
	private String warn;
	
	

}
