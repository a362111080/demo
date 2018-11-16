package com.zero.egg.service;

import java.util.List;

import com.zero.egg.model.DamageGoods;
import com.zero.egg.model.WhGoods;
import com.zero.egg.tool.PageData;

/**
 * @author hhfeng
 * @Title: WhProductService 
 * @Description:  仓库产品接口
 * @date 2018年11月13日
 */
public interface WhGoodsService {

	/**
	 * 
	 *@title: getGoodsInfoById
	 *@Description 根据id获取产品
	 * @return
	 */
	WhGoods getGoodsInfoById(String id);
	
	/**
	 *@title: GoodsList
	 *@Description 商品列表（可条件查询）
	 * @return
	 */
	List<WhGoods> GoodsList();
	
	/**
	 * 
	 *@title: listDamageGoods
	 *@Description 损坏商品列表（可条件查询）
	 * @param pd
	 * @return
	 */
	List<PageData> listDamageGoods(PageData pd);
	
	/**
	 *@title: addGoods
	 *@Description 添加商品
	 * @param whGoods
	 * @return
	 */
	int addGoods(WhGoods whGoods);
	
	/**
	 * 
	 *@title: addDamageGoods
	 *@Description 添加损坏商品
	 * @param damageGoods
	 * @return
	 */
	int addDamageGoods(DamageGoods damageGoods);
	
	/**
	 *@title: updateGoods
	 *@Description 修改商品
	 * @param whGoods
	 * @return
	 */
	int updateGoods(WhGoods whGoods);
	
}
