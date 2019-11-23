package com.zero.egg.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

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
 * @since 2019-05-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("bd_change_goods")
@ApiModel(value="ChangeGoods对象", description="")
public class ChangeGoods implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.UUID)
    private String id;

    @ApiModelProperty(value = "所属报损主键")
    private String brokenId;

    @ApiModelProperty(value = "客户主键")
    private String customerId;

    @ApiModelProperty(value = "规格主键")
    private String specificationId;

    @ApiModelProperty(value = "商品分类主键")
    private String goodsCategoryId;

    @ApiModelProperty(value = "商品主键")
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

    @ApiModelProperty(value = "修改人")
    private String modifier;

    @ApiModelProperty(value = "修改时间")
    private Date modifytime;

    @ApiModelProperty(value = "删除标识")
    private Boolean dr;


}
