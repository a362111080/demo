package com.zero.egg.controller;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zero.egg.api.ApiConstants;
import com.zero.egg.api.dto.BaseResponse;
import com.zero.egg.api.dto.response.ListResponse;
import com.zero.egg.enums.CompanyEnums;
import com.zero.egg.model.Company;
import com.zero.egg.service.ICompanyService;
import com.zero.egg.tool.StringTool;
import com.zero.egg.tool.UuidUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * <p>
 *  企业控制器
 * </p>
 *
 * @author Hhaifeng
 * @since 2019-03-09
 */
@RestController
@Api(value="企业管理")
@RequestMapping("/company")
public class CompanyController {

	@Autowired
	private ICompanyService iCompanyService;
	
	@ApiOperation(value="查询企业")
	@RequestMapping(value="/list.data",method=RequestMethod.POST)
	public ListResponse<Company> list(@RequestParam @ApiParam(required =true,name ="pageNum",value="页码") int pageNum,
			@RequestParam @ApiParam(required=true,name="pageSize",value="页大小") int pageSize,
			@RequestBody @ApiParam(required=false,name="company",value="查询字段：关键词（名称 、编号）、状态") Company company) {
		ListResponse<Company> response = new ListResponse<>(ApiConstants.ResponseCode.EXECUTE_ERROR, ApiConstants.ResponseMsg.EXECUTE_ERROR);
		Page<Company> page = new Page<>();
		page.setPages(pageNum);
		page.setSize(pageSize);
		QueryWrapper<Company> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("dr", false);//查询未删除信息
		if (company != null) {
			queryWrapper.like(StringUtils.isNotBlank(company.getName()),"name", company.getName())
			.like(StringUtils.isNotBlank(company.getCode()),"code", company.getCode())
			.eq(StringUtils.isNotBlank(company.getStatus()), "status", company.getStatus());
		}
		IPage<Company> list = iCompanyService.page(page, queryWrapper);
		response.getData().setData(list.getRecords());
		response.getData().setTotal(list.getTotal());
		return response;
		
	}
	
	@ApiOperation(value="根据Id查询企业")
	@RequestMapping(value="/get.data",method=RequestMethod.POST)
	public BaseResponse<Object> getById(@RequestParam @ApiParam(required=true,name="id",value="企业id") String id) {
		BaseResponse<Object> response = new BaseResponse<>(ApiConstants.ResponseCode.EXECUTE_ERROR, ApiConstants.ResponseMsg.EXECUTE_ERROR);
		Company company = iCompanyService.getById(id);
		if (company != null) {
			response.setCode(ApiConstants.ResponseCode.SUCCESS);
			response.setMsg("查询成功");
			response.setData(company);
		}else {
			response.setMsg(ApiConstants.ResponseMsg.NULL_DATA);
		}
		return response;
	}
	
	@ApiOperation(value="新增企业")
	@RequestMapping(value="/add.do",method=RequestMethod.POST)
	public BaseResponse<Object> add(@RequestBody @ApiParam(required=true,name="company",value="企业信息:编号，名称，电话") Company company
			,HttpSession session) {
		BaseResponse<Object> response = new BaseResponse<>(ApiConstants.ResponseCode.EXECUTE_ERROR, ApiConstants.ResponseMsg.EXECUTE_ERROR);
		company.setId(UuidUtil.get32UUID());
		company.setCreatetime(LocalDateTime.now());
		company.setModifytime(LocalDateTime.now());
		company.setStatus(CompanyEnums.Status.Normal.index().toString());
		/*LoginInfo loginUser = (LoginInfo) session.getAttribute(SysConstants.LOGIN_USER);*/
		company.setModifier("1");
		company.setCreator("1");
		company.setDr(false);
		if (iCompanyService.save(company)) {
			response.setCode(ApiConstants.ResponseCode.SUCCESS);
			response.setMsg("添加成功");
		}
		return response;
	}
	
	@ApiOperation(value="根据id修改企业信息")
	@RequestMapping(value="/edit.do",method=RequestMethod.POST)
	public BaseResponse<Object> edit(@RequestBody  @ApiParam(required=true,name="company",value="企业信息：编号，名称，电话") Company company,HttpSession session) {
		BaseResponse<Object> response = new BaseResponse<>(ApiConstants.ResponseCode.EXECUTE_ERROR, ApiConstants.ResponseMsg.EXECUTE_ERROR);
		if (iCompanyService.updateById(company)) {
			response.setCode(ApiConstants.ResponseCode.SUCCESS);
			response.setMsg("修改成功");
		}
		return response;
	}
	
	@ApiOperation(value="停用企业")
	@RequestMapping(value="/stopcompany.do",method=RequestMethod.POST)
	public BaseResponse<Object> edit(@RequestParam @ApiParam(required=true,name="id",value="企业id") String id) {
		BaseResponse<Object> response = new BaseResponse<>(ApiConstants.ResponseCode.EXECUTE_ERROR, ApiConstants.ResponseMsg.EXECUTE_ERROR);
		Company company = new Company();
		company.setStatus(CompanyEnums.Status.Disable.index().toString());
		company.setId(id);
		if (iCompanyService.updateById(company)) {
			response.setCode(ApiConstants.ResponseCode.SUCCESS);
			response.setMsg("修改成功");
		}
		return response;
	}
	
	
	
	@ApiOperation(value="根据id删除企业信息")
	@RequestMapping(value="/del.do",method=RequestMethod.POST)
	public BaseResponse<Object> del(@RequestParam @ApiParam(required=true,name="id",value="企业id") String id) {
		BaseResponse<Object> response = new BaseResponse<>(ApiConstants.ResponseCode.EXECUTE_ERROR, ApiConstants.ResponseMsg.EXECUTE_ERROR);
		Company company = new Company();
		company.setId(id);
		company.setDr(true);
		if (iCompanyService.updateById(company)) {//逻辑删除
			response.setCode(ApiConstants.ResponseCode.SUCCESS);
			response.setMsg("删除成功");
		}
		return response;
	}
	
	@ApiOperation(value="批量删除企业信息")
	@RequestMapping(value="/batchdel.do",method=RequestMethod.POST)
	public BaseResponse<Object> batchDel(@RequestParam @ApiParam(required=true,name="ids",value="企业ids,逗号拼接") String ids) {
		BaseResponse<Object> response = new BaseResponse<>(ApiConstants.ResponseCode.EXECUTE_ERROR, ApiConstants.ResponseMsg.EXECUTE_ERROR);
		List<String> idsList = StringTool.splitToList(ids, ",");
		if (idsList !=null) {
			List<Company> companyList = new ArrayList<>();
			for (String id : idsList) {
				Company company = new Company();
				company.setId(id);
				company.setDr(true);
				companyList.add(company);
			}
			if (iCompanyService.updateBatchById(companyList)) {//逻辑删除
				response.setCode(ApiConstants.ResponseCode.SUCCESS);
				response.setMsg("删除成功");
			}
		}else {
			response.setMsg(ApiConstants.ResponseMsg.NULL_DATA);
		}
		
		return response;
	}
	
	
	
}