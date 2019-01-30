package com.zero.egg.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
     * 主键id
     */
    @TableId(type = IdType.UUID)
    private String id;

    /**
     * 鸡蛋类别名称
     */
    private String strEggtypeName;

    /**
     * 类别创建时间
     */
    private Date strTypeCreatetime;

    /**
     * 创建人
     */
    private String strCreateuser;

    /**
     * 状态 0:停用 1:启用(默认)
     */
    private Integer lngState = 1;

    /**
     * 所属店铺id
     */
    private String shopId;

    /**
     * 店铺所属企业id
     */
    private String enterpriseId;
}
