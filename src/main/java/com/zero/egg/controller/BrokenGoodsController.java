package com.zero.egg.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zero.egg.annotation.LoginToken;
import com.zero.egg.api.ApiConstants;
import com.zero.egg.enums.BrokenGoodsEnums;
import com.zero.egg.model.BrokenGoods;
import com.zero.egg.model.ChangeGoods;
import com.zero.egg.model.Goods;
import com.zero.egg.requestDTO.BrokenGoodsRequest;
import com.zero.egg.requestDTO.LoginUser;
import com.zero.egg.service.IBrokenGoodsService;
import com.zero.egg.service.IChangeGoodsService;
import com.zero.egg.service.IGoodsService;
import com.zero.egg.tool.Message;
import com.zero.egg.tool.UtilConstants;
import com.zero.egg.tool.UuidUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Hhaifeng
 * @since 2019-05-03
 */
@RestController
@Api(value="报损管理")
	@RequestMapping("/broken")
public class BrokenGoodsController {

	@Autowired
	IBrokenGoodsService brokenGoodsService;
	
	@Autowired
	IChangeGoodsService changeGoodsService;
	
	@Autowired IGoodsService goodsService;

	@Autowired
	private HttpServletRequest request;


	@LoginToken
	@ApiOperation(value="查询货物信息")
	@PostMapping(value="/goodview")
	public Message GetBrokenInfo(@RequestBody BrokenGoods brokenGoods)
	{
		Message message = new Message();
		if (brokenGoods.getType().equals(BrokenGoodsEnums.Type.BrokenByCustomer.index().toString()))
		{
			//报损
			Goods  broken=  brokenGoodsService.GetBrokenInfo(brokenGoods.getBrokenGoodsNo());
			if (null!=broken) {
				message.setData(broken);
				message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
				message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
			}
			else
			{
				message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
				message.setMessage("无商品信息");
			}

		}
		else
		{
			//自损
			Goods  broken=  brokenGoodsService.GetStoBrokenInfo(brokenGoods.getBrokenGoodsNo());
			if (null!=broken) {

				message.setData(broken);
				message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
				message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
			}
			else
			{
				message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
				message.setMessage("无商品信息");
			}
		}
		return message;
	}

	@LoginToken
	@ApiOperation(value="查询报损列表")
	@PostMapping(value="/brokentask")
	public Message GetBrokenTask(@RequestBody BrokenGoodsRequest Request) {
		Message message = new Message();
		PageHelper.startPage(Request.getCurrent().intValue(), Request.getSize().intValue());
		//获取报损任务
		List<BrokenGoods> newBroken=brokenGoodsService.GetBrokenTask(Request);
		PageInfo<BrokenGoods>  pageInfo = new PageInfo<>(newBroken);
		message.setData(pageInfo);
		message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
		message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
		return message;
	}

	@LoginToken
	@ApiOperation(value="扫码要替换的货")
	@PostMapping(value="/brokenstep1")
	public Message brokenstep1(@RequestBody BrokenGoodsRequest Request) {
		Message message = new Message();
		LoginUser user = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
		if (null != Request.getGoodsNo())
		{
			BrokenGoods  newBroken=new BrokenGoods();
			newBroken.setId(Request.getId());
			newBroken.setChangeGoodsNo(Request.getGoodsNo());
			newBroken.setStatus(BrokenGoodsEnums.Status.Normal.index().toString());
			newBroken.setUserId(user.getId());
			brokenGoodsService.updateById(newBroken);
			message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
			message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
		}
		else
		{
			message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
			message.setMessage(UtilConstants.ResponseMsg.FAILED);
		}
		return message;
	}

	@LoginToken
	@ApiOperation(value="扫码坏掉的货")
	@PostMapping(value="/brokenstep2")
	public Message brokenstep2(@RequestBody BrokenGoodsRequest Request) {
		Message message = new Message();
		LoginUser user = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
		if (null != Request.getGoodsNo())
		{
			BrokenGoods  newBroken=new BrokenGoods();
			newBroken.setId(Request.getId());
			newBroken.setBrokenGoodsNo(Request.getGoodsNo());
			newBroken.setStatus(BrokenGoodsEnums.Status.Disable.index().toString());
			newBroken.setUserId(user.getId());
			brokenGoodsService.updateById(newBroken);
			message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
			message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
		}
		else
		{
			message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
			message.setMessage(UtilConstants.ResponseMsg.FAILED);
		}
		return message;
	}



