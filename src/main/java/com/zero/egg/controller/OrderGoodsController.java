package com.zero.egg.controller;

import com.zero.egg.annotation.LoginToken;
import com.zero.egg.api.ApiConstants;
import com.zero.egg.model.OrderCategory;
import com.zero.egg.requestDTO.LoginUser;
import com.zero.egg.requestDTO.OrderGoodsRequestDTO;
import com.zero.egg.responseDTO.OrderCategoryResponseDTO;
import com.zero.egg.service.IShopService;
import com.zero.egg.service.OrderGoodsService;
import com.zero.egg.tool.Message;
import com.zero.egg.tool.ServiceException;
import com.zero.egg.tool.UtilConstants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @ClassName OrderGoodsController
 * @Description 订货平台商品相关
 * @Author lyming
 * @Date 2019/9/29 10:10 上午
 **/
@Api("订货平台商品管理")
@Slf4j
@RestController
@RequestMapping(("/ordergoods"))
public class OrderGoodsController {

    @Autowired
    HttpServletRequest request;

    @Autowired
    private IShopService shopService;
    @Autowired
    private OrderGoodsService orderGoodsService;

    @ApiOperation("商品列表")
    @RequestMapping(value = "/getgoodslist", method = RequestMethod.POST)
    public Message getGoodsList(@RequestBody OrderGoodsRequestDTO model) {
        Message msg = new Message();
        LoginUser loginUser = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
        try {
            //店铺id必传
            if (null == model || null == model.getShopId()) {
                msg.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
                msg.setMessage(UtilConstants.ResponseMsg.PARAM_MISSING);
                return msg;
            }
            msg = orderGoodsService.getGoodsList(model, loginUser);
        } catch (Exception e) {
            log.error("getAdList controller error:" + e);
            msg.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
            if (e instanceof ServiceException) {
                msg.setMessage(e.getMessage());
            }
            msg.setMessage((UtilConstants.ResponseMsg.FAILED));
        }
        return msg;
    }

    @LoginToken
    @ApiOperation(value = "查询类目")
    @RequestMapping(value = "/queryordercategory", method = RequestMethod.POST)
    public Message<Object> queryordercategory(@RequestBody OrderCategory model) {
        Message<Object> message = new Message<Object>();
        //当前登录用户
        LoginUser loginUser = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
        //TODO 校验登录用户绑定的店铺id是否包含model里面的id
        if (model == null || null == model.getShopId()) {
            message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
            message.setMessage(UtilConstants.ResponseMsg.PARAM_MISSING);
            return message;
        }
        List<OrderCategoryResponseDTO> ResponseDTO = shopService.GetOrderCateGory(model);
        if (ResponseDTO.size() > 0) {
            if (model.getIsQueryChild() == true) {
                for (int m = 0; m < ResponseDTO.size(); m++) {
                    model.setPid(ResponseDTO.get(m).getId());
                    List<OrderCategory> child = shopService.GetOrderCateGoryChild(model);
                    ResponseDTO.get(m).setOrderCategoryList(child);
                }
            }
            message.setData(ResponseDTO);
            message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
            message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
        } else {
            message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
            message.setMessage("店铺无商品分类信息");
        }
        return message;
    }
    @ApiOperation("类别下商品列表")
    @RequestMapping(value = "/getcategorygoodslist", method = RequestMethod.POST)
    public Message getCategoryGoodsList(@RequestBody OrderGoodsRequestDTO model) {
        Message msg = new Message();
        LoginUser loginUser = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
        try {
            //店铺id必传
            if (null == model || null == model.getShopId()||null == model.getCategoryId()) {
                msg.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
                msg.setMessage(UtilConstants.ResponseMsg.PARAM_MISSING);
                return msg;
            }
            msg = orderGoodsService.getGoodsListByCategoryId(model, loginUser);
        } catch (Exception e) {
            log.error("getAdList controller error:" + e);
            msg.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
            if (e instanceof ServiceException) {
                msg.setMessage(e.getMessage());
            }
            msg.setMessage((UtilConstants.ResponseMsg.FAILED));
        }
        return msg;
    }
}
