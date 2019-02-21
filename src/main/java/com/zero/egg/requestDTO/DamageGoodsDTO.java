package com.zero.egg.requestDTO;

import java.io.Serializable;

import com.zero.egg.model.DamageGoods;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DamageGoodsDTO  extends DamageGoods implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7152712335805574470L;

	/**
	 * 仓库id
	 */
	@ApiModelProperty(name="warehouseId",dataType="string",value="仓库Id（查询时用）")
	private String warehouseId;
	
	/**
	 * 商品id
	 */
		@ApiModelProperty(name="goodsId",dataType="string",value="商品Id（新增时后台生成）")
		private String goodsId;
	
	/**
	 * 商品名
	 */
	@ApiModelProperty(name="goodsName",dataType="string",value="商品名")
	private String goodsName;
	
	/**
	 * 仓库名
	 */
	@ApiModelProperty(name="warehouseName",dataType="string",value="仓库名",example="一号仓库")
	private String warehouseName;
}
