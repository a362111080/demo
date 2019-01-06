package com.zero.egg.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zero.egg.model.DamageGoods;
import com.zero.egg.model.Warehouse;
import com.zero.egg.model.WhGoods;
import com.zero.egg.requestDTO.DamageGoodsDTO;
import com.zero.egg.requestDTO.WhGoodsDTO;
import com.zero.egg.service.WhGoodsService;
import com.zero.egg.tool.Message;
import com.zero.egg.tool.PageData;
import com.zero.egg.tool.UuidUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

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
@Api(value="商品管理")
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
	@ApiOperation(value="查询商品",notes="根据id查询")
	@ApiImplicitParam(paramType="query",name="商品id",value="goodId",dataType="String")
	@RequestMapping(value="/getgoodsbyid",method=RequestMethod.GET)
	public Message getGoodsById(@RequestParam String goodId) {
		Message mg = new Message();
		WhGoods whGoods = whGoodsService.getGoodsInfoById(goodId);
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
	@ApiOperation(value="商品列表" ,notes="分页查询，条件查询")
	@RequestMapping(value="/goodslist",method=RequestMethod.POST)
	public Message goodsList(
			@RequestParam @ApiParam(required =true,name ="pageNum",value="页码") int pageNum,
			@RequestParam @ApiParam(required=true,name="pageSize",value="页大小") int pageSize,
			@RequestBody @ApiParam(required=false,name="whGoodsDTO",value="根据商品信息条件查询字段") WhGoodsDTO whGoodsDTO) {
		Message mg = new Message();
		PageHelper.startPage(pageNum, pageSize, "");
		List<WhGoodsDTO> list = whGoodsService.GoodsList(whGoodsDTO);
		PageInfo<WhGoodsDTO> pageInfo = new PageInfo<WhGoodsDTO>(list);
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
	@ApiOperation(value="损坏商品列表" , notes="分页查询，条件查询")
	@RequestMapping(value="/damagegoodslist",method=RequestMethod.POST)
	public Message DamageGoodsList(
			@RequestParam @ApiParam(required =true,name ="pageNum",value="页码") int pageNum,
			@RequestParam @ApiParam(required=true,name="pageSize",value="页大小") int pageSize,
			@RequestBody @ApiParam(required=false,name="whGoodsDTO",value="根据商品信息条件查询字段") DamageGoodsDTO damageGoodsDTO) {
		Message mg = new Message();
		PageHelper.startPage(pageNum, pageSize, "");
		List<DamageGoodsDTO> list = whGoodsService.listDamageGoods(damageGoodsDTO);
		PageInfo<DamageGoodsDTO> pageInfo = new PageInfo<DamageGoodsDTO>(list);
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
	
	@ApiOperation(value="添加商品",notes="goodsId,indate,lngState三个字段内容后台生成")
	@RequestMapping(value="/addGoods",method=RequestMethod.POST)
	public Message addGoods(@RequestBody @ApiParam(value="只要whgoods对象数据")  WhGoods goods) {
		Message mg = new Message();
		goods.setGoodsId(UuidUtil.get32UUID());
		goods.setIndate(new Date());
		goods.setLngState(GoodsState.inStore);
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
	@ApiOperation(value="添加损坏商品",notes="damageId,recordTime两个字段内容后台生成")
	@RequestMapping(value="/adddamagegoods",method=RequestMethod.POST)
	@Transactional(rollbackFor=Exception.class)
	public Message addDamageGoods(@RequestBody DamageGoods damageGoods) {
		Message mg = new Message();
		damageGoods.setDamageId(UuidUtil.get32UUID());
		damageGoods.setRecordTime(new Date());
		WhGoods whGoods = new WhGoods();
		//修改商品状态为损坏
		whGoods.setGoodsId(damageGoods.getGoodsId());
		whGoods.setLngState(GoodsState.damage);
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
	 *@Description 修改商品信息
	 * @param request
	 * @return
	 */
	@ApiOperation(value="修改商品信息",notes="goodId必填")
	@RequestMapping(value="/updateGoods",method=RequestMethod.POST)
	private Message updateGoods(@RequestBody WhGoods goods) {
		Message mg = new Message();
		String id = goods.getGoodsId() ;
		//判断是否有仓库id
		if (StringUtils.isNotBlank(id)) {
			int result = whGoodsService.updateGoods(goods);
			if (result==1) {
				mg.setData(goods);
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
