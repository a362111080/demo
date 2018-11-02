package com.zero.egg.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @ClassName EggType
 * @Description 鸡蛋类型
 * @Author lyming
 * @Date 2018/11/1 16:22
 **/
@Data
@TableName(value = "sms_eggtype_data")
public class EggType {

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
     * 状态 0:停用 1:启用
     */
    private int LngState;
}
