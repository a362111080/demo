package com.zero.egg.responseDTO;


import com.zero.egg.model.OrderCategory;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderCategoryResponseDTO {

    @ApiModelProperty(value = "品种id",required=false)
    private String id;

    @ApiModelProperty(value = "品种名称",required=false)
    private String name;

    @ApiModelProperty(value = "品种父级id",required=false)
    private String pid;

    @ApiModelProperty(value = "企业主键",required=false)
    private String companyId;

    @ApiModelProperty(value = "店铺主键",required=false)
    private String shopId;

    @ApiModelProperty(value = "层级",required=false)
    private String level;

    @ApiModelProperty(value = "子种类集合",required=false)
    private List<OrderCategory> OrderCategoryList;

}

