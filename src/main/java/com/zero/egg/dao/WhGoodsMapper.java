package com.zero.egg.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.zero.egg.model.DamageGoods;
import com.zero.egg.model.WhGoods;
import com.zero.egg.tool.PageData;

/**
 * @author hhfeng
 * @Title: WhGoodsMapper 
 * @Description:  商品mapper
 * @date 2018年11月13日
 */
@Mapper
@Repository
public interface WhGoodsMapper {
	/**
	 * 
	 *@title: getGoodsInfoById
	 *@Description 根据id获取产品
	 * @return
	 */
	WhGoods getGoodsInfoById(String id);
	
	/**
	 *@title: GoodsList
	 *@Description 商品列表
	 * @return
	 */
	List<WhGoods> GoodsList();
	
	/**
	 * 
	 *@title: listDamageGoods
	 *@Description 损坏商品列表
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
	 *@Description 新增损坏商品
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
