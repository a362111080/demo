package com.zero.egg.requestDTO;

import com.baomidou.mybatisplus.annotation.TableField;
import com.zero.egg.tool.PageDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class BrokenGoodsRequest extends PageDTO{

	
	 	@ApiModelProperty(value = "主键")
	    private String id;
	 	
	 	 @ApiModelProperty(value = "所属店铺")
	     private String shopId;

	     @ApiModelProperty(value = "所属企业")
	     private String companyId;

	     @ApiModelProperty(value = "客户主键")
	     private String customerId;
	
	 	@ApiModelProperty(value = "标记")
	    private String marker;

	    @ApiModelProperty(value = "记重方式")
	    private String mode;

	    @ApiModelProperty(value = "重量")
	    private BigDecimal weight;

	    @ApiModelProperty(value = "类型")
	    private String type;
	    
	    @ApiModelProperty(value = "换货商品状态")
	    private String status;

		@ApiModelProperty(value = "换货商品状态")
		private String goodsNo;

		@ApiModelProperty(value = "查询")
		private String keyword;


		@ApiModelProperty(value = "报损描述")
		private String remark;

		@ApiModelProperty(value = "二维码id",hidden=true)
		public String barcodeid;

}
