package com.zero.egg.model;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.ToString;

/**
 * @author hhfeng
 * @Title: DamageGoods 
 * @Description:  损坏商品实体
 * @date 2018年11月16日
 */
@Data
@ToString
public class DamageGoods implements Serializable{/**
	 * 
	 */
	private static final long serialVersionUID = -4321883921246252074L;
	//损坏id
	private String damage_id;
	//商品id
	private String goods_id;
	//损坏时间
	private Date damaged_time;
	//损坏地点
	private String damaged_place;
	//损坏原因
	private String damaged_reason;
	//登记人
	private String recorder;
	//登记时间
	private Date record_time;
	//损坏类型 1自损 2报损
	private String damage_type;

}
