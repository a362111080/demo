package com.zero.egg.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zero.egg.annotation.LoginToken;
import com.zero.egg.api.ApiConstants;
import com.zero.egg.cache.JedisUtil;
import com.zero.egg.enums.TaskEnums;
import com.zero.egg.model.ShipmentGoods;
import com.zero.egg.requestDTO.LoginUser;
import com.zero.egg.requestDTO.RemShipmentGoodsRequestDTO;
import com.zero.egg.requestDTO.ShipmentGoodBarCodeRequestDTO;
import com.zero.egg.requestDTO.ShipmentGoodsRequest;
import com.zero.egg.requestDTO.ShipmentStaticRequestDTO;
import com.zero.egg.responseDTO.ShipmentGoodsResponse;
import com.zero.egg.service.CategoryService;
import com.zero.egg.service.IGoodsService;
import com.zero.egg.service.IShipmentGoodsService;
import com.zero.egg.tool.Message;
import com.zero.egg.tool.ServiceException;
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
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Hhaifeng
 * @since 2019-04-23
 */
@RestController
@Api(value = "出货管理")
@RequestMapping("/shipment-goods")
@Slf4j
public class ShipmentGoodsController {

    @Autowired
    private IShipmentGoodsService shipmentGoodsService;
    @Autowired
    private IGoodsService goodService;

    @Autowired
    private JedisUtil.Strings jedisStrings;

    @Autowired
    private JedisUtil.Keys jedisKeys;

    @Autowired
    private JedisUtil.SortSets sortSets;

    @Autowired
    private CategoryService categoryService;

    @LoginToken
    @ApiOperation(value = "移除出货商品")
    @PostMapping(value = "/remove")
    public Message removeShipmentGood(@RequestBody RemShipmentGoodsRequestDTO remShipmentGoodsRequestDTO,
                                      HttpServletRequest request) {
        //当前登录用户
        LoginUser loginUser = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
        Message message;
        try {
            /**
             * 非空判断
             */
            if (remShipmentGoodsRequestDTO == null || null == remShipmentGoodsRequestDTO.getTaskId()
                    || null == remShipmentGoodsRequestDTO.getCustomerId()
                    || null == remShipmentGoodsRequestDTO.getGoodsResponseList()
                    || 1 > remShipmentGoodsRequestDTO.getGoodsResponseList().size()) {
                message = new Message();
                message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
                message.setMessage(UtilConstants.ResponseMsg.PARAM_ERROR);
                return message;
            }
            /**
             * 任务状态确认,如果任务已取消或者已完成,不能进行出货商品的移除
             */
            if (!jedisKeys.exists(UtilConstants.RedisPrefix.SHIPMENTGOOD_TASK + loginUser.getCompanyId() + ":" + loginUser.getShopId()
                    + ":" + remShipmentGoodsRequestDTO.getCustomerId() + ":" + remShipmentGoodsRequestDTO.getTaskId() + ":" + "status")
                    || TaskEnums.Status.CANCELED.index().toString().equals(jedisStrings.get(UtilConstants.RedisPrefix.SHIPMENTGOOD_TASK
                    + loginUser.getCompanyId() + ":" + loginUser.getShopId()
                    + ":" + remShipmentGoodsRequestDTO.getCustomerId() + ":" + remShipmentGoodsRequestDTO.getTaskId() + ":" + "status"))
                    || TaskEnums.Status.Finish.index().toString().equals(jedisStrings.get(UtilConstants.RedisPrefix.SHIPMENTGOOD_TASK
                    + loginUser.getCompanyId() + ":" + loginUser.getShopId()
                    + ":" + remShipmentGoodsRequestDTO.getCustomerId() + ":" + remShipmentGoodsRequestDTO.getTaskId() + ":" + "status"))) {
                message = new Message();
                message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
                message.setMessage(UtilConstants.ResponseMsg.TASK_NOT_FOUND);
                return message;
            }
            message = goodService.removeShipmentGoods(remShipmentGoodsRequestDTO, loginUser);
        } catch (Exception e) {
            log.error("removeShipmentGood error:" + e);
            message = new Message();
            message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
            message.setMessage(e.toString());
        }
        return message;
    }

