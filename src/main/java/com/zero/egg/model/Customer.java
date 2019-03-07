package com.zero.egg.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;
@Data
@TableName(value = "bd_customer")
public class Customer {

    /**
     * @ClassName 客户类
     * @Description 客户类
     * @Author CQ
     * @Date 2019/02/27
     **/

    /**
     * 主键id
     */
    @TableId(type = IdType.UUID)
    private String id;

    /**
     * 供应商关联店铺
     */
    private  String shopid;
    /**
     * 供应商关联企业
     */
    private  String companyid;

    /**
     * 供应商编码
     */
    private String code;

    /**
     * 供应商名称
     */
    private String name;

    /**
     * 供应商简称
     */
    private String shortname;

    /**
     * 供应商类型
     */
    private String type;

    /**
     * 供应商联系方式
     */
    private String phone;


    /**
     * 供应商联系人
     */
    private String linkman;


    /**
     * 供应商备注
     */
    private String remark;


    /**
     * 状态 0:停用 1:启用(默认)
     */
    private String status;

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
     * 省市区id
     */
    private String cityid;
}
