package com.zero.egg.requestDTO;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.zero.egg.model.WhGoods;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
@ApiModel(value="WhGoodsDTO对象",description="商品对象")
@Data
public class WhGoodsDTO extends WhGoods implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4556531454816106812L;
	
	/**
	 * 模糊查询内容
	 */
	@ApiModelProperty(name="searchValue",dataType="string",value="搜索条件")
	private String searchValue;
	
	
	/**
	 * 仓库code
	 */
	@ApiModelProperty(name="warehouseId",dataType="string",value="仓库code",example="1-2")
	private String warehouseId;
	/**
	 * 仓库名
	 */
	@ApiModelProperty(name="warehouseName",dataType="string",value="仓库名",example="一号仓库")
	private String warehouseName;
	
	
	/**
     * 方案详情id
     */
	@ApiModelProperty(name="standardDetlId",dataType="string",value=" 方案详情id",example="1")
    private String standardDetlId;
    
    
    /**
     * 判定最小称重
     */
	@ApiModelProperty(name="weightMin",dataType="string",value="判定最小称重",example="42")
    private Integer weightMin;

    /**
     * 判定最大称重
     */
	@ApiModelProperty(name="weightMax",dataType="string",value="判定最大称重",example="43")
    private Integer weightMax;
    
    /**
     * 方案id
     */
	@ApiModelProperty(name="standardId",dataType="string",value="方案id",example="1")
    private String standardId;

    /**
     * 方案名称
     */
	@ApiModelProperty(name="standarName",dataType="string",value="方案名称",example="方案一")
    private String standarName;

    
    /**
     * 鸡蛋类别id
     */
	@ApiModelProperty(name="eggtypeId",dataType="string",value="鸡蛋类别id")
    private String eggtypeId;

    /**
     * 鸡蛋类别名称
     */
	@ApiModelProperty(name="eggtypeName",dataType="string",value="鸡蛋类别名称",example="红色")
    private String eggtypeName;

}
