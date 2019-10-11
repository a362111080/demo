package com.zero.egg.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zero.egg.annotation.LoginToken;
import com.zero.egg.api.ApiConstants;
import com.zero.egg.requestDTO.AddOrderBillRequestDTO;
import com.zero.egg.requestDTO.CancelMissedBillReqeustDTO;
import com.zero.egg.requestDTO.DeleteCompletedBillReqeustDTO;
import com.zero.egg.requestDTO.LoginUser;
import com.zero.egg.requestDTO.OrderBillListReqeustDTO;
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

    @PostMapping(value = "/addnewbill")
    @LoginToken
    @ApiOperation("新增订单")
    public Message addNewBill(@RequestBody AddOrderBillRequestDTO addOrderBillRequestDTO) {
        //参数校验
        BeanValidator.check(addOrderBillRequestDTO);
        Message msg = new Message();
        try {
            LoginUser loginUser = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
            addOrderBillRequestDTO.setUserId(loginUser.getId());
            msg = orderBillService.addNewBill(addOrderBillRequestDTO);
        } catch (Exception e) {
            log.error("addnewbill controller error:" + e);
            msg.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
            if (e instanceof ServiceException) {
                msg.setMessage(e.getMessage());
            }
            msg.setMessage((UtilConstants.ResponseMsg.FAILED));
        }
        return msg;
    }

    @PostMapping(value = "/listbill")
    @LoginToken
    @ApiOperation("订单列表")
    public Message listBill(@RequestBody OrderBillListReqeustDTO orderBillListReqeustDTO) {
        Message msg = new Message();
        try {
            LoginUser loginUser = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
            orderBillListReqeustDTO.setUserId(loginUser.getId());
            IPage iPage = orderBillService.listOrderBill(orderBillListReqeustDTO);
            msg.setData(iPage);
            msg.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
            msg.setMessage(UtilConstants.ResponseMsg.SUCCESS);
        } catch (Exception e) {
            log.error("listBill controller error:" + e);
            msg.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
            if (e instanceof ServiceException) {
                msg.setMessage(e.getMessage());
            }
            msg.setMessage((UtilConstants.ResponseMsg.FAILED));
        }
        return msg;
    }

    @PostMapping(value = "/cancelmissedbill")
    @LoginToken
    @ApiOperation("取消未接单的订货账单")
    public Message listBill(@RequestBody CancelMissedBillReqeustDTO cancelMissedBillReqeustDTO) {
        //参数校验
        BeanValidator.check(cancelMissedBillReqeustDTO);
        Message msg = new Message();
        try {
            LoginUser loginUser = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
            cancelMissedBillReqeustDTO.setUserId(loginUser.getId());
            orderBillService.cancelorderBill(cancelMissedBillReqeustDTO);
            msg.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
            msg.setMessage(UtilConstants.ResponseMsg.SUCCESS);
        } catch (Exception e) {
            log.error("cancelmissedbill controller error:" + e);
            msg.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
            if (e instanceof ServiceException) {
                msg.setMessage(e.getMessage());
            }
            msg.setMessage((UtilConstants.ResponseMsg.FAILED));
        }
        return msg;
    }

    @PostMapping(value = "/deletecompletedbill")
    @LoginToken
    @ApiOperation("删除已经完成的订货账单")
    public Message deleteCompletedBill(@RequestBody DeleteCompletedBillReqeustDTO deleteCompletedBillReqeustDTO) {
        //参数校验
        BeanValidator.check(deleteCompletedBillReqeustDTO);
        Message msg = new Message();
        try {
            LoginUser loginUser = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
            deleteCompletedBillReqeustDTO.setUserId(loginUser.getId());
            orderBillService.deleteCompletedBill(deleteCompletedBillReqeustDTO);
            msg.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
            msg.setMessage(UtilConstants.ResponseMsg.SUCCESS);
        } catch (Exception e) {
            log.error("listBill controller error:" + e);
            msg.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
            if (e instanceof ServiceException) {
                msg.setMessage(e.getMessage());
            }
            msg.setMessage((UtilConstants.ResponseMsg.FAILED));
        }
        return msg;
    }
}