    @LoginToken
    @ApiOperation(value = "新增出货商品")
    @PostMapping(value = "/add")
    public Message add(
            @RequestBody @ApiParam(required = true, value = "1.二维码信息 2.任务主键 3.客户主键")
                    ShipmentGoodBarCodeRequestDTO shipmentGoodRequestDTO
            , HttpServletRequest request) {
        //当前登录用户
        LoginUser loginUser = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
        Message message;

        /**
         * 非空判断
         */
        if (shipmentGoodRequestDTO == null || null == shipmentGoodRequestDTO.getBarCodeString()
                || "".equals(shipmentGoodRequestDTO.getBarCodeString()) || null == shipmentGoodRequestDTO.getTaskId()
                || null == shipmentGoodRequestDTO.getCustomerId()) {
            message = new Message();
            message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
            message.setMessage(UtilConstants.ResponseMsg.PARAM_ERROR);
            return message;
        }
        /**
         * 任务状态确认,只有任务在进行中,才能进行出货
         */
        if (!jedisKeys.exists(UtilConstants.RedisPrefix.SHIPMENTGOOD_TASK + loginUser.getCompanyId() + ":" + loginUser.getShopId()
                + ":" + shipmentGoodRequestDTO.getCustomerId() + ":" + shipmentGoodRequestDTO.getTaskId() + ":" + "status")
                || !TaskEnums.Status.Execute.index().toString().equals(jedisStrings.get(UtilConstants.RedisPrefix.SHIPMENTGOOD_TASK
                + loginUser.getCompanyId() + ":" + loginUser.getShopId()
                + ":" + shipmentGoodRequestDTO.getCustomerId() + ":" + shipmentGoodRequestDTO.getTaskId() + ":" + "status"))) {
            message = new Message();
            message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
            message.setMessage(UtilConstants.ResponseMsg.TASK_NOT_FOUND);
            return message;
        }
        try {
            message = goodService.querySingleGoodByBarCodeInfo(shipmentGoodRequestDTO, loginUser);
        } catch (Exception e) {
            message = new Message();
            message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
            //如果是service层抛出的ServiceExeption,则将错误信息装进返回消息中
            if (e instanceof ServiceException) {
                message.setMessage(e.getMessage());
            } else {
                message.setMessage(UtilConstants.ResponseMsg.FAILED);
            }
        }
        return message;

    }

    @LoginToken
    @ApiOperation(value = "查询当前出货任务的商品(出货列表用)")
    @PostMapping(value = "/shipmentgoodslist")
    public Message shipmentTaskList(@RequestBody @ApiParam(required = true, value = "1.客户主键 2.任务主键 3.sortType")
                                            ShipmentGoodsRequest shipmentGoodsRequest, HttpServletRequest request) {
        Message message = new Message();
        /**
         * 非空判断
         */
        if (shipmentGoodsRequest == null || null == shipmentGoodsRequest.getTaskId()
                || null == shipmentGoodsRequest.getCustomerId() || null == shipmentGoodsRequest.getSortType()) {
            message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
            message.setMessage(UtilConstants.ResponseMsg.PARAM_ERROR);
            return message;
        }
        try {
            //当前登录用户
            LoginUser loginUser = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
            message = shipmentGoodsService.listBySortType(loginUser, shipmentGoodsRequest);
        } catch (Exception e) {
            log.error("shipmentTastList failed:" + e);
            message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
            //如果是service层抛出的ServiceExeption,则将错误信息装进返回消息中
            if (e instanceof ServiceException) {
                message.setMessage(e.getMessage());
            } else {
                message.setMessage(UtilConstants.ResponseMsg.FAILED);
            }
        }
        return message;
    }


