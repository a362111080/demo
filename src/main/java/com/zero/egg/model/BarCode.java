package com.zero.egg.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value = "bd_barcode")
@ApiModel(value="barcode对象", description="")
public class BarCode implements Serializable {

    private static final long serialVersionUID = -3485701086348884048L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.UUID)
    private String id;

    @ApiModelProperty(value = "店铺主键",required=false)
    private String shopId;

    @ApiModelProperty(value = "企业主键",required=false)
    private String companyId;

    @ApiModelProperty(value = "编号",required=false)
    private String code;

    @ApiModelProperty(value = "供应商主键",required=false)
    private String supplierId;

    /**
     * 供应商名
     */
    private String supplierName;

    @ApiModelProperty(value = "品种主键",required=false)
    private String categoryId;

    /**
     * 品种名
     */
    private String categoryName;

    @ApiModelProperty(value = "当前编号",required=false)
    private String currentCode;

    /**
     * 二维码图片地址
     */
    @ApiModelProperty(value = "二维码图片地址", required = false)
    private String matrixAddr;

    @ApiModelProperty(value = "创建人",hidden=true)
    private String creator;

    @ApiModelProperty(value = "创建时间",hidden=true)
    private Date createtime;

    @ApiModelProperty(value = "更新人",hidden=true)
    private String modifier;

    @ApiModelProperty(value = "更新时间",hidden=true)
    private Date modifytime;

    @ApiModelProperty(value = "删除标识",hidden=true)
    /**
     * 删除标识,默认为0
     */
    private Boolean dr = false;

    @ApiModelProperty(value = "条码状态",hidden=true)
    @TableField(exist = false)
    private String status;

}
