package com.zero.egg.controller;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zero.egg.annotation.LoginToken;
import com.zero.egg.annotation.PassToken;
import com.zero.egg.api.ApiConstants;
import com.zero.egg.api.dto.BaseResponse;
import com.zero.egg.api.dto.response.ListResponse;
import com.zero.egg.model.Category;
import com.zero.egg.model.Stock;
import com.zero.egg.requestDTO.CategoryRequestDTO;
import com.zero.egg.requestDTO.StockRequest;
import com.zero.egg.responseDTO.CategoryListResponseDTO;
import com.zero.egg.responseDTO.StockResponse;
import com.zero.egg.service.CategoryService;
import com.zero.egg.service.IStockService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * <p>
 *  库存控制器
 * </p>
 *
 * @author Hhaifeng
 * @since 2019-03-26
 */
@RestController
@Api(value="库存管理")
@RequestMapping("/stock")
public class StockController {
	
	
	@Autowired
	private  IStockService stockService;
	
	@Autowired
	private  CategoryService categoryService;
	
	//@LoginToken
	@PassToken
	@ApiOperation(value="分页查询库存")
	@RequestMapping(value="/list-page.data",method=RequestMethod.POST)
	public ListResponse<StockResponse> list(@RequestParam @ApiParam(required =true,name ="pageNum",value="页码") int pageNum,
			@RequestParam @ApiParam(required=true,name="pageSize",value="页大小") int pageSize,
			@RequestBody @ApiParam(required=false,name="stockRequest" ,value="根据需求自行确定搜索字段") StockRequest stockRequest) {
		ListResponse<StockResponse> response = new ListResponse<>(ApiConstants.ResponseCode.EXECUTE_ERROR, ApiConstants.ResponseMsg.EXECUTE_ERROR);
		Page<Stock> page = new Page<>();
		page.setPages(pageNum);
		page.setSize(pageSize);
		QueryWrapper<StockRequest> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("s.dr", false);//查询未删除信息
		if (stockRequest != null) {
			queryWrapper.eq(StringUtils.isNotBlank(stockRequest.getShopId()),"s.shop_id", stockRequest.getShopId())
			.eq(StringUtils.isNotBlank(stockRequest.getCompanyId()),"s.company_id", stockRequest.getCompanyId())
			.eq(StringUtils.isNotBlank(stockRequest.getSpecificationId()), "s.specification_id", stockRequest.getSpecificationId())
			.eq(StringUtils.isNotBlank(stockRequest.getProgramId()), "sp.program_id", stockRequest.getProgramId())
			.eq(StringUtils.isNotBlank(stockRequest.getMarker()), "sp.marker", stockRequest.getMarker())
			.eq(StringUtils.isNotBlank(stockRequest.getMode()), "sp.mode", stockRequest.getMode())
			.eq(StringUtils.isNotBlank(stockRequest.getWarn()), "sp.warn", stockRequest.getWarn())
			.eq(StringUtils.isNotBlank(stockRequest.getNumerical()), "sp.numerical", stockRequest.getNumerical())
			.ge(StringUtils.isNotBlank(stockRequest.getWeightMin()), "sp.weight_min", stockRequest.getWeightMin())
			.le(StringUtils.isNotBlank(stockRequest.getWeightMax()), "sp.weight_max", stockRequest.getWeightMax())
			;
		}
		IPage<StockResponse> list = stockService.listByCondition(page, queryWrapper);
		response.getData().setData(list.getRecords());
		response.getData().setTotal(list.getTotal());
		return response;
		
	}
	
