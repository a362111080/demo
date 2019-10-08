package com.zero.egg.controller;


import com.zero.egg.annotation.LoginToken;
import com.zero.egg.api.ApiConstants;
import com.zero.egg.requestDTO.AddOrderBillRequestDTO;
import com.zero.egg.requestDTO.LoginUser;
import com.zero.egg.service.OrderBillService;
import com.zero.egg.tool.BeanValidator;
import com.zero.egg.tool.Message;
import com.zero.egg.tool.ServiceException;
import com.zero.egg.tool.UtilConstants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author lym
 */
@RestController
@RequestMapping("/orderbill")
@Api(value = "订单平台订单管理")
@Slf4j
public class OrderBillController {

    @Autowired
    private OrderBillService orderBillService;

    @Autowired
    HttpServletRequest request;

    @PostMapping(value = "/deletecartgoods")
    @LoginToken
    @ApiOperation("新增订单")
    public Message deleteCartGoods(@RequestBody AddOrderBillRequestDTO addOrderBillRequestDTO) {
        //参数校验
        BeanValidator.check(addOrderBillRequestDTO);
        Message msg = new Message();
        try {
            LoginUser loginUser = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
            addOrderBillRequestDTO.setUserId(loginUser.getId());
            msg = orderBillService.addNewBill(addOrderBillRequestDTO);
        } catch (Exception e) {
            log.error("deletecartgoods controller error:" + e);
            msg.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
            if (e instanceof ServiceException) {
                msg.setMessage(e.getMessage());
            }
            msg.setMessage((UtilConstants.ResponseMsg.FAILED));
        }
        return msg;
    }
}
