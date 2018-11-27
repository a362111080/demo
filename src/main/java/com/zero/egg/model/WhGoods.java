package com.zero.egg.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author hhfeng
 * @Title: WhProject 
 * @Description: 仓库产品实体  
 * @date 2018年11月13日
 */
@ApiModel(value="WhGoods对象")
@Data
public class WhGoods implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//商品id
	@ApiModelProperty(name="goodsId",dataType="string",value="商品Id（新增时后台生成）")
	private String goodsId;
	//规格id
	@ApiModelProperty(name="standId",dataType="string",value="规格id")
	private String standId;
	//仓库id
	@ApiModelProperty(name="warehouseId",dataType="string",value="仓库id")
	private String warehouseId;
	//净重
	@ApiModelProperty(name="strrealWeight",dataType="decimal",value="净重")
	private BigDecimal strrealWeight;
	//状态（1正常，-1损坏，2已售）
	@ApiModelProperty(name="lngState",dataType="int",value="状态（1正常，-1损坏，2已售）",allowableValues="1,-1,2")
	private int lngState;
	//入库时间
	@ApiModelProperty(name="indate",dataType="date",value="入库时间")
	private Date indate;
	//商品来源商贩
	@ApiModelProperty(name="resourceSaler",dataType="string",value="商品来源商贩")
	private String resourceSaler;
	private String searchValue;
	
	//仓库实体
	@ApiModelProperty(required=false,name="warehouse")
	private Warehouse warehouse = new Warehouse();
	//规格信息实体
	@ApiModelProperty(required=false,name="standardDetl")
	private StandardDetl standardDetl = new StandardDetl();
	//规格类型实体
	@ApiModelProperty(required=false,name="standardData")
	private StandardData standardData = new StandardData();
	//品种实体
	@ApiModelProperty(required=false,name="eggType")
	private EggType eggType = new EggType();
	
	
	
}
