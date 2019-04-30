package com.zero.egg.controller;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
import com.zero.egg.api.ApiConstants;
import com.zero.egg.api.dto.BaseResponse;
import com.zero.egg.api.dto.response.ListResponse;
import com.zero.egg.model.Goods;
import com.zero.egg.model.ShipmentGoods;
import com.zero.egg.requestDTO.LoginUser;
import com.zero.egg.responseDTO.ShipmentGoodsResponse;
import com.zero.egg.service.IGoodsService;
import com.zero.egg.service.IShipmentGoodsService;
import com.zero.egg.tool.UuidUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Hhaifeng
 * @since 2019-04-23
 */
@RestController
@Api(value="出货管理")
@RequestMapping("/shipment-goods")
public class ShipmentGoodsController {

	@Autowired
	private IShipmentGoodsService shipmentGoodsService;
	@Autowired
	private IGoodsService goodService;
	
	@LoginToken
	@ApiOperation(value="新增出货商品")
	@PostMapping(value="/add")
	public BaseResponse<Object> add(
			@RequestBody @ApiParam(required=true,name="shipmentGoods",value="店铺主键、企业主键、商品编号，客户主键,任务主键，备注") ShipmentGoods shipmentGoods
			,HttpServletRequest request
			){
		BaseResponse<Object> response = new BaseResponse<>(ApiConstants.ResponseCode.EXECUTE_ERROR, ApiConstants.ResponseMsg.EXECUTE_ERROR);
		QueryWrapper<Goods>  goodQuery = new QueryWrapper<>();
		goodQuery.eq("goods_no", shipmentGoods.getGoodsNo());
		Goods goods = goodService.getOne(goodQuery);
		if (goods != null) {
			//当前登录用户
			LoginUser loginUser = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
			shipmentGoods.setId(UuidUtil.get32UUID());
			shipmentGoods.setSpecificationId(goods.getSpecificationId());
			shipmentGoods.setGoodsCategoryId(goods.getGoodsCategoryId());
			shipmentGoods.setMarker(goods.getMarker());
			shipmentGoods.setMode(goods.getMode());
			shipmentGoods.setWeight(goods.getWeight());
			shipmentGoods.setCreatetime(new Date());
			shipmentGoods.setCreator(loginUser.getId());
			shipmentGoods.setModifytime(new Date());
			shipmentGoods.setModifier(loginUser.getId());
			shipmentGoods.setDr(false);
			if (shipmentGoodsService.save(shipmentGoods)) {
				response.setCode(ApiConstants.ResponseCode.SUCCESS);
				response.setMsg("添加成功");
			}
		}else {
			response.setMsg("商品不存在");
		}
		
		return response;
	}
	
	
	@LoginToken
	@ApiOperation(value="查询出货商品")
	@RequestMapping(value="/shipment-list.data",method=RequestMethod.POST)
	public ListResponse<ShipmentGoodsResponse> shipmentlist(@RequestParam @ApiParam(required =true,name ="pageNum",value="页码") int pageNum,
			@RequestParam @ApiParam(required=true,name="pageSize",value="页大小") int pageSize,
			@RequestBody @ApiParam(required=false,name="task",value="查询字段：企业主键、店铺主键,任务主键,创建人，创建时间）") ShipmentGoods shipmentGoods) {
		ListResponse<ShipmentGoodsResponse> response = new ListResponse<>(ApiConstants.ResponseCode.EXECUTE_ERROR, ApiConstants.ResponseMsg.EXECUTE_ERROR);
		Page<ShipmentGoods> page = new Page<>();
		page.setPages(pageNum);
		page.setSize(pageSize);
		QueryWrapper<ShipmentGoods> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("s.dr", false);//查询未删除信息
		if (shipmentGoods != null) {
			queryWrapper.eq(StringUtils.isNotBlank(shipmentGoods.getCompanyId()),"s.company_id", shipmentGoods.getCompanyId())
			.eq(StringUtils.isNotBlank(shipmentGoods.getShopId()),"s.shop_id", shipmentGoods.getShopId())
			.eq(StringUtils.isNotBlank(shipmentGoods.getCreator()),"s.creator", shipmentGoods.getCreator())
			.eq(StringUtils.isNotBlank(shipmentGoods.getTaskId()),"s.task_id", shipmentGoods.getTaskId())
			.ge(shipmentGoods.getCreatetime() != null,"s.createtime",  LocalDateTime.of(LocalDate.now(), LocalTime.MIN))
			.le(shipmentGoods.getCreatetime() != null,"s.createtime",  LocalDateTime.of(LocalDate.now(), LocalTime.MAX))
			;
		}
		IPage<ShipmentGoodsResponse> list = shipmentGoodsService.listByCondition(page, queryWrapper);
		response.getData().setData(list.getRecords());
		response.getData().setTotal(list.getTotal());
		return response;
		
	}
	
	
	@LoginToken
	@ApiOperation(value="统计出货商品")
	@PostMapping(value="/shipment-statistics.data")
	public BaseResponse<Object> statistics(
			@RequestBody @ApiParam(required=false,name="task",value="查询字段：企业主键、店铺主键,任务主键）") ShipmentGoods shipmentGoods
			) {
		BaseResponse<Object> response = new BaseResponse<>(ApiConstants.ResponseCode.EXECUTE_ERROR, ApiConstants.ResponseMsg.EXECUTE_ERROR);
		List<Map<String, Object>> resultList = new ArrayList<>();
		QueryWrapper<ShipmentGoods> programqueryWrapper = new QueryWrapper<>();
		programqueryWrapper.eq("s.dr", false);//查询未删除信息
		if (shipmentGoods != null) {
			programqueryWrapper.eq(StringUtils.isNotBlank(shipmentGoods.getCompanyId()),"s.company_id", shipmentGoods.getCompanyId())
			.eq(StringUtils.isNotBlank(shipmentGoods.getShopId()),"s.shop_id", shipmentGoods.getShopId())
			.eq(StringUtils.isNotBlank(shipmentGoods.getTaskId()),"s.task_id", shipmentGoods.getTaskId())
			.groupBy("sp.program_id");
		}
		//方案
		List<ShipmentGoodsResponse> listProgram =shipmentGoodsService.countProgram(programqueryWrapper);
		if (listProgram != null) {
			for (ShipmentGoodsResponse shipmentGoodsResponse : listProgram) {
				Map<String, Object> map = new HashMap<>();
				map.put("programId", shipmentGoodsResponse.getProgramId());
				map.put("programName", shipmentGoodsResponse.getProgramName());
				int programGoodsCount = 0;
				//品种
				QueryWrapper<ShipmentGoods> categoryqueryWrapper = new QueryWrapper<>();
				categoryqueryWrapper.eq("s.dr", false);//查询未删除信息
				if (shipmentGoods != null) {
					categoryqueryWrapper.eq(StringUtils.isNotBlank(shipmentGoods.getCompanyId()),"s.company_id", shipmentGoods.getCompanyId())
					.eq(StringUtils.isNotBlank(shipmentGoods.getShopId()),"s.shop_id", shipmentGoods.getShopId())
					.eq(StringUtils.isNotBlank(shipmentGoods.getTaskId()),"s.task_id", shipmentGoods.getTaskId())
					.eq("sp.program_id", shipmentGoodsResponse.getProgramId()).groupBy(true,"p.category_id");
				}
				List<ShipmentGoodsResponse> listCategory =shipmentGoodsService.countCategory(categoryqueryWrapper);
				List<Map<String, Object>> categoryList = new ArrayList<>();
				for (ShipmentGoodsResponse shipmentGoodsResponse2 : listCategory) {
					Map<String, Object> map2 = new HashMap<>();
					map2.put("categoryId", shipmentGoodsResponse2.getCategoryId());
					map2.put("categoryName", shipmentGoodsResponse2.getCategoryName());
					QueryWrapper<ShipmentGoods> queryWrapper = new QueryWrapper<>();
					queryWrapper.eq("s.dr", false);//查询未删除信息
					if (shipmentGoods != null) {
						queryWrapper.eq(StringUtils.isNotBlank(shipmentGoods.getCompanyId()),"s.company_id", shipmentGoods.getCompanyId())
						.eq(StringUtils.isNotBlank(shipmentGoods.getShopId()),"s.shop_id", shipmentGoods.getShopId())
						.eq(StringUtils.isNotBlank(shipmentGoods.getTaskId()),"s.task_id", shipmentGoods.getTaskId())
						.eq(StringUtils.isNotBlank(shipmentGoods.getTaskId()),"sp.program_id", shipmentGoodsResponse.getProgramId())
						.eq(StringUtils.isNotBlank(shipmentGoods.getTaskId()),"p.category_id", shipmentGoodsResponse2.getCategoryId())
						.groupBy("s.specification_id","s.marker");
					}
					List<ShipmentGoodsResponse> shipList =shipmentGoodsService.countSpecification(queryWrapper);
					List<Map<String, Object>> goodsList = new ArrayList<>();
					for (ShipmentGoodsResponse shipmentGoodsResponse3 : shipList) {
						programGoodsCount += shipmentGoodsResponse3.getCount();
						Map<String, Object> map3 = new HashMap<>();
						map3.put("specification_id", shipmentGoodsResponse3.getSpecificationId());
						map3.put("marker", shipmentGoodsResponse3.getMarker());
						map3.put("count", shipmentGoodsResponse3.getCount());
						goodsList.add(map3);
					}
					map2.put("shipmentGoods", goodsList);
					categoryList.add(map2);
				}
				map.put("category", categoryList);
				map.put("programGoodsCount", programGoodsCount);
				resultList.add(map);
			}
			response.setData(resultList);
			response.setMsg("查询成功");
		}
		return response;
		
	}
	
	
	
	
	
}
