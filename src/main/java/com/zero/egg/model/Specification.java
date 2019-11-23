package com.zero.egg.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 规格方案细节
 *
 * @ClassName Specification
 * @Author lyming
 * @Date 2018/11/9 14:32
 **/
@TableName(value = "bd_specification")
@Data
@ApiModel(value="规格方案细节对象", description="")
public class Specification {

    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键",required=false)
    @TableId(type = IdType.UUID)
    private String id;

    /**
     * 方案id
     */
    @ApiModelProperty(value = "方案id",required=false)
    private String programId;

    /**
     * 判定最小称重
     */
    @ApiModelProperty(value = "判定最小称重",required=false)
    private BigDecimal weightMin;

    /**
     * 判定最大称重
     */
    @ApiModelProperty(value = "判定最大称重",required=false)
    private BigDecimal weightMax;

    /**
     * 标记
     */
    @ApiModelProperty(value = "标记",required=false)
    private String marker;

    /**
     * 计重方式(1:去皮 2:包)
     */
    @ApiModelProperty(value = "计重方式(1:去皮 2:包)",required=false)
    private Integer mode;

    /**
     * 数值（去皮对应的数值）
     */
    @ApiModelProperty(value = "数值（去皮对应的数值）",required=false)
    private BigDecimal numerical;

    /**
     * 去皮时,拼接"实重("+重量区间+")"
     */
    @ApiModelProperty(value = "去皮时,拼接\"实重(\"+重量区间+\")\"", required = false)
    private String weightName;
    /**
     * 是否预警(0:否,1:是)
     */
    @ApiModelProperty(value = "是否预警(0:否,1:是)",required=false)
    private Integer warn;
    /**
     * 所属店铺id
     */
    @ApiModelProperty(value = "所属店铺id",required=false)
    private String shopId;

    /**
     * 店铺所属企业id
     */
    @ApiModelProperty(value = "店铺所属企业id",required=false)
    private String companyId;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人",required=false)
    private String creator;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间",required=false)
    private Date createtime;

    /**
     * 修改人
     */
    @ApiModelProperty(value = "修改人",required=false)
    private String modifier;

    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间",required=false)
    private Date modifytime;

    /**
     * 删除标识 0:未删除(默认) 1:已删除
     */
    @ApiModelProperty(value = "删除标识 0:未删除(默认) 1:已删除",required=false)
    private Integer dr = 0;
}
