package com.zero.egg.model;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

/**
 * @author hhfeng
 * @Title: DamageGoods 
 * @Description:  损坏商品实体
 * @date 2018年11月16日
 */
@Data
@ApiModel(value="DamageGoods实体")
@ToString
public class DamageGoods implements Serializable{/**
	 * 
	 */
	private static final long serialVersionUID = -4321883921246252074L;
	/**
	 * 损坏id
	 */
	@ApiModelProperty(name="damageId",dataType="string",value="损坏id（新增时后台生成）")
	private String damageId;
	/**
	 * 商品id
	 */
	@ApiModelProperty(name="goodsId",dataType="string",value="商品Id")
	private String goodsId;
	/**
	 * 损坏时间
	 */
	@ApiModelProperty(name="damagedTime",dataType="date",value="损坏时间")
	private Date damagedTime;
	/**
	 * 损坏地点
	 */
	@ApiModelProperty(name="damagedPlace",dataType="string",value="损坏地点")
	private String damagedPlace;
	/**
	 * 损坏原因
	 */
	@ApiModelProperty(name="damagedReason",dataType="string",value="损坏原因")
	private String damagedReason;
	/**
	 * 登记人
	 */
	@ApiModelProperty(name="recorder",dataType="string",value="登记人")
	private String recorder;
	/**
	 * 登记时间
	 */
	@ApiModelProperty(name="recordTime",dataType="date",value="登记时间")
	private Date recordTime;
	/**
	 * 损坏类型 1自损 2报损
	 */
	@ApiModelProperty(name="damageType",dataType="int",value="损坏类型1自损 2报损")
	private String damageType;
	/**
	 * 仓库id
	 */
	@ApiModelProperty(name="warehouseId",dataType="string",value="仓库Id（查询时用）")
	private String warehouseId;

}
