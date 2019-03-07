package com.zero.egg.controller;


import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.apache.ibatis.session.RowBounds;
import org.hibernate.service.spi.Wrapped;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zero.egg.api.dto.BaseResponse;
import com.zero.egg.api.dto.response.ListResponse;
import com.zero.egg.controller.BaseInfoController;
import com.zero.egg.model.Company;
import com.zero.egg.service.impl.CompanyServiceImpl;
import com.zero.egg.tool.UuidUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Hhaifeng
 * @since 2019-03-06
 */
@RestController
@Api(value="企业管理")
@RequestMapping("/company")
public class CompanyController extends BaseInfoController {

	CompanyServiceImpl companyServiceImpl  =  new CompanyServiceImpl();
	
	@ApiOperation(value="新增企业",notes="id后台自动生成")
	@RequestMapping(value="/add.do",method=RequestMethod.POST)
	public BaseResponse<Object> add(@RequestBody Company company) {
		BaseResponse<Object> response = new BaseResponse<>();
		companyServiceImpl.save(company);
		return response;
	}
	
	@RequestMapping(value="/list.data",method=RequestMethod.POST)
	public ListResponse<Company> list() {
		ListResponse<Company> response = new ListResponse<>();
		QueryWrapper<Company> queryWrapper = new QueryWrapper<Company>();
		
		List<Company> list =companyServiceImpl.list(queryWrapper);
		
		response.getData().setData(list);
		return response;
	}
}
