package com.zero.egg.service;

import com.zero.egg.requestDTO.IncomingGoodsDTO;
import com.zero.egg.tool.Message;

public interface IncomingGoodsService {
	
	/**
	 * 卸货入库
	 * 
	 * @return
	 */
	public Message saveGoods(IncomingGoodsDTO dto);
	
	/**
	 * 卸货信息展示
	 * 
	 * @return
	 */
	public Message showGoods();
	
	/**
	 * 暂停卸货
	 * 
	 * @return
	 */
	public Message stopIncomingGoods();
	
	/**
	 * 结束卸货
	 * 
	 * @return
	 */
	public Message endIncomingGoods();
	
	/**
	 * 单据打印
	 * 
	 * @return
	 */
	public Message infoPrint();
	
	/**
	 * 卸货信息统计
	 * 
	 * @return
	 */
	public Message incomingGoodsStatistical();
}
