package com.zero.egg.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zero.egg.annotation.LoginToken;
import com.zero.egg.api.ApiConstants;
import com.zero.egg.model.Category;
import com.zero.egg.model.Goods;
import com.zero.egg.model.Specification;
import com.zero.egg.model.Stock;
import com.zero.egg.requestDTO.CategoryRequestDTO;
import com.zero.egg.requestDTO.LoginUser;
import com.zero.egg.requestDTO.StockRequest;
import com.zero.egg.responseDTO.CategoryListResponseDTO;
import com.zero.egg.responseDTO.GoodsResponse;
import com.zero.egg.responseDTO.StockMarkerListResponseDTO;
import com.zero.egg.responseDTO.StockResponse;
import com.zero.egg.service.CategoryService;
import com.zero.egg.service.IGoodsService;
import com.zero.egg.service.IStockService;
import com.zero.egg.service.SpecificationService;
import com.zero.egg.tool.Message;
import com.zero.egg.tool.UtilConstants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 库存控制器
 * </p>
 *
 * @author Hhaifeng
 * @since 2019-03-26
 */
@RestController
@Api(value = "库存管理")
@RequestMapping("/stock")
@Slf4j
public class StockController {


    @Autowired
    private IStockService stockService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private IGoodsService goodsService;

    @Autowired
    private SpecificationService specificationService;

