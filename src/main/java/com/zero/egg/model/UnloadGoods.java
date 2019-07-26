package com.zero.egg.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
 * @since 2019-03-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("bd_unload_goods")
@ApiModel(value="UnloadGoods对象", description="")
public class UnloadGoods implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键",required=false)
    @TableId(value = "id", type = IdType.UUID)
    private String id;

    @ApiModelProperty(value = "店铺主键",required=false)
    private String shopId;

    @ApiModelProperty(value = "企业主键",required=false)
    private String companyId;

    @ApiModelProperty(value = "供应商主键",required=false)
    private String supplierId;

    @ApiModelProperty(value = "方案主键",required=false)
    private String programId;

    @ApiModelProperty(value = "规格主键",required=false)
    private String specificationId;

    @ApiModelProperty(value = "任务主键",required=false)
    private String taskId;

    @ApiModelProperty(value = "商品分类主键",required=false)
    private String goodsCategoryId;

    @ApiModelProperty(value = "商品编码",required=false)
    private String goodsNo;

    @ApiModelProperty(value = "标记",required=false)
    private String marker;

    @ApiModelProperty(value = "记重方式",required=false)
    private String mode;

    @ApiModelProperty(value = "重量",required=false)
    private BigDecimal weight;

    @ApiModelProperty(value = "是否预警",required=false)
    private Boolean warn;

    @ApiModelProperty(value = "备注",required=false)
    private String remark;

    @ApiModelProperty(value = "创建人",hidden=true)
    private String creator;

    @ApiModelProperty(value = "创建时间",hidden=true)
    private Date createtime;

    @ApiModelProperty(value = "更新人",hidden=true)
    private String modifier;

    @ApiModelProperty(value = "更新时间",hidden=true)
    private Date modifytime;

    @ApiModelProperty(value = "删除标识",hidden=true)
    private Boolean dr;

    @TableField(exist = false)
    @ApiModelProperty(value = "统计日期(汇总卸货数量使用yyyy-mm-dd 字符)",hidden=true)
    private String UnloadTime;




    @TableField(exist = false)
    @ApiModelProperty(value = "查询使用  鸡蛋品种",hidden=true)
    private String categoryName;


    @TableField(exist = false)
    @ApiModelProperty(value = "二维码id",hidden=true)
    private String qrCode;

}
