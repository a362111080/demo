package com.zero.egg.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

/**
 * @author hhfeng
 * @Title: WhProject 
 * @Description: 仓库产品实体  
 * @date 2018年11月13日
 */
@Data
public class WhGoods implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//商品id
	private String goods_id;
	//规格id
	private String stand_id;
	//仓库id
	private String warehous_id;
	//净重
	private BigDecimal strreal_weight;
	//状态（1正常，-1损坏，2已售）
	private int lng_state;
	//入库时间
	private Date indate;
	//商品来源商贩
	private String resource_saler;
	
	
	
	
}