    @LoginToken
    @ApiOperation(value = "分页查询库存")
    @RequestMapping(value = "/list-page.data", method = RequestMethod.POST)
    public Message<IPage<StockResponse>> listPage(
            @RequestBody StockRequest stockRequest
            , HttpServletRequest request) {
        Message<IPage<StockResponse>> message = new Message<IPage<StockResponse>>();
        try {
            LoginUser user = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
            Page<Stock> page = new Page<>();
            stockRequest.setCompanyId(user.getCompanyId());
            stockRequest.setShopId(user.getShopId());
            page.setCurrent(stockRequest.getCurrent());
            page.setSize(stockRequest.getSize());
            QueryWrapper<StockRequest> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("s.dr", false);//查询未删除信息
            if (stockRequest != null) {
                queryWrapper.eq(StringUtils.isNotBlank(stockRequest.getShopId()), "s.shop_id", stockRequest.getShopId())
                        .eq(StringUtils.isNotBlank(stockRequest.getCompanyId()), "s.company_id", stockRequest.getCompanyId())
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
        } catch (Exception e) {
            log.error("list-page.data error:" + e);
            message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
            message.setMessage(UtilConstants.ResponseMsg.FAILED);
        }
        return message;
    }

    @LoginToken
    @ApiOperation(value = "根据条件查询库存")
    @RequestMapping(value = "/list.data", method = RequestMethod.POST)
    public Message<List<StockResponse>> list(@RequestBody @ApiParam(required = false, name = "stockRequest", value = "根据需求自行确定搜索字段") StockRequest stockRequest
            , HttpServletRequest request) {
        Message<List<StockResponse>> message = new Message<List<StockResponse>>();
        try {
            LoginUser user = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
            stockRequest.setShopId(user.getShopId());
            stockRequest.setCompanyId(user.getCompanyId());
            QueryWrapper<StockRequest> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("s.dr", false);//查询未删除信息
            if (stockRequest != null) {
                queryWrapper.eq(StringUtils.isNotBlank(stockRequest.getShopId()), "s.shop_id", stockRequest.getShopId())
                        .eq(StringUtils.isNotBlank(stockRequest.getCompanyId()), "s.company_id", stockRequest.getCompanyId())
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
            }
            message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
            message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
        } catch (Exception e) {
            log.error("list.data error:" + e);
            message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
            message.setMessage(UtilConstants.ResponseMsg.FAILED);
        }
        return message;
    }

    @LoginToken
    @ApiOperation(value = "库存中存在的品种")
    @RequestMapping(value = "/findcategory.data", method = RequestMethod.POST)
    public Message<List<StockResponse>> findCategory(@RequestBody StockRequest stockRequest, HttpServletRequest request) {
        Message<List<StockResponse>> message = new Message<List<StockResponse>>();
        try {
            QueryWrapper<StockRequest> queryWrapper = new QueryWrapper<>();
            LoginUser user = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
            stockRequest.setCompanyId(user.getCompanyId());
            stockRequest.setShopId(user.getShopId());
            //查询未删除信息
            queryWrapper.eq("s.dr", false)
                    .eq(StringUtils.isNotBlank(stockRequest.getShopId()), "s.shop_id", stockRequest.getShopId())
                    .eq(StringUtils.isNotBlank(stockRequest.getCompanyId()), "s.company_id", stockRequest.getCompanyId());
            List<StockResponse> categoryList = stockService.categoryListByCondition(queryWrapper);
            if (categoryList != null) {
                message.setData(categoryList);
                message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
                message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
            } else {
                message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
                message.setMessage(UtilConstants.ResponseMsg.FAILED);
            }
        } catch (Exception e) {
            log.error("findCategory failed:" + e);
            message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
            message.setMessage(UtilConstants.ResponseMsg.FAILED);
        }
        return message;
    }

    @LoginToken
    @ApiOperation(value = "仓库规格下商品详情")
    @RequestMapping(value = "/findbyspecification.data", method = RequestMethod.POST)
    public Message<Map<String, Object>> findBySpecification(@RequestBody @ApiParam(required = false, name = "stockRequest"
            , value = "规格主键") StockRequest stockRequest, HttpServletRequest request) {
        Message<Map<String, Object>> message = new Message<Map<String, Object>>();
        QueryWrapper<Goods> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("s.dr", false);//查询未删除信息
        LoginUser user = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
        stockRequest.setShopId(user.getShopId());
        stockRequest.setCompanyId(user.getCompanyId());
        if (null != stockRequest.getSpecificationId()) {
            queryWrapper.eq("g.shop_id", stockRequest.getShopId())
                    .eq("g.company_id", stockRequest.getCompanyId())
                    .eq("g.specification_id", stockRequest.getSpecificationId())
            ;
        } else {
            message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
            message.setMessage(UtilConstants.ResponseMsg.PARAM_MISSING);
            return message;
        }
        List<GoodsResponse> goodsList = goodsService.listByCondition(queryWrapper);
        Map<String, Object> map = new HashMap<>();
        if (goodsList != null && goodsList.size() > 0) {
            map.put("goodsList", goodsList);
            map.put("amount", goodsList.size());
        } else {
            map.put("goodsList", "");
            map.put("amount", 0);
        }
        //获取规格下的重量差值
        Specification specification = new Specification();
        specification.setId(stockRequest.getSpecificationId());
        specification = specificationService.getById(specification);
        BigDecimal weightMax = specification.getWeightMax().setScale(0, BigDecimal.ROUND_UP); // 向上取整
        BigDecimal weightMin = specification.getWeightMin().setScale(0, BigDecimal.ROUND_DOWN); // 向下取整
        long weighValue = weightMax.subtract(weightMin).longValue();
        //统计每个重量值之间的数量
        List<Map<String, Object>> gapList = new ArrayList<>();
        for (int i = 1; i <= weighValue; i++) {
            Map<String, Object> gapMap = new HashMap<>();
            int weighCount = 0;
            if (goodsList != null && goodsList.size() > 0) {
                weightMax = weightMin.add(new BigDecimal(i));
                for (GoodsResponse goodsResponse : goodsList) {
                    if (goodsResponse.getWeight().compareTo(weightMin) == 1 && goodsResponse.getWeight().compareTo(weightMax) == -1) {
                        weighCount += 1;
                    }
                }
                gapMap.put("gapName", weightMin.intValue() + "--" + weightMax.intValue());
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
    @ApiOperation(value = "店铺下各品种的库存数量")
    @PostMapping(value = "/statistics")
    public Message<List<Map<String, Object>>> statistics(HttpServletRequest request) {
        //BaseResponse<Object> response = new BaseResponse<>(ApiConstants.ResponseCode.EXECUTE_ERROR, ApiConstants.ResponseMsg.EXECUTE_ERROR);
        Message<List<Map<String, Object>>> message = new Message<List<Map<String, Object>>>();
        List<Map<String, Object>> resultList = new ArrayList<>();
        try {
            LoginUser user = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
            String companyId = user.getCompanyId();
            String shopId = user.getShopId();
            //查询店铺下的所有的种类
            CategoryRequestDTO categoryRequestDTO = new CategoryRequestDTO();
            //查询所有数据(暂时不做分页)
            categoryRequestDTO.setSize(999L);
            categoryRequestDTO.setCompanyId(companyId);
            categoryRequestDTO.setShopId(shopId);
            CategoryListResponseDTO categoryListResponseDTO = (CategoryListResponseDTO) categoryService.listEggType(categoryRequestDTO).getData();
            List<Category> categories = categoryListResponseDTO.getCategoryList();
            if (categories != null && categories.size() > 0) {
                //遍历查询的种类
                for (Category category : categories) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("categoryName", category.getName());
                    map.put("categoryId", category.getId());
                    //查询种类下的库存
                    BigDecimal quantitys = BigDecimal.ZERO;
                    QueryWrapper<StockRequest> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("c.id", category.getId());
                    List<StockResponse> stockResponses = stockService.categoryCountListByCondition(queryWrapper);
                    if (stockResponses != null && stockResponses.size() > 0) {
                        for (StockResponse stockResponse : stockResponses) {
                            if (stockResponse.getQuantity() != null) {
                                quantitys = quantitys.add(stockResponse.getQuantity());
                            }
                        }
                    }
                    map.put("quantitys", quantitys);
                    resultList.add(map);
                }
                message.setData(resultList);
                message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
                message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
            } else {
                message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
                message.setMessage(UtilConstants.ResponseMsg.FAILED);
            }
        } catch (Exception e) {
            log.error("statistics show failed:" + e);
            message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
            message.setMessage(UtilConstants.ResponseMsg.FAILED);
        }
        return message;
    }

    /**
     * 当前企业当前店铺下库存里对应鸡蛋品种的所有标记列表
     *
     * @return
     */
    @PostMapping(value = "/liststockmarker")
    @ApiOperation(value = "当前企业当前店铺下库存里对应鸡蛋品种的所有标记列表")
    @LoginToken
    public Message<StockMarkerListResponseDTO> listStockMarker(@RequestParam String categoryId, HttpServletRequest request) {
        Message message;
        try {
            LoginUser user = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
            QueryWrapper<Stock> queryWrapper = new QueryWrapper<>();
            //基础查询条件为当前企业当前店铺未失效的记录
            queryWrapper.eq("s.company_id", user.getCompanyId())
                    .eq("s.shop_id", user.getShopId())
                    .eq("s.dr", false)
                    .eq("c.id", categoryId);
            message = stockService.markerListByCondition(queryWrapper);
        } catch (Exception e) {
            log.error("listStockMarker failed:" + e);
            message = new Message();
            message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
            message.setMessage(UtilConstants.ResponseMsg.FAILED);
        }
        return message;
    }

}
