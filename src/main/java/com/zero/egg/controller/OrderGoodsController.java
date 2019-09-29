package com.zero.egg.controller;

import com.zero.egg.api.ApiConstants;
import com.zero.egg.requestDTO.LoginUser;
import com.zero.egg.requestDTO.OrderGoodsRequestDTO;
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

/**
 * @ClassName OrderGoodsController
 * @Description 订货平台商品相关
 * @Author lyming
 * @Date 2019/9/29 10:10 上午
 **/
@Api("订货平台商品管理")
@Slf4j
@RestController("/ordergoods")
public class OrderGoodsController {

    @Autowired
    HttpServletRequest request;

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
}
