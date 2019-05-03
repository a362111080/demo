package com.zero.egg.responseDTO;

import java.math.BigDecimal;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ChangeGoodsReponse {

	
	 	@ApiModelProperty(value = "主键")
	    private String id;

	    @ApiModelProperty(value = "所属报损主键")
	    private String brokenId;

	    @ApiModelProperty(value = "客户主键")
	    private String customerId;
	    
	    @ApiModelProperty(value = "客户名称")
	    private String customerName;

	    @ApiModelProperty(value = "规格主键")
	    private String specificationId;
	    
	    @ApiModelProperty(value = "品种名称")
	    private String categoryName;

	    @ApiModelProperty(value = "商品分类主键")
	    private String goodsCategoryId;

	    @ApiModelProperty(value = "商品主键")
	    private String goodsNo;
	    
	    @ApiModelProperty(value = "商品名称")
	    private String goodsName;

	    @ApiModelProperty(value = "标记")
	    private String marker;

	    @ApiModelProperty(value = "记重方式")
	    private String mode;

	    @ApiModelProperty(value = "重量")
	    private BigDecimal weight;

	    @ApiModelProperty(value = "备注")
	    private String remark;

	    @ApiModelProperty(value = "创建人")
	    private String creator;
	    
	    @ApiModelProperty(value = "创建人名")
	    private String creatorName;

	    @ApiModelProperty(value = "创建时间")
	    private Date createtime;

	    @ApiModelProperty(value = "修改人")
	    private String modifier;

	    @ApiModelProperty(value = "修改时间")
	    private Date modifytime;

	    @ApiModelProperty(value = "删除标识")
	    private Boolean dr;
}