    @LoginToken
    @ApiOperation(value = "查询出货商品(出货完成用)")
    @RequestMapping(value = "/shipment-list.data", method = RequestMethod.POST)
    public Message<IPage<ShipmentGoodsResponse>> shipmentlist(
            @RequestBody @ApiParam(required = false, name = "task", value = "查询字段：,任务主键,创建人，创建时间,商品编号")
                    ShipmentGoodsRequest shipmentGoods, HttpServletRequest request) {
        //ListResponse<ShipmentGoodsResponse> response = new ListResponse<>(ApiConstants.ResponseCode.EXECUTE_ERROR, ApiConstants.ResponseMsg.EXECUTE_ERROR);
        Message<IPage<ShipmentGoodsResponse>> message = new Message<IPage<ShipmentGoodsResponse>>();
        //当前登录用户
        LoginUser loginUser = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
        Page<ShipmentGoods> page = new Page<>();
        page.setCurrent(shipmentGoods.getCurrent());
        page.setSize(shipmentGoods.getSize());
        QueryWrapper<ShipmentGoods> queryWrapper = new QueryWrapper<>();
        //查询未删除信息
        queryWrapper.eq("s.dr", false);
        if (shipmentGoods != null) {
            queryWrapper.eq(StringUtils.isNotBlank(shipmentGoods.getCompanyId()), "s.company_id", loginUser.getCompanyId())
                    .eq(StringUtils.isNotBlank(shipmentGoods.getShopId()), "s.shop_id", loginUser.getShopId())
                    .eq(StringUtils.isNotBlank(shipmentGoods.getCreator()), "s.creator", shipmentGoods.getCreator())
                    .eq(StringUtils.isNotBlank(shipmentGoods.getTaskId()), "s.task_id", shipmentGoods.getTaskId())
                    .eq(StringUtils.isNotBlank(shipmentGoods.getGoodsNo()), "s.goods_no", shipmentGoods.getGoodsNo())
                    .ge(shipmentGoods.getCreatetime() != null, "s.createtime", LocalDateTime.of(LocalDate.now(), LocalTime.MIN))
                    .le(shipmentGoods.getCreatetime() != null, "s.createtime", LocalDateTime.of(LocalDate.now(), LocalTime.MAX))
            ;
        }
        IPage<ShipmentGoodsResponse> list = shipmentGoodsService.listByCondition(page, queryWrapper);
        message.setData(list);
        message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
        message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
        return message;

    }


