package com.zero.egg.controller;


import java.time.LocalDateTime;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.type.TrueFalseType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sun.xml.bind.v2.TODO;
import com.zero.egg.annotation.LoginToken;
import com.zero.egg.api.ApiConstants;
import com.zero.egg.api.dto.BaseResponse;
import com.zero.egg.api.dto.response.ListResponse;
import com.zero.egg.enums.TaskEnums;
import com.zero.egg.model.Task;
import com.zero.egg.model.TaskProgram;
import com.zero.egg.model.UnloadGoods;
import com.zero.egg.service.IUnloadGoodsService;
import com.zero.egg.tool.UuidUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

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
	
	@LoginToken
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
			queryWrapper.eq(StringUtils.isNotBlank(unloadGoods.getTaskId()),"task_id", unloadGoods.getTaskId())
			.eq(StringUtils.isNotBlank(unloadGoods.getProgramId()),"program_id", unloadGoods.getProgramId());
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
		unloadGoods.setCreatetime(LocalDateTime.now());
		unloadGoods.setModifytime(LocalDateTime.now());
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

}