	@LoginToken
	@ApiOperation(value="新增报损")
	@PostMapping(value="/add")
	public Message<Object> add(@RequestBody @ApiParam(required=true,name="brokenGoods",value="损坏货物商品号") BrokenGoods brokenGoods) {
		Message<Object> message = new Message<Object>();
		LoginUser user = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
		try{

			if (brokenGoods.getType().equals(BrokenGoodsEnums.Type.BrokenByCustomer.index().toString()))
			{
				//报损
				Goods  broken=  brokenGoodsService.GetBrokenInfo(brokenGoods.getBrokenGoodsNo());
				//判断出货商品码是否存在
				if (null!=broken)
				{
					//货物存在，建立报损任务
					BrokenGoods  newBroken=new BrokenGoods();
					newBroken.setId(UuidUtil.get32UUID());
					newBroken.setShopId(user.getShopId());
					newBroken.setCompanyId(user.getCompanyId());
					newBroken.setSpecificationId(broken.getSpecificationId());
					newBroken.setTaskId(broken.getTaskId());
					newBroken.setGoodsNo(broken.getGoodsNo());
					newBroken.setGoodsCategroyId(broken.getGoodsCategoryId());
					newBroken.setMarker(broken.getMarker());
					newBroken.setMode(broken.getMode());
					newBroken.setWeight(broken.getWeight());
					newBroken.setUserId(brokenGoods.getUserId());
					newBroken.setBillNo(broken.getBillNo());
					newBroken.setCustomerId(broken.getCustomerId());
					newBroken.setType(BrokenGoodsEnums.Type.BrokenByCustomer.index().toString());
					newBroken.setStatus(BrokenGoodsEnums.Status.Normal.index().toString());
					newBroken.setCreatetime(new Date());
					newBroken.setModifytime(new Date());
					newBroken.setCreator(user.getId());
					newBroken.setModifier(user.getId());
					newBroken.setDr(false);
				    if (brokenGoodsService.save(newBroken))
					{
						message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
						message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
					}
				    else
					{
						message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
						message.setMessage(UtilConstants.ResponseMsg.FAILED);
					}
				}
				else
				{
					message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
					message.setMessage("输入的商品编码不存在");
				}
			}
			else
			{
				//自损
				Goods  broken=  brokenGoodsService.GetStoBrokenInfo(brokenGoods.getBrokenGoodsNo());
				if (null!=broken)
				{
					//自损扫码后直接完成，改变商品状态。
					BrokenGoods  newBroken=new BrokenGoods();
					newBroken.setId(UuidUtil.get32UUID());
					newBroken.setShopId(user.getShopId());
					newBroken.setCompanyId(user.getCompanyId());
					newBroken.setCustomerId(broken.getSupplierId());
					newBroken.setSpecificationId(broken.getSpecificationId());
					newBroken.setGoodsCategroyId(broken.getGoodsCategoryId());
					newBroken.setMarker(broken.getMarker());
					newBroken.setMode(broken.getMode());
					newBroken.setWeight(broken.getWeight());
					newBroken.setCustomerId(broken.getSupplierId());
					newBroken.setBillNo(broken.getBillNo());
					newBroken.setUserId(user.getId());
					newBroken.setBrokenGoodsNo(broken.getGoodsNo());
					newBroken.setGoodsNo(broken.getGoodsNo());
					newBroken.setType(BrokenGoodsEnums.Type.BrokenBySelf.index().toString());
					newBroken.setStatus(BrokenGoodsEnums.Status.Disable.index().toString());
					newBroken.setCreatetime(new Date());
					newBroken.setModifytime(new Date());
					newBroken.setCreator(user.getId());
					newBroken.setModifier(user.getId());
					newBroken.setDr(false);
					newBroken.setRemark(brokenGoods.getRemark());
					if (brokenGoodsService.save(newBroken))
					{
						//修改商品表状态
						brokenGoodsService.updateGoodsDr(newBroken.getBrokenGoodsNo());
						message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
						message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
					}
					else
					{
						message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
						message.setMessage(UtilConstants.ResponseMsg.FAILED);
					}
				}
				else
				{
					message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
					message.setMessage("商品编码不存在");
				}
			}

		 } catch (Exception e) {
			message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
			message.setMessage(UtilConstants.ResponseMsg.FAILED);
		 }

		return message;
	}
	
	
	@LoginToken
	@ApiOperation(value="新增报损换货新商品")
	@PostMapping(value="/addchangegoods")
	public Message<Object> addChangeGoods(
			@RequestBody @ApiParam(required=true,name="brokenGoods",value="报损主键，客户主键,商品编码") ChangeGoods changeGoods
			,HttpServletRequest request) {
		Message<Object> message = new Message<>();
		String uuid = UuidUtil.get32UUID();
		changeGoods.setId(uuid);
		changeGoods.setDr(false);
		QueryWrapper<Goods> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("goods_no", changeGoods.getGoodsNo());
		Goods goods =goodsService.getOne(queryWrapper);
		changeGoods.setSpecificationId(goods.getSpecificationId());
		changeGoods.setMarker(goods.getMarker());
		changeGoods.setMode(goods.getMode());
		changeGoods.setWeight(goods.getWeight());
		changeGoods.setCreatetime(new Date());
		changeGoods.setModifytime(new Date());
		//当前登录用户
		LoginUser loginUser = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
		changeGoods.setModifier(loginUser.getId());
		changeGoods.setCreator(loginUser.getId());
		if (changeGoodsService.save(changeGoods)) {
			message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
			message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
		}else {
			message.setMessage(UtilConstants.ResponseMsg.PARAM_MISSING);
			message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
		}
		return message;
	}
	
	@LoginToken
	@ApiOperation(value="修改报损状态")
	@PostMapping(value="/changestutus")
	public Message<Object> changeStutus(
			@RequestBody @ApiParam(required=true,name="brokenGoods",value="报损主键，状态（1开始售后，-1完成售后，0 正在售后）") BrokenGoods brokenGoods
			) {
		Message<Object> message = new Message<>();
		if (brokenGoodsService.updateById(brokenGoods)) {
			message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
			message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
		}else {
			message.setMessage(UtilConstants.ResponseMsg.PARAM_MISSING);
			message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
		}
		return message;
	}
	
}
