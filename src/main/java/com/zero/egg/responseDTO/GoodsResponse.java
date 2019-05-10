package com.zero.egg.responseDTO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class GoodsResponse {

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.UUID)
    private String id;

    @ApiModelProperty(value = "店铺主键")
    private String shopId;

    @ApiModelProperty(value = "企业主键")
    private String companyId;

    @ApiModelProperty(value = "供应商主键")
    private String supplierId;

    @ApiModelProperty(value = "供应商")
    private String supplierName;

    @ApiModelProperty(value = "规格主键")
    private String specificationId;

    @ApiModelProperty(value = "商品分类主键")
    private String goodsCategoryId;

    @ApiModelProperty(value = "商品类别名")
    private String categoryName;

    @ApiModelProperty(value = "商品编码")
    private String goodsNo;

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

    @ApiModelProperty(value = "创建时间")
    private Date createtime;

    @ApiModelProperty(value = "更新人")
    private String modifier;

    @ApiModelProperty(value = "更新时间")
    private Date modifytime;

    @ApiModelProperty(value = "删除标识")
    private Boolean dr;

    @ApiModelProperty(value = "员工主键")
    private String employeeId;

    @ApiModelProperty(value = "员工姓名")
    private String employeeName;
}
