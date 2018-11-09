package com.zero.egg.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
@Data
@TableName(value = "SYS_Supplier_Data")
public class Supplier {

	/**
	 * @ClassName 合作单位类
	 * @Description 合作单位类
	 * @Author CQ
	 * @Date 2018/11/57
	 **/


	    /**
	     * 主键id
	     */
	    @TableId(type = IdType.UUID)
	    private String id;

	    /**
	     * 合作单位编码
	     */
	    private String strSupplierCode;

	    /**
	     * 合作单位名称
	     */
	    private String strSupplierName;

	    /**
	     * 合作单位简称
	     */
	    private String strSupplierShortName;

	    /**
	     * 合作单位类型
	     */
	    private String strSupplierKindCode;
	    
	    /**
	     * 合作单位联系方式
	     */
	    private String strSupplierPhone;
	    
	    
	    /**
	     * 合作单位联系人
	     */
	    private String strSupplierPerSon;
	    
	    
	    /**
	     * 合作单位备注
	     */
	    private String strSupplierDesc;
	    
	    /**
	     * 合作单位关联店铺
	     */
	    private String strStoreCode;
	    
	    /**
	     * 合作单位种类
	     */
	    private String strSupplierTypeCode;
	    
	    
	    /**
	     * 状态 0:停用 1:启用(默认)
	     */
	    private Integer LngState = 1;
}

