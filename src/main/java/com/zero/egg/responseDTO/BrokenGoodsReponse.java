package com.zero.egg.responseDTO;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.zero.egg.enums.BrokenGoodsEnums;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BrokenGoodsReponse implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 470768483200975000L;
	
	
	   @ApiModelProperty(value = "主键")
	    private String id;

	    @ApiModelProperty(value = "所属店铺")
	    private String shopId;

	    @ApiModelProperty(value = "所属企业")
	    private String companyId;

	    @ApiModelProperty(value = "客户主键")
	    private String customerId;
	    
	    @ApiModelProperty(value = "客户名称")
	    private String customerName;

	    @ApiModelProperty(value = "规格主键")
	    private String specificationId;
	    
	    @ApiModelProperty(value = "品种名称")
	    private String categoryName;
	    

	    @ApiModelProperty(value = "任务主键")
	    private String taskId;

	    @ApiModelProperty(value = "商品分类主键")
	    private String goodsCategroyId;

	    @ApiModelProperty(value = "商品编码")
	    private String goodsNo;
	    
	    @ApiModelProperty(value = "商品名称")
	    private String goodsName;

	    @ApiModelProperty(value = "标记")
	    private String marker;

	    @ApiModelProperty(value = "记重方式")
	    private String mode;

	    @ApiModelProperty(value = "重量")
	    private BigDecimal weight;

	    @ApiModelProperty(value = "业务员")
	    private String userId;

	    @ApiModelProperty(value = "换货订单号")
	    private String changeGoodsOrderNo;

	    @ApiModelProperty(value = "类型")
	    private String type;

	    @ApiModelProperty(value = "派送订单号")
	    private String shipmentGoodsOrderNo;

	    @ApiModelProperty(value = "换货商品状态")
	    private String status;

	    @ApiModelProperty(value = "备注")
	    private String remark;

	    @ApiModelProperty(value = "创建人")
	    private String creator;
	    
	    @ApiModelProperty(value = "创建人名")
	    private String creatorName;

	    @ApiModelProperty(value = "创建时间")
	    private Date createtime;
	    
	    public String getStatusName() {
	    	String statusName = "";
	    	if (StringUtils.isNotBlank(this.status)) {
	    		BrokenGoodsEnums.Status.note(Integer.parseInt(this.status));
			}
			return statusName;
		}

	    public String getTypeName() {
	    	String typeName = "";
	    	if (StringUtils.isNotBlank(this.type)) {
	    		BrokenGoodsEnums.Status.note(Integer.parseInt(this.type));
	    	}
	    	return typeName;
	    }

}
