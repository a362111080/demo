package com.zero.egg.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @ClassName Category
 * @Description 鸡蛋类型
 * @Author lyming
 * @Date 2018/11/1 16:22
 **/
@Data
@TableName(value = "bd_category")
public class Category {

    /**
     * 主键id
     */
    @TableId(type = IdType.UUID)
    private String id;

    /**
     * 鸡蛋类别名称
     */
    private String name;

    /**
     * 创建人
     */
    private String creator;

    /**
     * 类别创建时间
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
     * 所属店铺id
     */
    private String shopId;

    /**
     * 店铺所属企业id
     */
    private String companyId;

    /**
     * 删除标识 0:未删除(默认) 1:已删除
     */
    private Integer dr = 0;
}
