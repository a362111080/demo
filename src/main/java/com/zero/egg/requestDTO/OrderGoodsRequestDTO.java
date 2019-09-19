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


    @ApiModelProperty(value="种类")
    private String categoryId;


    @ApiModelProperty(value="是否上架")
    private Boolean isOnSale;


}