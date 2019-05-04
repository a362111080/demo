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
import com.zero.egg.model.Category;
import com.zero.egg.model.Goods;
import com.zero.egg.model.Specification;
import com.zero.egg.model.Stock;
import com.zero.egg.requestDTO.CategoryRequestDTO;
import com.zero.egg.requestDTO.StockRequest;
import com.zero.egg.responseDTO.CategoryListResponseDTO;
import com.zero.egg.responseDTO.GoodsResponse;
import com.zero.egg.responseDTO.StockResponse;
import com.zero.egg.service.CategoryService;
import com.zero.egg.service.IGoodsService;
import com.zero.egg.service.IStockService;
import com.zero.egg.service.SpecificationService;
import com.zero.egg.service.impl.SpecificationServiceImpl;
import com.zero.egg.tool.Message;
import com.zero.egg.tool.UtilConstants;

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
	
	@Autowired
	private IGoodsService goodsService;
	
	@Autowired 
	private SpecificationService specificationService;
	
	//@LoginToken
	@PassToken
	@ApiOperation(value="分页查询库存")
	@RequestMapping(value="/list-page.data",method=RequestMethod.POST)
	public Message<IPage<StockResponse>> listPage(
			@RequestBody @ApiParam(required=false,name="stockRequest" ,value="企业主键，店铺主键，品种主键") StockRequest stockRequest) {
		//ListResponse<StockResponse> response = new ListResponse<>(ApiConstants.ResponseCode.EXECUTE_ERROR, ApiConstants.ResponseMsg.EXECUTE_ERROR);
		Message<IPage<StockResponse>> message = new Message<IPage<StockResponse>>();
		Page<Stock> page = new Page<>();
		page.setCurrent(stockRequest.getCurrent());
		page.setSize(stockRequest.getSize());
		QueryWrapper<StockRequest> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("s.dr", false);//查询未删除信息
		if (stockRequest != null) {
			queryWrapper.eq(StringUtils.isNotBlank(stockRequest.getShopId()),"s.shop_id", stockRequest.getShopId())
			.eq(StringUtils.isNotBlank(stockRequest.getCompanyId()),"s.company_id", stockRequest.getCompanyId())
			.eq(StringUtils.isNotBlank(stockRequest.getSpecificationId()), "s.specification_id", stockRequest.getSpecificationId())
			.eq(StringUtils.isNotBlank(stockRequest.getProgramId()), "sp.program_id", stockRequest.getProgramId())
			.eq(StringUtils.isNotBlank(stockRequest.getCategoryId()), "c.id", stockRequest.getCategoryId())
			.eq(StringUtils.isNotBlank(stockRequest.getMarker()), "sp.marker", stockRequest.getMarker())
			.eq(StringUtils.isNotBlank(stockRequest.getMode()), "sp.mode", stockRequest.getMode())
			.eq(StringUtils.isNotBlank(stockRequest.getWarn()), "sp.warn", stockRequest.getWarn())
			.eq(StringUtils.isNotBlank(stockRequest.getNumerical()), "sp.numerical", stockRequest.getNumerical())
			.ge(StringUtils.isNotBlank(stockRequest.getWeightMin()), "sp.weight_min", stockRequest.getWeightMin())
			.le(StringUtils.isNotBlank(stockRequest.getWeightMax()), "sp.weight_max", stockRequest.getWeightMax())
			;
		}
		IPage<StockResponse> list = stockService.listByCondition(page, queryWrapper);
		message.setData(list);
		message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
		message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
		return message;
		
	}
	
	@LoginToken
	//@PassToken
	@ApiOperation(value="根据条件查询库存")
	@RequestMapping(value="/list.data",method=RequestMethod.POST)
	public Message<List<StockResponse>> list(@RequestBody @ApiParam(required=false,name="stockRequest" ,value="根据需求自行确定搜索字段") StockRequest stockRequest) {
		//BaseResponse<Object> response = new BaseResponse<>(ApiConstants.ResponseCode.EXECUTE_ERROR, ApiConstants.ResponseMsg.EXECUTE_ERROR);
		Message<List<StockResponse>> message = new Message<List<StockResponse>>();
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
		List<StockResponse> stockList = stockService.listByCondition(queryWrapper);
		if (stockList != null) {
			message.setData(stockList);
			message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
			message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
		}else {
			message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
			message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
		}
		return message;
	}
	
	//@LoginToken
	@PassToken
	@ApiOperation(value="库存中存在的品种")
	@RequestMapping(value="/findcategory.data",method=RequestMethod.POST)
	public Message<List<StockResponse>> findCategory(@RequestBody @ApiParam(required=false,name="stockRequest" ,value="企业主键，店铺主键") StockRequest stockRequest) {
		//BaseResponse<Object> response = new BaseResponse<>(ApiConstants.ResponseCode.EXECUTE_ERROR, ApiConstants.ResponseMsg.EXECUTE_ERROR);
		Message<List<StockResponse>> message = new Message<List<StockResponse>>();
		QueryWrapper<StockRequest> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("s.dr", false);//查询未删除信息
		if (stockRequest != null) {
			queryWrapper.eq(StringUtils.isNotBlank(stockRequest.getShopId()),"s.shop_id", stockRequest.getShopId())
			.eq(StringUtils.isNotBlank(stockRequest.getCompanyId()),"s.company_id", stockRequest.getCompanyId())
			;
		}
		List<StockResponse> categoryList = stockService.categoryListByCondition(queryWrapper);
		if (categoryList != null) {
			message.setData(categoryList);
			message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
			message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
		}else {
			message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
			message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
		}
		return message;
	}
	
	//@LoginToken
	@PassToken
	@ApiOperation(value="仓库规格下商品详情")
	@RequestMapping(value="/findbyspecification.data",method=RequestMethod.POST)
	public Message<Map<String, Object>> findBySpecification(@RequestBody @ApiParam(required=false,name="stockRequest" ,value="企业主键,店铺主键，规格主键") StockRequest stockRequest) {
		//BaseResponse<Object> response = new BaseResponse<>(ApiConstants.ResponseCode.EXECUTE_ERROR, ApiConstants.ResponseMsg.EXECUTE_ERROR);
		Message<Map<String, Object>> message = new Message<Map<String, Object>>();
		QueryWrapper<Goods> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("s.dr", false);//查询未删除信息
		if (stockRequest != null) {
			queryWrapper.eq(StringUtils.isNotBlank(stockRequest.getShopId()),"g.shop_id", stockRequest.getShopId())
			.eq(StringUtils.isNotBlank(stockRequest.getCompanyId()),"g.company_id", stockRequest.getCompanyId())
			.eq(StringUtils.isNotBlank(stockRequest.getSpecificationId()), "g.specification_id", stockRequest.getSpecificationId())
			;
		}
		List<GoodsResponse> goodsList = goodsService.listByCondition(queryWrapper);
		Map<String, Object> map =  new HashMap<>();
		if (goodsList !=null && goodsList.size()>0) {
			map.put("goodsList", goodsList);
			map.put("amount", goodsList.size());
		}else {
			map.put("goodsList", "");
			map.put("amount", 0);
		}
		//获取规格下的重量差值
		Specification specification = new Specification();
		specification.setId(stockRequest.getSpecificationId());
		specification =specificationService.getById(specification);
		BigDecimal weightMax  = specification.getWeightMax().setScale( 0, BigDecimal.ROUND_UP ); // 向上取整
		BigDecimal weightMin  = specification.getWeightMin().setScale( 0, BigDecimal.ROUND_DOWN ); // 向下取整
		long weighValue = weightMax.subtract(weightMin).longValue();
		//统计每个重量值之间的数量
		List<Map<String, Object>> gapList = new ArrayList<>();
		for (int i = 1; i <= weighValue; i++) {
			Map<String, Object> gapMap = new HashMap<>();
			int weighCount =0;
			if (goodsList != null && goodsList.size()>0) {
				weightMax = weightMin.add(new BigDecimal(i));
				for (GoodsResponse goodsResponse : goodsList) {
					if (goodsResponse.getWeight().compareTo(weightMin) == 1 && goodsResponse.getWeight().compareTo(weightMax) == -1) {
						weighCount +=1; 
					}
				}
				gapMap.put("gapName", weightMin.intValue()+"--"+weightMax.intValue());
				gapMap.put("gapCount", weighCount);
				gapList.add(gapMap);
				weightMin = weightMax;
			}
		}
		map.put("gapList", gapList);
		message.setData(map);
		message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
		message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
		return message;
	}
	
	
	
	@LoginToken
	//@PassToken
	@ApiOperation(value="店铺下各品种的库存数量")
	@PostMapping(value="/statistics")
	public Message<List<Map<String, Object>>> statistics(@RequestParam @ApiParam(required =true,name ="shopId",value="店铺id") String shopId
			,@RequestParam @ApiParam(required =true,name ="companyId",value="企业id") String companyId) {
		//BaseResponse<Object> response = new BaseResponse<>(ApiConstants.ResponseCode.EXECUTE_ERROR, ApiConstants.ResponseMsg.EXECUTE_ERROR);
		Message<List<Map<String, Object>>> message = new Message<List<Map<String, Object>>>();
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
		message.setData(resultList);
		message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
		message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
		return message;
	}

}
