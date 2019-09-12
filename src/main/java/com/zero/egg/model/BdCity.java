package com.zero.egg.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author lym
 */
@ApiModel(value="com.zero.egg.model.BdCity")
@Data
@TableName(value = "bd_city")
public class BdCity implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.INPUT)
    @ApiModelProperty(value="主键")
    private String id;

    /**
     * 父级主键
     */
    @TableField(value = "parent_id")
    @ApiModelProperty(value="父级主键")
    private String parentId;

    /**
     * 名称
     */
    @TableField(value = "name")
    @ApiModelProperty(value="名称")
    private String name;

    /**
     * 合并名称
     */
    @TableField(value = "merger_name")
    @ApiModelProperty(value="合并名称")
    private String mergerName;

    /**
     * 简称
     */
    @TableField(value = "short_name")
    @ApiModelProperty(value="简称")
    private String shortName;

    /**
     * 合并简称
     */
    @TableField(value = "merger_short_name")
    @ApiModelProperty(value="合并简称")
    private String mergerShortName;

    /**
     * 级别
     */
    @TableField(value = "level_type")
    @ApiModelProperty(value="级别")
    private String levelType;

    /**
     * 城市编号
     */
    @TableField(value = "city_code")
    @ApiModelProperty(value="城市编号")
    private String cityCode;

    /**
     * 邮编
     */
    @TableField(value = "zip_code")
    @ApiModelProperty(value="邮编")
    private String zipCode;

    /**
     * 拼音
     */
    @TableField(value = "pinyin")
    @ApiModelProperty(value="拼音")
    private String pinyin;

    /**
     * 简拼
     */
    @TableField(value = "jianpin")
    @ApiModelProperty(value="简拼")
    private String jianpin;

    /**
     * 首字符
     */
    @TableField(value = "first_char")
    @ApiModelProperty(value="首字符")
    private String firstChar;

    /**
     * 经度
     */
    @TableField(value = "lng")
    @ApiModelProperty(value="经度")
    private String lng;

    /**
     * 维度
     */
    @TableField(value = "lat")
    @ApiModelProperty(value="维度")
    private String lat;

    /**
     * 备注
     */
    @TableField(value = "remarks")
    @ApiModelProperty(value="备注")
    private String remarks;

    private static final long serialVersionUID = 1L;
}