    @LoginToken
    @ApiOperation(value = "统计出货商品")
    @PostMapping(value = "/shipment-statistics.data")
    public Message<List<Map<String, Object>>> statistics(
            @RequestBody @ApiParam(required = false, name = "task", value = "查询字段：任务主键）")
                    ShipmentGoods shipmentGoods, HttpServletRequest request) {
        //BaseResponse<Object> response = new BaseResponse<>(ApiConstants.ResponseCode.EXECUTE_ERROR, ApiConstants.ResponseMsg.EXECUTE_ERROR);
        //当前登录用户
        LoginUser loginUser = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
        Message<List<Map<String, Object>>> message = new Message<List<Map<String, Object>>>();
        List<Map<String, Object>> resultList = new ArrayList<>();
        QueryWrapper<ShipmentGoods> programqueryWrapper = new QueryWrapper<>();
        //查询未删除信息
        programqueryWrapper.eq("s.dr", false);
        if (shipmentGoods != null) {
            programqueryWrapper.eq(StringUtils.isNotBlank(shipmentGoods.getCompanyId()), "s.company_id", loginUser.getCompanyId())
                    .eq(StringUtils.isNotBlank(shipmentGoods.getShopId()), "s.shop_id", loginUser.getShopId())
                    .eq(StringUtils.isNotBlank(shipmentGoods.getTaskId()), "s.task_id", shipmentGoods.getTaskId())
                    .groupBy("sp.program_id");
        }
        //方案
        List<ShipmentGoodsResponse> listProgram = shipmentGoodsService.countProgram(programqueryWrapper);
        if (listProgram != null) {
            for (ShipmentGoodsResponse shipmentGoodsResponse : listProgram) {
                Map<String, Object> map = new HashMap<>();
                map.put("programId", shipmentGoodsResponse.getProgramId());
                map.put("programName", shipmentGoodsResponse.getProgramName());
                int programGoodsCount = 0;
                //方案下的品种
                QueryWrapper<ShipmentGoods> categoryqueryWrapper = new QueryWrapper<>();
                //查询未删除信息
                categoryqueryWrapper.eq("s.dr", false);
                if (shipmentGoods != null) {
                    categoryqueryWrapper.eq(StringUtils.isNotBlank(shipmentGoods.getCompanyId()), "s.company_id", loginUser.getCompanyId())
                            .eq(StringUtils.isNotBlank(shipmentGoods.getShopId()), "s.shop_id", loginUser.getShopId())
                            .eq(StringUtils.isNotBlank(shipmentGoods.getTaskId()), "s.task_id", shipmentGoods.getTaskId())
                            .eq("sp.program_id", shipmentGoodsResponse.getProgramId()).groupBy(true, "p.category_id");
                }
                List<ShipmentGoodsResponse> listCategory = shipmentGoodsService.countCategory(categoryqueryWrapper);
                List<Map<String, Object>> categoryList = new ArrayList<>();
                for (ShipmentGoodsResponse shipmentGoodsResponse2 : listCategory) {
                    Map<String, Object> map2 = new HashMap<>();
                    //每个品种下的出货商品
                    map2.put("categoryId", shipmentGoodsResponse2.getCategoryId());
                    map2.put("categoryName", shipmentGoodsResponse2.getCategoryName());
                    QueryWrapper<ShipmentGoods> queryWrapper = new QueryWrapper<>();
                    //查询未删除信息
                    queryWrapper.eq("s.dr", false);
                    if (shipmentGoods != null) {
                        queryWrapper.eq(StringUtils.isNotBlank(shipmentGoods.getCompanyId()), "s.company_id", loginUser.getCompanyId())
                                .eq(StringUtils.isNotBlank(shipmentGoods.getShopId()), "s.shop_id", loginUser.getShopId())
                                .eq(StringUtils.isNotBlank(shipmentGoods.getTaskId()), "s.task_id", shipmentGoods.getTaskId())
                                .eq(StringUtils.isNotBlank(shipmentGoodsResponse.getProgramId()), "sp.program_id", shipmentGoodsResponse.getProgramId())
                                .eq(StringUtils.isNotBlank(shipmentGoodsResponse2.getCategoryId()), "p.category_id", shipmentGoodsResponse2.getCategoryId())
                                .groupBy("s.specification_id", "s.marker");
                    }
                    List<ShipmentGoodsResponse> shipList = shipmentGoodsService.countSpecification(queryWrapper);
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
            message.setData(resultList);
            message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
            message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
        }
        return message;

    }

    @LoginToken
    @ApiOperation(value = "出货品种数目统计")
    @PostMapping(value = "/today-statistics")
    public Message<List<Map<String, Object>>> todayStatistics(HttpServletRequest request, @RequestBody ShipmentStaticRequestDTO shipmentStaticRequestDTO) {
        //当前登录用户
        LoginUser loginUser = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
        Message<List<Map<String, Object>>> message = new Message<List<Map<String, Object>>>();
        //店铺今天出货品种
        QueryWrapper<ShipmentGoods> categoryqueryWrapper = new QueryWrapper<>();
        categoryqueryWrapper.eq("s.dr", false);//查询未删除信息
        categoryqueryWrapper.eq("s.company_id", loginUser.getCompanyId())
                .eq("s.shop_id", loginUser.getShopId())
                .groupBy(true, "p.category_id");
        List<ShipmentGoodsResponse> listCategory = shipmentGoodsService.todaycountcategory(shipmentStaticRequestDTO,categoryqueryWrapper);
        List<Map<String, Object>> categoryList = new ArrayList<>();
        for (ShipmentGoodsResponse shipmentGoodsResponse2 : listCategory) {
            Map<String, Object> map = new HashMap<>();
            int count = 0;
            //每个品种下的出货商品
            map.put("categoryId", shipmentGoodsResponse2.getCategoryId());
            map.put("categoryName", shipmentGoodsResponse2.getCategoryName());
            QueryWrapper<ShipmentGoods> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("s.dr", false);//查询未删除信息
            queryWrapper.eq(StringUtils.isNotBlank(loginUser.getCompanyId()), "s.company_id", loginUser.getCompanyId())
                    .eq(StringUtils.isNotBlank(loginUser.getShopId()), "s.shop_id", loginUser.getShopId())
                    .eq(StringUtils.isNotBlank(shipmentGoodsResponse2.getCategoryId()), "p.category_id", shipmentGoodsResponse2.getCategoryId())
                    .groupBy("s.specification_id", "s.marker");
            List<ShipmentGoodsResponse> shipList = shipmentGoodsService.todaycountspecification(shipmentStaticRequestDTO,queryWrapper);
            for (ShipmentGoodsResponse shipmentGoodsResponse3 : shipList) {
                count += shipmentGoodsResponse3.getCount();
            }
            map.put("count", count);
            categoryList.add(map);
        }
        message.setData(categoryList);
        message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
        message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
        return message;
    }


}
