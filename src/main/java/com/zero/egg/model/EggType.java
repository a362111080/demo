package com.zero.egg.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @ClassName EggType
 * @Description 鸡蛋类型
 * @Author lyming
 * @Date 2018/11/1 16:22
 **/
@Data
@TableName(value = "SMS_EggType_Data")
public class EggType {

    /**
     * 主键id
     */
    @TableId(type = IdType.UUID)
    private String id;

    /**
     * 鸡蛋类别编码
     */
    private String strEggTypeCode;

    /**
     * 鸡蛋类别名称
     */
    private String strEggTypeName;

    /**
     * 类别创建时间
     */
    private Date strTypeCreateTime;

    /**
     * 创建人
     */
    private String strCreateUser;

    /**
     * 状态 0:停用 1:启用(默认)
     */
    private Integer LngState = 1;

    /**
     * 前端传来的id数组
     */
    @TableField(exist = false)
    private List<String> ids;
}
