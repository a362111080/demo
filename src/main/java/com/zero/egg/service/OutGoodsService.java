package com.zero.egg.service;

import com.zero.egg.requestDTO.OutGoodsDTO;
import com.zero.egg.tool.Message;

public interface OutGoodsService {
	
	/**
	 * 出货
	 * 
	 * @return
	 */
	public Message outGoods(OutGoodsDTO dto);
	
	/**
	 * 出货信息展示
	 * 
	 * @return
	 */
	public Message showGoods();
	
	/**
	 * 结束出货
	 * 
	 * @return
	 */
	public Message endOutGoods();
	
	/**
	 * 单据打印
	 * 
	 * @return
	 */
	public Message infoPrint();
	
	/**
	 * 出货信息统计
	 * 
	 * @return
	 */
	public Message outGoodsStatistical();
}
