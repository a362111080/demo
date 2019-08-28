package com.zero.egg.controller;

import com.zero.egg.service.OrderAdService;
import com.zero.egg.tool.Message;
import com.zero.egg.tool.ServiceException;
import com.zero.egg.tool.UtilConstants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    @RequestMapping(value ="/getadlist",method = RequestMethod.GET)
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
}
