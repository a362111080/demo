package com.zero.egg.controller;

import com.zero.egg.annotation.LoginToken;
import com.zero.egg.api.ApiConstants;
import com.zero.egg.requestDTO.LoginUser;
import com.zero.egg.requestDTO.OrderAddressDTO;
import com.zero.egg.requestDTO.RemAddressRequestDTO;
import com.zero.egg.service.OrderAddressService;
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
import java.util.Date;

/**
 * @ClassName AddressController
 * @Description 订货平台地址Controller
 * @Author lyming
 * @Date 2019-08-21 09:55
 **/
@RestController
@RequestMapping(value ="/address")
@Slf4j
@Api(value ="订货平台地址管理")
public class AddressController {

    @Autowired
    private OrderAddressService orderAddressService;

    @Autowired
    HttpServletRequest request;

    @ApiOperation("创建地址")
    @PostMapping(value = "/create")
    @LoginToken
    public Message create(@RequestBody  OrderAddressDTO orderAddressDTO) {
        Message msg = new Message();
        BeanValidator.check(orderAddressDTO);
        try {
            LoginUser user = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
            orderAddressDTO.setUserId(user.getId());
            orderAddressDTO.setCreatetime(new Date());
            orderAddressDTO.setCreator(user.getId());
            msg = orderAddressService.createAddress(orderAddressDTO);
        } catch (Exception e) {
            log.error("create address controller error:" + e);
            msg.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
            if (e instanceof ServiceException) {
                msg.setMessage(e.getMessage());
            }
            msg.setMessage((UtilConstants.ResponseMsg.FAILED));
        }
        return msg;
    }

    @ApiOperation("更新地址")
    @PostMapping(value = "/update")
    @LoginToken
    public Message update(@RequestBody OrderAddressDTO orderAddressDTO) {
        //参数校验
        BeanValidator.check(orderAddressDTO);
        Message msg = new Message();
        try {
            LoginUser user = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
            orderAddressDTO.setUserId(user.getId());
            orderAddressDTO.setModifytime(new Date());
            orderAddressDTO.setModifier(user.getId());
            msg = orderAddressService.updateAddress(orderAddressDTO);
        } catch (Exception e) {
            log.error("update address controller error:" + e);
            msg.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
            if (e instanceof ServiceException) {
                msg.setMessage(e.getMessage());
            }
            msg.setMessage((UtilConstants.ResponseMsg.FAILED));
        }
        return msg;
    }

    @ApiOperation("删除用户收货地址(可批量)")
    @PostMapping(value = "/delete")
    @LoginToken
    public Message delete(@RequestBody RemAddressRequestDTO remAddressRequestDTO) {
        //参数校验
        BeanValidator.check(remAddressRequestDTO);
        Message msg = new Message();
        try {
            LoginUser user = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
            msg = orderAddressService.removeAddress(remAddressRequestDTO,user);
        }catch (Exception e) {
            log.error("delete address controller error:" + e);
            msg.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
            if (e instanceof ServiceException) {
                msg.setMessage(e.getMessage());
            }
            msg.setMessage((UtilConstants.ResponseMsg.FAILED));
        }
        return msg;
    }

    @ApiOperation("查看所有收货地址")
    @PostMapping(value = "/list")
    @LoginToken
    public Message list() {
        Message msg = new Message();
        try {
            LoginUser user = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
            msg = orderAddressService.listAddress(user);
        }catch (Exception e) {
            log.error("list address controller error:" + e);
            msg.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
            if (e instanceof ServiceException) {
                msg.setMessage(e.getMessage());
            }
            msg.setMessage((UtilConstants.ResponseMsg.FAILED));
        }
        return msg;
    }
}
