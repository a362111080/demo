package com.zero.egg.requestDTO;

import com.zero.egg.tool.PageDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@Data
public class OrderGoodsRequestDTO extends PageDTO implements Serializable {

    private static final long serialVersionUID = -7115238017672580978L;
    @ApiModelProperty(value="主键")
    private String id;

    @ApiModelProperty(value = "商品名称")
    private  String name;

    @ApiModelProperty(value="企业")
    public String companyId;

    @ApiModelProperty(value="店铺")
    public String shopId;

    @ApiModelProperty(value="店铺")
    public String goodsSn;

    @ApiModelProperty(value="种类")
    private String categoryId;


    @ApiModelProperty(value="是否上架")
    private Boolean isOnSale;


    @ApiModelProperty(value="送货员")
    private String dispatchman;

    @ApiModelProperty(value="删除标识")
    private Boolean dr;

    @ApiModelProperty(value="订单编号")
    private String orderSn;

    @ApiModelProperty(value="订单状态")
    private String orderstatus;
}
