package com.zero.egg.controller;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zero.egg.annotation.LoginToken;
import com.zero.egg.api.ApiConstants;
import com.zero.egg.enums.TaskEnums;
import com.zero.egg.model.BarCode;
import com.zero.egg.model.UnloadGoods;
import com.zero.egg.requestDTO.LoginUser;
import com.zero.egg.requestDTO.UnloadGoodsRequest;
import com.zero.egg.responseDTO.UnLoadCountResponseDto;
import com.zero.egg.responseDTO.UnLoadGoodsQueryResponseDto;
import com.zero.egg.responseDTO.UnLoadResponseDto;
import com.zero.egg.service.IUnloadGoodsService;
import com.zero.egg.tool.Message;
import com.zero.egg.tool.StringTool;
import com.zero.egg.tool.UtilConstants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  卸货控制器
 * </p>
 *
 * @author Hhaifeng
 * @since 2019-03-25
 */
@RestController
@RequestMapping("/unload-goods")
@Api(value="卸货管理")
public class UnloadGoodsController {
	
	@Autowired
	private IUnloadGoodsService unloadGoodsService;
	@Autowired
	private HttpServletRequest request;
	

	@ApiOperation(value="分页查询卸货商品")
	@RequestMapping(value="/unloadlist.data",method=RequestMethod.POST)
	public Message  unloadList(
			@RequestBody @ApiParam(required=false,name="unloadGoods",value="查询字段：任务主键、方案（可选）,是否预警") UnloadGoodsRequest unloadGoods) {

		Message ms = new Message();
		PageHelper.startPage(unloadGoods.getCurrent().intValue(), unloadGoods.getSize().intValue());
		List<UnloadGoods> unloadList = unloadGoodsService.GetUnloadList(unloadGoods);

		//查询当前任务的 激活的品种id  方案id
		UnLoadGoodsQueryResponseDto ts=unloadGoodsService.GetTaskProgram(unloadGoods.getTaskId());

		ts.setId(unloadGoods.getTaskId());
		PageInfo<UnloadGoods> pageInfo = new PageInfo<>(unloadList);
		ms.setData(pageInfo);
		ms.setTotaldata(ts);
		ms.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
		ms.setMessage(UtilConstants.ResponseMsg.SUCCESS);
		return ms;
		
	}


	@LoginToken
	@ApiOperation(value="新增卸货记录")
	@RequestMapping(value = "/addunloaddetl",method = RequestMethod.POST)
	public Message<Object> AddSupplier(@RequestBody  UnloadGoods model) {
		Message<Object> message = new Message<Object>();
		LoginUser user = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
		try {

			model.setCreator(user.getId());
			model.setCreatetime(new Date());
			model.setModifier(user.getId());
			model.setModifytime(new Date());
			//根据二维码id 获取相关信息
			BarCode  bar=unloadGoodsService.GetBarCodeInfo(model.getQrCode());
			if (null !=bar) {

				//判断商品码是否存在
				if (unloadGoodsService.GoodNoIsExists(bar.getCurrentCode())>0)
				{
					//存在  终止操作，返回信息
					message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
					message.setMessage("请勿重复扫码！");
					return message;
				}

				model.setShopId(bar.getShopId());
				model.setCompanyId(bar.getCompanyId());
				model.setSupplierId(bar.getSupplierId());
				model.setGoodsCategoryId(bar.getCategoryId());
				model.setGoodsNo(bar.getCurrentCode());
				//判断当前卸货任务状态是否在执行中，通过供应商查找任务
				String info = unloadGoodsService.GetTaskStatusBySupplier(bar.getSupplierId());
				String taskId = StringTool.splitToList(info, ",").get(1);
				model.setTaskId(taskId);
				String status = StringTool.splitToList(info, ",").get(0);
				String Programid = StringTool.splitToList(info, ",").get(2);
				model.setProgramId(Programid);
				if (status == TaskEnums.Status.Unexecuted.toString()) {
					//任务已暂停
					message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
					message.setMessage("当前任务已暂停，请稍后再操作");
				} else {
					//根据重量对应规程方案判断是否预警
					if (null != model.getWeight() && null != model.getProgramId()) {
						//小于方案最小称重则预警，返回标识以及是否预警结果
						UnLoadResponseDto res = unloadGoodsService.CheckWeight(model.getWeight(),model.getProgramId());
						if (null != res) {
							model.setMode(res.getMode());
							model.setSpecificationId(res.getSpecificationId());
							if (res.getNumerical().compareTo(BigDecimal.ZERO) != 0) {
								//存在去皮数值   显示标识为实际称重减去去皮值
								model.setMarker(model.getWeight().add(res.getNumerical()).toString());


							} else {
								model.setMarker(res.getMarker());

							}
							model.setWarn(res.getWarn());
						} else {
							UnLoadResponseDto war = unloadGoodsService.CheckWeightForWarning(model.getProgramId());
							//无返回结果代表小于最低称重范围，进行预警，同时返回最小标识
							if (war.getNumerical().compareTo(BigDecimal.ZERO) != 0) {
								//存在去皮数值   显示标识为实际称重减去去皮值
								model.setMarker(model.getWeight().add(war.getNumerical()).toString());
							} else {
								model.setMarker(war.getMarker());
							}
							model.setMode(war.getMode());
							model.setSpecificationId(war.getSpecificationId());
							model.setWarn(res.getWarn());
						}
						int strval = unloadGoodsService.AddUnloadDetl(model);
						if (strval > 0) {
							UnLoadCountResponseDto dto = new UnLoadCountResponseDto();
							dto.setSupplierName(bar.getSupplierName());
							dto.setCategoryName(bar.getCategoryName());
							dto.setMarker(model.getMarker());
							dto.setWarn(model.getWarn());
							if (null != model.getTaskId()) {
								//获取当前卸货任务已卸货数量，含本次
								int count = unloadGoodsService.GetTaskUnloadCount(model.getTaskId());
								dto.setCount(count);
								message.setData(dto);
							}
							message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
							message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
						} else {
							message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
							message.setMessage(UtilConstants.ResponseMsg.FAILED);

						}
					}
					else
					{
						message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
						message.setMessage("参数错误！");

					}

				}
				return message;
			}
			else
			{
				message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
				message.setMessage("无效的二维码，请确认后再操作");
				return message;
			}

		} catch (Exception e) {
			message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
			message.setMessage(UtilConstants.ResponseMsg.FAILED);
			return message;
		}
	}



