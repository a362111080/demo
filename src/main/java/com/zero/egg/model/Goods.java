package com.zero.egg.model;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author Hhaifeng
 * @since 2019-04-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("bd_goods")
@ApiModel(value="Goods对象", description="")
public class Goods implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.UUID)
    private String id;

    @ApiModelProperty(value = "店铺主键")
    private String shopId;

    @ApiModelProperty(value = "企业主键")
    private String companyId;

    @ApiModelProperty(value = "供应商主键")
    private String supplierId;

    @ApiModelProperty(value = "规格主键")
    private String specificationId;

    @ApiModelProperty(value = "商品分类主键")
    private String goodsCategoryId;

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
    private LocalDateTime createtime;

    @ApiModelProperty(value = "更新人")
    private String modifier;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime modifytime;

    @ApiModelProperty(value = "删除标识")
    private Boolean dr;


}
