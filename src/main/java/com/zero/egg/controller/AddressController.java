package com.zero.egg.controller;

import com.zero.egg.api.ApiConstants;
import com.zero.egg.requestDTO.LoginUser;
import com.zero.egg.requestDTO.OrderAddressDTO;
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
 * @Description TODO
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
}