	@ApiOperation(value="根据任务id,按品种、方案分组统计本次卸货数量")
	@RequestMapping(value = "/queryunloadgoods",method = RequestMethod.POST)
	public Message<PageInfo<UnLoadGoodsQueryResponseDto>> QueryUnloadGood(@RequestBody  UnloadGoods model)
	{
		Message<PageInfo<UnLoadGoodsQueryResponseDto>> ms = new Message<PageInfo<UnLoadGoodsQueryResponseDto>>();
		PageHelper.startPage(1, 999);
		List<UnLoadGoodsQueryResponseDto> ResponseDto=unloadGoodsService.QueryUnloadGood(model.getTaskId());
		PageInfo<UnLoadGoodsQueryResponseDto> pageInfo = new PageInfo<>(ResponseDto);
		ms.setData(pageInfo);
		ms.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
		ms.setMessage(UtilConstants.ResponseMsg.SUCCESS);
		return  ms;
	}


    @ApiOperation(value="根据当天日期、按店铺、品种统计今日卸货数量")
    @RequestMapping(value = "/queryunloadgoodsforday",method = RequestMethod.POST)
    public Message QueryUnloadGoodForDay(@RequestBody  UnloadGoods model)
    {
        Message ms = new Message();
        PageHelper.startPage(1, 999);
        if (null==model.getUnloadTime()) {
			model.setUnloadTime("2019-04-04");
		}
        List<UnLoadGoodsQueryResponseDto>  ResponseDto=unloadGoodsService.QueryUnloadGoodForDay(model.getShopId(),model.getUnloadTime());
        PageInfo<UnLoadGoodsQueryResponseDto> pageInfo = new PageInfo<>(ResponseDto);
        ms.setData(pageInfo);
        ms.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
        ms.setMessage(UtilConstants.ResponseMsg.SUCCESS);
        return  ms;
    }


	/**
	 * 从当前登录用户信息中检查shopId和companyId是否为空
	 *
	 */
	private boolean checkShopAndCompanyExist(LoginUser user, UnloadGoods UnloadGoods) {
		boolean flag = (null != UnloadGoods.getShopId() && null != UnloadGoods.getCompanyId()) ? true : false;
		UnloadGoods.setCompanyId(user.getCompanyId());
		UnloadGoods.setShopId(user.getShopId());
		UnloadGoods.setModifier(user.getName());
		UnloadGoods.setModifytime(new Date());
		return flag;
	}
}
