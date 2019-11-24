package com.zero.egg.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zero.egg.annotation.LoginToken;
import com.zero.egg.api.ApiConstants;
import com.zero.egg.enums.BillEnums;
import com.zero.egg.model.OrderBill;
import com.zero.egg.model.OrderBillDetail;
import com.zero.egg.model.OrderGoodSpecification;
import com.zero.egg.model.OrderGoods;
import com.zero.egg.requestDTO.*;
import com.zero.egg.service.OrderBillService;
import com.zero.egg.tool.BeanValidator;
import com.zero.egg.tool.Message;
import com.zero.egg.tool.ServiceException;
import com.zero.egg.tool.UtilConstants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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

    @PostMapping(value = "/getorderbilldetails")
    @LoginToken
    @ApiOperation("获取订货平台订单细节")
    public Message getOrderBillDetails(@RequestBody OrderBillDetailsRequestDTO orderBillDetailsRequestDTO) {
        //参数校验
        BeanValidator.check(orderBillDetailsRequestDTO);
        Message msg = new Message();
        try {
            LoginUser loginUser = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
            orderBillDetailsRequestDTO.setUserId(loginUser.getId());
            msg = orderBillService.getOrderBillDetails(orderBillDetailsRequestDTO);
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


    @PostMapping(value = "/editorderstatus")
    @LoginToken
    @ApiOperation("编辑订单状态")
    public Message editorderstatus(@RequestBody OrderBill model) {
        Message<Object> message = new Message<Object>();
        //当前登录用户
        model.setOrderStatus(BillEnums.OrderStatus.Received.index());
        LoginUser loginUser = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
        model.setShopId(loginUser.getShopId());
        model.setCompanyId(loginUser.getCompanyId());
        if(model.getOrderStatus().toString()==BillEnums.OrderStatus.Received.index().toString())
        {
            model.setAcceptStatus(BillEnums.OrderStatus.Received.index().toString());
        }
        if(model.getOrderStatus().toString()==BillEnums.OrderStatus.Canceld.index().toString())
        {
            model.setAcceptStatus(BillEnums.OrderStatus.Canceld.index().toString());
        }
        orderBillService.editorderstatus(model);
        message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
        message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
        return message;
    }


    @LoginToken
    @ApiOperation(value="查询店铺订单")
    @RequestMapping(value="/queryshoporder",method= RequestMethod.POST)
    public Message<Object> queryshoporder(@RequestBody OrderGoodsRequestDTO model) {
        Message<Object> message = new Message<Object>();
        //当前登录用户
        LoginUser loginUser = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
        PageHelper.startPage(model.getCurrent().intValue(), model.getSize().intValue());
        model.setShopId(loginUser.getShopId());
        model.setCompanyId(loginUser.getCompanyId());
        if (loginUser.getCompanyId()!=null) {
            List<OrderBill> ResponseDTO=orderBillService.queryshoporder(model);
            if (ResponseDTO.size()>0) {
                for (int m = 0; m < ResponseDTO.size(); m++) {
                    List<OrderBillDetail> spec = orderBillService.GetOrderGoodDelList(ResponseDTO.get(m));
                    ResponseDTO.get(m).setOrderDetlList(spec);
                }
            }
            PageInfo<OrderBill> pageInfo = new PageInfo<>(ResponseDTO);
            message.setData(pageInfo);
            message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
            message.setMessage(UtilConstants.ResponseMsg.SUCCESS);

        }
        else
        {
            message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
            message.setMessage("操作失败，无企业信息");
        }
        return message;
    }



    @LoginToken
    @ApiOperation(value="查询订单")
    @RequestMapping(value="/querystafforder",method= RequestMethod.POST)
    public Message<Object> querystafforder(@RequestBody OrderGoodsRequestDTO model) {
        Message<Object> message = new Message<Object>();
        //当前登录用户
        LoginUser loginUser = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
        PageHelper.startPage(model.getCurrent().intValue(), model.getSize().intValue());
        if (loginUser.getCompanyId()!=null) {
            List<OrderBill> ResponseDTO=orderBillService.queryshoporder(model);
            if (ResponseDTO.size()>0) {
                for (int m = 0; m < ResponseDTO.size(); m++) {
                    List<OrderBillDetail> spec = orderBillService.GetOrderGoodDelList(ResponseDTO.get(m));
                    ResponseDTO.get(m).setOrderDetlList(spec);
                }
            }
            PageInfo<OrderBill> pageInfo = new PageInfo<>(ResponseDTO);
            message.setData(pageInfo);
            message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
            message.setMessage(UtilConstants.ResponseMsg.SUCCESS);

        }
        else
        {
            message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
            message.setMessage("操作失败，无企业信息");
        }
        return message;
    }


}
