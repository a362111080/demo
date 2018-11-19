package com.zero.egg.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zero.egg.model.DamageGoods;
import com.zero.egg.model.WhGoods;
import com.zero.egg.service.WhGoodsService;
import com.zero.egg.tool.Message;
import com.zero.egg.tool.PageData;
import com.zero.egg.tool.UuidUtil;
import com.zero.egg.tool.UtilConstants.GoodsState;
import com.zero.egg.tool.UtilConstants.ResponseCode;
import com.zero.egg.tool.UtilConstants.ResponseMsg;

/**
 * @author hhfeng
 * @Title: WhProjectController 
 * @Description:商品控制器  
 * @date 2018年11月13日
 */
@RestController
@RequestMapping(value="/whgoods")
public class WhGoodsController {

	@Autowired
	private WhGoodsService whGoodsService;
	
	/**
	 *@title: getGoodsById
	 *@Description 根据id查询商品
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/getgoodsbyid")
	public Message getGoodsById(String id) {
		Message mg = new Message();
		WhGoods whGoods = whGoodsService.getGoodsInfoById(id);
		mg.setData(whGoods);
		mg.setMessage(ResponseMsg.SUCCESS);
		mg.setState(ResponseCode.SUCCESS_HEAD);
		return mg;
	}
	
	/**
	 * 
	 *@title: goodsList
	 *@Description 查询商品列表（可条件查询）
	 * @param pageNum 第几页
	 * @param pageSize 页大小
	 * @return
	 */
	@RequestMapping(value="/goodslist")
	public Message goodsList(int pageNum,int pageSize) {
		Message mg = new Message();
		PageHelper.startPage(pageNum, pageSize, "");
		List<WhGoods> list = whGoodsService.GoodsList();
		PageInfo<WhGoods> pageInfo = new PageInfo<WhGoods>(list);
		mg.setData(pageInfo);
		mg.setMessage(ResponseMsg.SUCCESS);
		mg.setState(ResponseCode.SUCCESS_HEAD);
		return mg;
	}
	
	/**
	 * 
	 *@title: DamageGoodsList
	 *@Description 损坏商品列表（可条件查询）
	 * @param request
	 * @param pageNum 第几页
	 * @param pageSize 页大小
	 * @return
	 */
	@RequestMapping(value="/damagegoodslist")
	public Message DamageGoodsList( HttpServletRequest request,int pageNum,int pageSize) {
		Message mg = new Message();
		PageData pd = new PageData(request);
		PageHelper.startPage(pageNum, pageSize, "");
		List<PageData> list = whGoodsService.listDamageGoods(pd);
		PageInfo<PageData> pageInfo = new PageInfo<PageData>(list);
		mg.setData(pageInfo);
		mg.setMessage(ResponseMsg.SUCCESS);
		mg.setState(ResponseCode.SUCCESS_HEAD);
		return mg;
	}
	
	/**
	 *@title: addGoods
	 *@Description 添加商品
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/addGoods")
	public Message addGoods(@RequestBody WhGoods goods,HttpServletRequest request) {
		Message mg = new Message();
		goods.setGoods_id(UuidUtil.get32UUID());
		goods.setIndate(new Date());
		goods.setLng_state(GoodsState.inStore);
		int result = whGoodsService.addGoods(goods);
		if (result==1) {
			mg.setData(goods);
			mg.setMessage(ResponseMsg.SUCCESS);
			mg.setState(ResponseCode.SUCCESS_HEAD);
		}else {
			mg.setMessage(ResponseMsg.FAILED);
			mg.setState(ResponseCode.EXCEPTION_HEAD);
		}
		return mg;
		
	}
	
	/**
	 * 
	 *@title: addDamageGoods
	 *@Description 添加损坏商品
	 * @param damageGoods
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/adddamagegoods")
	@Transactional(rollbackFor=Exception.class)
	public Message addDamageGoods(@RequestBody DamageGoods damageGoods,HttpServletRequest request) {
		Message mg = new Message();
		damageGoods.setGoods_id(UuidUtil.get32UUID());
		damageGoods.setRecord_time(new Date());
		WhGoods whGoods = new WhGoods();
		//修改商品状态为损坏
		whGoods.setGoods_id(damageGoods.getGoods_id());
		whGoods.setLng_state(GoodsState.damage);
		whGoodsService.updateGoods(whGoods);
		//添加损坏信息
		int result = whGoodsService.addDamageGoods(damageGoods);
		if (result==1) {
			mg.setData(damageGoods);
			mg.setMessage(ResponseMsg.SUCCESS);
			mg.setState(ResponseCode.SUCCESS_HEAD);
		}else {
			mg.setMessage(ResponseMsg.FAILED);
			mg.setState(ResponseCode.EXCEPTION_HEAD);
		}
		return mg;
		
	}
	
	
	
	
	/**
	 *@title: updateGoods
	 *@Description 修改仓库信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/updateGoods")
	private Message updateGoods(@RequestBody WhGoods Goods, HttpServletRequest request) {
		Message mg = new Message();
		PageData pd = new PageData(request);
		String id = pd.getString("id");
		//判断是否有仓库id
		if (StringUtils.isNotBlank(id)) {
			int result = whGoodsService.updateGoods(Goods);
			if (result==1) {
				mg.setData(Goods);
				mg.setMessage(ResponseMsg.SUCCESS);
				mg.setState(ResponseCode.SUCCESS_HEAD);
			}else {
				mg.setMessage(ResponseMsg.FAILED);
				mg.setState(ResponseCode.EXCEPTION_HEAD);
			}
		}else {
			mg.setMessage(ResponseMsg.FAILED);
			mg.setMessage(ResponseMsg.PARAM_MISSING);
		}
		return mg;
	}
	
}
