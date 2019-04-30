package com.zero.egg.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zero.egg.annotation.LoginToken;
import com.zero.egg.api.ApiConstants;
import com.zero.egg.api.dto.BaseResponse;
import com.zero.egg.api.dto.response.ListResponse;
import com.zero.egg.model.UnloadGoods;
import com.zero.egg.responseDTO.UnLoadCountResponseDto;
import com.zero.egg.responseDTO.UnLoadGoodsQueryResponseDto;
import com.zero.egg.responseDTO.UnLoadResponseDto;
import com.zero.egg.service.IUnloadGoodsService;
import com.zero.egg.tool.Message;
import com.zero.egg.tool.UtilConstants;
import com.zero.egg.tool.UuidUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.time.LocalDateTime;
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
	

	@ApiOperation(value="分页查询卸货商品")
	@RequestMapping(value="/unloadlist.data",method=RequestMethod.POST)
	public ListResponse<UnloadGoods> unloadList(@RequestParam @ApiParam(required =true,name ="pageNum",value="页码") int pageNum,
			@RequestParam @ApiParam(required=true,name="pageSize",value="页大小") int pageSize,
			@RequestBody @ApiParam(required=false,name="unloadGoods",value="查询字段：任务主键、方案（可选）") UnloadGoods unloadGoods) {
		ListResponse<UnloadGoods> response = new ListResponse<>(ApiConstants.ResponseCode.EXECUTE_ERROR, ApiConstants.ResponseMsg.EXECUTE_ERROR);
		Page<UnloadGoods> page = new Page<>();
		page.setPages(pageNum);
		page.setSize(pageSize);
		QueryWrapper<UnloadGoods> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("dr", false);//查询未删除信息
		if (unloadGoods != null) {

		    if(unloadGoods.getWarn()!=null) {

		    	queryWrapper.eq(StringUtils.isNotBlank(unloadGoods.getTaskId()), "task_id", unloadGoods.getTaskId())
                 .eq(StringUtils.isNotBlank(unloadGoods.getProgramId()), "program_id", unloadGoods.getProgramId())
                  .eq(StringUtils.isNotBlank(unloadGoods.getWarn().toString()), "warn", unloadGoods.getWarn());
            }
		    else
            {
                queryWrapper.eq(StringUtils.isNotBlank(unloadGoods.getTaskId()), "task_id", unloadGoods.getTaskId())
                        .eq(StringUtils.isNotBlank(unloadGoods.getProgramId()), "program_id", unloadGoods.getProgramId());
            }
		}
		IPage<UnloadGoods> list = unloadGoodsService.page(page, queryWrapper);
		response.getData().setData(list.getRecords());
		response.getData().setTotal(list.getTotal());
		return response;
		
	}
	
	@LoginToken
	@ApiOperation(value="新增卸货")
	@RequestMapping(value="/unloadadd.do",method=RequestMethod.POST)
	public BaseResponse<Object> unloadAdd(@RequestParam @ApiParam(required=true,name="specificationId",value="规格主键") String specificationId,
			@RequestBody @ApiParam(required=true,name="unloadGoods"
			,value="企业主键、店铺主键、供应商主键、规格方案主键、任务主键、商品分类主键、商品编码、重量、创建人") UnloadGoods unloadGoods
			,HttpSession session) {
		BaseResponse<Object> response = new BaseResponse<>(ApiConstants.ResponseCode.EXECUTE_ERROR, ApiConstants.ResponseMsg.EXECUTE_ERROR);
		unloadGoods.setId(UuidUtil.get32UUID());
		unloadGoods.setCreatetime(new Date());
		unloadGoods.setModifytime(new Date());
		/*LoginInfo loginUser = (LoginInfo) session.getAttribute(SysConstants.LOGIN_USER);*/
		unloadGoods.setModifier("1");
		unloadGoods.setCreator("1");
		unloadGoods.setDr(false);
		//TODO 根据规格查询数据，并进行预警处理
		if (unloadGoodsService.save(unloadGoods)) {
			response.setCode(ApiConstants.ResponseCode.SUCCESS);
			response.setMsg("添加成功");
		}
		return response;
	}

	@ApiOperation(value="新增卸货记录")
	@RequestMapping(value = "/addunloaddetl",method = RequestMethod.POST)
	public Message AddSupplier(@RequestBody  UnloadGoods model) {
		Message message = new Message();
		try {
			//实际根据界面传值
			model.setCreatetime(new Date());
			model.setModifytime(new Date());
			//根据重量对应规程方案判断是否预警
			if (null!=model.getWeight() && null !=model.getProgramId())
			{
				//小于方案最小称重则预警，返回标识以及是否预警结果
				UnLoadResponseDto  res=unloadGoodsService.CheckWeight(model.getWeight(),model.getProgramId());
				if (null!=res)
				{
					if(res.getNumerical().compareTo(BigDecimal.ZERO)!=0)
					{
						//存在去皮数值   显示标识为实际称重减去去皮值
						model.setMarker(model.getWeight().add(res.getNumerical()).toString());
						model.setWarn(false);

					}
					else
					{
						model.setMarker(res.getMarker());
						model.setWarn(false);
					}
				}
				else
				{
					//无返回结果代表小于最低称重范围，进行预警
					model.setMarker("");
					model.setWarn(true);
				}
			}

			int strval=unloadGoodsService.AddUnloadDetl(model);
			if (strval>0) {
				UnLoadCountResponseDto dto=new UnLoadCountResponseDto();
				dto.setMarker(model.getMarker());
				if(null != model.getTaskId())
				{
					//获取当前卸货任务已卸货数量，含本次
					int count=unloadGoodsService.GetTaskUnloadCount(model.getTaskId());
					dto.setCount(count);
					message.setData(dto);
				}

				message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
				message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
			}
			else
			{
				message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
				message.setMessage(UtilConstants.ResponseMsg.FAILED);

			}
			return message;

		} catch (Exception e) {
			message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
			message.setMessage(UtilConstants.ResponseMsg.FAILED);
			return message;
		}
	}



	@ApiOperation(value="根据任务id,按品种、方案分组统计本次卸货数量")
	@RequestMapping(value = "/queryunloadgoods",method = RequestMethod.POST)
	public Message QueryUnloadGood(@RequestBody  UnloadGoods model)
	{
		Message ms = new Message();
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
}
