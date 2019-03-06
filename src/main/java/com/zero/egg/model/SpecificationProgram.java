package com.zero.egg.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 规格方案
 *
 * @ClassName SpecificationProgram
 * @Author lyming
 * @Date 2018/11/9 13:43
 **/
@Data
@TableName(value = "bd_specification_program")
public class SpecificationProgram {
    /**
     * 主键id
     */
    @TableId(type = IdType.UUID)
    private String id;

    /**
     * 方案名称
     */
    private String name;

    /**
     * 品种id
     */
    private String categoryId;

    /**
     * 删除标识 0:未删除(默认) 1:已删除
     */
    private Integer dr = 0;

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
}