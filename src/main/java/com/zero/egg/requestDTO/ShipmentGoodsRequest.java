package com.zero.egg.requestDTO;

import com.zero.egg.tool.PageDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class ShipmentGoodsRequest extends PageDTO{

    @ApiModelProperty(value = "整理类型:1->列表 2->整理归类", required = false)
    private Integer sortType;

	@ApiModelProperty(value = "店铺主键",required=false)
    private String shopId;

    @ApiModelProperty(value = "企业主键",required=false)
    private String companyId;

    @ApiModelProperty(value = "客户主键",required=false)
    private String customerId;

    @ApiModelProperty(value = "规格主键",required=false)
    private String specificationId;

    @ApiModelProperty(value = "任务主键",required=false)
    private String taskId;
    
    @ApiModelProperty(value = "商品编号",required=false)
    private String goodsNo;
    
    @ApiModelProperty(value = "创建人",required=false)
    private String creator;

    @ApiModelProperty(value = "创建时间",required=false)
    private Date createtime;
}
