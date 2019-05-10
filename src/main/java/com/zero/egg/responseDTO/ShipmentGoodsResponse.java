package com.zero.egg.responseDTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class ShipmentGoodsResponse {
	
		@ApiModelProperty(value = "出货商品主键")
	    private String id;

		@ApiModelProperty(value = "店铺主键")
	    private String shopId;

		@ApiModelProperty(value = "企业主键")
	    private String companyId;

    @ApiModelProperty(value = "客户主键")
	    private String customerId;

		@ApiModelProperty(value = "规格主键")
	    private String specificationId;

		@ApiModelProperty(value = "任务主键")
	    private String taskId;

		@ApiModelProperty(value = "分类主键")
	    private String goodsCategoryId;

		@ApiModelProperty(value = "商品编码")
	    private String goodsNo;

		@ApiModelProperty(value = "标记")
	    private String marker;
	    
		@ApiModelProperty(value = "计重方式")
	    private String mode;

		@ApiModelProperty(value = "重量")
	    private BigDecimal weight;

		@ApiModelProperty(value = "备注")
	    private String remark;

		@ApiModelProperty(value = "创建人")
	    private String creator;

		@ApiModelProperty(value = "创建时间")
	    private Date createtime;

		@ApiModelProperty(value = "更改人")
	    private String modifier;

		@ApiModelProperty(value = "更改时间")
	    private Date modifytime;

		@ApiModelProperty(value = "删除标志")
	    private Boolean dr;
	    
		@ApiModelProperty(value = "规格方案名称")
	    private String programName;
	    
		@ApiModelProperty(value = "规格方案主键")
	    private String programId;
	    
		@ApiModelProperty(value = "品种名称")
	    private String categoryName;
	    
		@ApiModelProperty(value = "品种主键")
	    private String categoryId;
	    
		@ApiModelProperty(value = "操作员")
	    private String operator;
	    
		@ApiModelProperty(value = "数量")
	    private Integer count;

}
