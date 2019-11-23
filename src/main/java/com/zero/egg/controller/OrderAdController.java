package com.zero.egg.controller;

import com.zero.egg.annotation.LoginToken;
import com.zero.egg.api.ApiConstants;
import com.zero.egg.model.OrderAd;
import com.zero.egg.requestDTO.LoginUser;
import com.zero.egg.service.OrderAdService;
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
 * @ClassName OrderAdController
 * @Description 订货平台广告管理
 * @Author lyming
 * @Date 2019/8/28 10:17 上午
 **/
@Api("订货平台广告管理")
@Slf4j
@RestController("/orderad")
public class OrderAdController {

    @Autowired
    private OrderAdService orderAdService;

    @Autowired
    HttpServletRequest request;

    @ApiOperation("获取广告列表")
    @RequestMapping(value = "/getadlist", method = RequestMethod.GET)
    public Message getAdList() {
        Message msg = new Message();
        try {
            msg = orderAdService.getAdList();
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
    @ApiOperation(value = "新增广告")
    @RequestMapping(value = "/addorderad", method = RequestMethod.POST)
    public Message<Object> addorderad(@RequestBody OrderAd model) {
        Message<Object> message = new Message<Object>();
        //当前登录用户
        LoginUser loginUser = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
        try {
            int strval = orderAdService.addOrderad(model, loginUser);
            if (strval > 0) {
                message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
                message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
            } else {
                message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
                message.setMessage("操作失败");
            }
        } catch (Exception e) {
            message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
            if (e instanceof ServiceException) {
                message.setMessage(e.getMessage());
            }
            message.setMessage((UtilConstants.ResponseMsg.FAILED));
        }
        return message;
    }

    @LoginToken
    @ApiOperation(value = "编辑广告")
    @RequestMapping(value = "/editorderad", method = RequestMethod.POST)
    public Message<Object> editorderad(@RequestBody OrderAd model) {
        Message<Object> message = new Message<Object>();
        //当前登录用户
        LoginUser loginUser = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
        try {
            int strval = orderAdService.editorderad(model, loginUser);
            if (strval > 0) {
                message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
                message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
            } else {
                message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
                message.setMessage("操作失败");
            }
        } catch (Exception e) {
            message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
            if (e instanceof ServiceException) {
                message.setMessage(e.getMessage());
            }
            message.setMessage((UtilConstants.ResponseMsg.FAILED));
        }
        return message;
    }
}





