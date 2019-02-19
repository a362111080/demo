package com.zero.egg.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 规格方案细节
 *
 * @ClassName StandardDetl
 * @Author lyming
 * @Date 2018/11/9 14:32
 **/
@TableName(value = "bd_specification")
@Data
public class StandardDetl {

    /**
     * 主键id
     */
    @TableId(type = IdType.UUID)
    private String id;

    /**
     * 方案id
     */
    private String programId;

    /**
     * 方案名称
     */
    private String name;

    /**
     * 判定最小称重
     */
    private BigDecimal weightMin;

    /**
     * 判定最大称重
     */
    private BigDecimal weightMax;

    /**
     * 标记
     */
    private String marker;

    /**
     * 计重方式(1:去皮 2:包)
     */
    private Integer mode;

    /**
     * 数值（去皮对应的数值）
     */
    private Integer numerical;

    /**
     * 是否预警
     */
    private Integer warn;
    /**
     * 所属店铺id
     */
    private String shopId;

    /**
     * 店铺所属企业id
     */
    private String companyId;

    /**
     * 创建人
     */
    private String creator;

    /**
     * 创建时间
     */
    private Date createtime;

    /**
     * 修改人
     */
    private String modifier;

    /**
     * 修改时间
     */
    private Date modifytime;

    /**
     * 删除标识 0:未删除(默认) 1:已删除
     */
    private Integer dr = 0;
}
