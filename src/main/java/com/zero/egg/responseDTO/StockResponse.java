package com.zero.egg.responseDTO;

import java.io.Serializable;
import java.math.BigDecimal;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class StockResponse implements Serializable{
	
	 /**
	 * 
	 */
	private static final long serialVersionUID = 5974812890795365264L;
	
	@ApiModelProperty(value = "仓库主键")
	private String stockId;
	
	@ApiModelProperty(value = "数量")
	private BigDecimal quantity;
	
	@ApiModelProperty(value = "规格主键")
	private String specificationId;
	
	@ApiModelProperty(value = "规格方案主键")
	private String programId;
	
	@ApiModelProperty(value = "判定最小称重")
	private String weightMin;
	
	@ApiModelProperty(value = "判定最大称重")
	private String weightMax;
	
	@ApiModelProperty(value = "标记")
	private String marker;
	
	@ApiModelProperty(value = "计重方式(1:去皮 2:包)")
	private String mode;
	
	@ApiModelProperty(value = "数值（去皮对应的数值）")
	private String numerical;
	
	@ApiModelProperty(value = "是否预警(0:否,1:是)")
	private String warn;

}
