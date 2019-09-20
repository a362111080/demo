package com.zero.egg.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zero.egg.annotation.LoginToken;
import com.zero.egg.api.ApiConstants;
import com.zero.egg.enums.BrokenGoodsEnums;
import com.zero.egg.model.BarCode;
import com.zero.egg.model.BrokenGoods;
import com.zero.egg.model.Goods;
import com.zero.egg.model.Stock;
import com.zero.egg.requestDTO.BrokenGoodsRequest;
import com.zero.egg.requestDTO.LoginUser;
import com.zero.egg.service.IBrokenGoodsService;
import com.zero.egg.service.IChangeGoodsService;
import com.zero.egg.service.IGoodsService;
import com.zero.egg.service.IStockService;
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
import java.math.BigDecimal;
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
	@Autowired
	private IStockService stockService;

	@LoginToken
	@ApiOperation(value="查询货物信息")
	@PostMapping(value="/goodview")
	public Message GetBrokenInfo(@RequestBody BrokenGoods brokenGoods)
	{
		Message message = new Message();
		LoginUser user = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
		brokenGoods.setShopId(user.getShopId());
		if(null !=brokenGoods.getBarcodeid())
		{
			BarCode  bar= brokenGoodsService.GetBarCodeInfo(brokenGoods);
			brokenGoods.setBrokenGoodsNo(bar.getCurrentCode());
		}
		if (null==brokenGoods.getDr())
		{
			brokenGoods.setDr(false);
		}
		if (brokenGoods.getType().equals(BrokenGoodsEnums.Type.BrokenByCustomer.index().toString()))
		{
			//PageHelper.startPage(brokenGoods.getCurrent().intValue(), brokenGoods.getSize().intValue());
			//报损

			List<Goods>  broken=  brokenGoodsService.GetBrokenInfo(brokenGoods);

			if (null!=broken) {
				//PageInfo<Goods>  pageInfo = new PageInfo<>(broken);
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
			Goods  broken=  brokenGoodsService.GetStoBrokenInfo(brokenGoods);
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
		LoginUser user = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
		PageHelper.startPage(Request.getCurrent().intValue(), Request.getSize().intValue());
		Request.setShopId(user.getShopId());
		Request.setCompanyId(user.getCompanyId());
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
		if(null !=Request.getBarcodeid())
		{
			BrokenGoods req=new BrokenGoods();
			req.setBarcodeid(Request.getBarcodeid());
			BarCode  bar= brokenGoodsService.GetBarCodeInfo(req);
			Request.setGoodsNo(bar.getCurrentCode());
		}
		if (null != Request.getGoodsNo())
		{
            //判断货物是否能出
            Goods  newGood=brokenGoodsService.isNewGood(Request.getGoodsNo());
            if (null!=newGood) {
                BrokenGoods newBroken = new BrokenGoods();
                newBroken.setId(Request.getId());
                newBroken.setChangeGoodsNo(Request.getGoodsNo());
                BrokenGoods data = brokenGoodsService.getById(Request.getId());

                if (null != data.getBrokenGoodsNo()) {
                    newBroken.setStatus(BrokenGoodsEnums.Status.Disable.index().toString());
                } else {
                    newBroken.setStatus(BrokenGoodsEnums.Status.Working.index().toString());
                }
                newBroken.setUserId(user.getId());
                if (null != Request.getRemark()) {
                    newBroken.setRemark(Request.getRemark());
                }
                brokenGoodsService.updateById(newBroken);
                brokenGoodsService.updateGoodsDr(newBroken.getChangeGoodsNo());

                Goods stockGood = brokenGoodsService.GetStoBrokenInfo(newBroken);
                //减去相应的库存
                QueryWrapper<Stock> stockWrapper = new QueryWrapper<>();
                stockWrapper.eq("specification_id", stockGood.getSpecificationId());
                Stock stock = stockService.getOne(stockWrapper);
                stock.setQuantity(stock.getQuantity().subtract(new BigDecimal(1)));
                stockService.updateById(stock);

                message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
                message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
            }
            else
            {
                message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
                message.setMessage("该商品已出货，不可售后出货");
            }
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

		if(null !=Request.getBarcodeid())
		{
			BrokenGoods req=new BrokenGoods();
			req.setBarcodeid(Request.getBarcodeid());
			BarCode  bar= brokenGoodsService.GetBarCodeInfo(req);
			Request.setGoodsNo(bar.getCurrentCode());
		}
		if (null != Request.getGoodsNo())
		{


			BrokenGoods  newBroken=new BrokenGoods();
			newBroken.setId(Request.getId());
			newBroken.setBrokenGoodsNo(Request.getGoodsNo());
			BrokenGoods data=brokenGoodsService.getById(Request.getId());
			if (null != data.getChangeGoodsNo()) {
				newBroken.setStatus(BrokenGoodsEnums.Status.Disable.index().toString());
			} else {
				newBroken.setStatus(BrokenGoodsEnums.Status.Working.index().toString());
			}
			newBroken.setUserId(user.getId());
			if (null != Request.getRemark()) {
				newBroken.setRemark(Request.getRemark());
			}
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

			brokenGoods.setShopId(user.getShopId());
			if (brokenGoods.getType().equals(BrokenGoodsEnums.Type.BrokenByCustomer.index().toString())) {
				//报损
				List<Goods> brokenList = brokenGoodsService.GetBrokenInfo(brokenGoods);
				//判断出货商品码是否存在
				if (brokenList.size() > 0) {
					List<BrokenGoods> HadBrokenList = brokenGoodsService.CheckBroken(brokenGoods.getBrokenGoodsNo());
					if (HadBrokenList.size() == 0) {
						Goods broken = brokenList.get(0);
						//判断账单
						if (broken.getBillstatus().equals("0")) {
							message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
							message.setMessage("出货账单未生成，不可报损！");
						}
						else
						{
							//货物存在，建立报损任务
							BrokenGoods newBroken = new BrokenGoods();
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
							newBroken.setUserId(user.getId());
							newBroken.setDr(false);
							if (brokenGoodsService.save(newBroken)) {
								message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
								message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
							} else {
								message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
								message.setMessage(UtilConstants.ResponseMsg.FAILED);
							}
						}
					} else {
						message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
						message.setMessage("该商品已经报损，不可重复报损");
					}
				} else {
					message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
					message.setMessage("商品编码不存在");
				}
			}
			else
			{
				//自损
				Goods  broken=  brokenGoodsService.GetStoBrokenInfo(brokenGoods);
				if (null!=broken) {
				    if (broken.getDr().equals(0)) {
                        List<BrokenGoods> HadBrokenList = brokenGoodsService.CheckBroken(brokenGoods.getBrokenGoodsNo());
                        if (HadBrokenList.size() == 0) {
                            //自损扫码后直接完成，改变商品状态。
                            BrokenGoods newBroken = new BrokenGoods();
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
                            if (brokenGoodsService.save(newBroken)) {
                                //修改商品表状态
                                brokenGoodsService.updateGoodsDr(newBroken.getBrokenGoodsNo());
                                //减去相应的库存
                                QueryWrapper<Stock> stockWrapper = new QueryWrapper<>();
                                stockWrapper.eq("specification_id", newBroken.getSpecificationId());
                                Stock stock = stockService.getOne(stockWrapper);
                                stock.setQuantity(stock.getQuantity().subtract(new BigDecimal(1)));
                                stockService.updateById(stock);
                                message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
                                message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
                            } else {
                                message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
                                message.setMessage(UtilConstants.ResponseMsg.FAILED);
                            }
                        } else {
                            message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
                            message.setMessage("该商品已经报损，不可重复报损");
                        }
                    }
				    else
                    {
                        message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
                        message.setMessage("该商品已经出货，不可进行自损处理");
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