	@LoginToken
	//@PassToken
	@ApiOperation(value="根据条件查询库存")
	@RequestMapping(value="/list.data",method=RequestMethod.POST)
	public BaseResponse<Object> getByCompanyId(@RequestBody @ApiParam(required=false,name="stockRequest" ,value="根据需求自行确定搜索字段") StockRequest stockRequest) {
		BaseResponse<Object> response = new BaseResponse<>(ApiConstants.ResponseCode.EXECUTE_ERROR, ApiConstants.ResponseMsg.EXECUTE_ERROR);
		QueryWrapper<StockRequest> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("s.dr", false);//查询未删除信息
		if (stockRequest != null) {
			queryWrapper.eq(StringUtils.isNotBlank(stockRequest.getShopId()),"s.shop_id", stockRequest.getShopId())
			.eq(StringUtils.isNotBlank(stockRequest.getCompanyId()),"s.company_id", stockRequest.getCompanyId())
			.eq(StringUtils.isNotBlank(stockRequest.getSpecificationId()), "s.specification_id", stockRequest.getSpecificationId())
			.eq(StringUtils.isNotBlank(stockRequest.getProgramId()), "sp.program_id", stockRequest.getProgramId())
			.eq(StringUtils.isNotBlank(stockRequest.getMarker()), "sp.marker", stockRequest.getMarker())
			.eq(StringUtils.isNotBlank(stockRequest.getMode()), "sp.mode", stockRequest.getMode())
			.eq(StringUtils.isNotBlank(stockRequest.getWarn()), "sp.warn", stockRequest.getWarn())
			.eq(StringUtils.isNotBlank(stockRequest.getNumerical()), "sp.numerical", stockRequest.getNumerical())
			.ge(StringUtils.isNotBlank(stockRequest.getWeightMin()), "sp.weight_min", stockRequest.getWeightMin())
			.le(StringUtils.isNotBlank(stockRequest.getWeightMax()), "sp.weight_max", stockRequest.getWeightMax())
			;
		}
		List<StockResponse> companyUserList = stockService.listByCondition(queryWrapper);
		if (companyUserList != null) {
			response.setCode(ApiConstants.ResponseCode.SUCCESS);
			response.setMsg("查询成功");
			response.setData(companyUserList);
		}else {
			response.setMsg(ApiConstants.ResponseMsg.NULL_DATA);
		}
		return response;
	}
	
	//@LoginToken
	@PassToken
	@ApiOperation(value="店铺下各品种的库存数量")
	@PostMapping(value="/statistics")
	public BaseResponse<Object> statistics(@RequestParam @ApiParam(required =true,name ="shopId",value="店铺id") String shopId
			,@RequestParam @ApiParam(required =true,name ="companyId",value="企业id") String companyId) {
		BaseResponse<Object> response = new BaseResponse<>(ApiConstants.ResponseCode.EXECUTE_ERROR, ApiConstants.ResponseMsg.EXECUTE_ERROR);
		List<Map<String, Object>> resultList = new ArrayList<>();
		//查询店铺下的所有的种类
		CategoryRequestDTO categoryRequestDTO = new CategoryRequestDTO();
		categoryRequestDTO.setCompanyId(companyId);
		categoryRequestDTO.setShopId(shopId);
		CategoryListResponseDTO categoryListResponseDTO = (CategoryListResponseDTO) categoryService.listEggType(categoryRequestDTO).getData();
		List<Category> categories = categoryListResponseDTO.getCategoryList();
		if (categories !=null && categories.size()>0) {
			//遍历查询的种类
			for (Category category : categories) {
				Map<String, Object> map = new HashMap<>();
				map.put("categoryName", category.getName());
				map.put("categoryId", category.getId());
				//查询种类下的库存
				BigDecimal quantitys = BigDecimal.ZERO;
				QueryWrapper<StockRequest> queryWrapper = new QueryWrapper<>();
				queryWrapper.eq("c.id", category.getId());
				List<StockResponse> stockResponses =stockService.categoryCountListByCondition(queryWrapper);
				if (stockResponses != null && stockResponses.size()>0) {
					for (StockResponse stockResponse : stockResponses) {
						if (stockResponse.getQuantity() != null) {
							quantitys = quantitys.add(stockResponse.getQuantity());
						}
					}	
				}
				map.put("quantitys", quantitys);
				resultList.add(map);
			}
		}
		response.setCode(ApiConstants.ResponseCode.SUCCESS);
		response.setData(resultList);
		response.setMsg("查询成功");
		return response;
	}

}
