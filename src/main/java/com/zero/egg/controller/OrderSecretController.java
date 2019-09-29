package com.zero.egg.controller;

import com.zero.egg.annotation.LoginToken;
import com.zero.egg.api.ApiConstants;
import com.zero.egg.requestDTO.BindSecretRequestDTO;
import com.zero.egg.requestDTO.LoginUser;
import com.zero.egg.service.OrderSecretService;
import com.zero.egg.tool.BeanValidator;
import com.zero.egg.tool.Message;
import com.zero.egg.tool.ServiceException;
import com.zero.egg.tool.UtilConstants;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName OrderSecretController
 * @Description 订货平台秘钥管理Controller
 * @Author lyming
 * @Date 2019/8/27 11:28 上午
 **/
@Slf4j
@RestController
@RequestMapping("/secret")
public class OrderSecretController {

    @Autowired
    HttpServletRequest request;
    @Autowired
    private OrderSecretService orderSecretService;


    @ApiOperation("订货平台用户绑定鸡蛋平台店铺")
    @PostMapping("/bind")
    @LoginToken
    public Message bindSecret(@RequestBody BindSecretRequestDTO bindSecretRequestDTO){
        //参数校验
        BeanValidator.check(bindSecretRequestDTO);
        Message msg = new Message();
        try {
            String secret = bindSecretRequestDTO.getSecret();
            LoginUser user = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
            msg = orderSecretService.bindSecret(secret,user);

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

    @ApiOperation("获取绑定过的店铺列表")
    @PostMapping("/getshoplist")
    @LoginToken
    public Message getShoplist(){
        Message msg = new Message();
        try {
            LoginUser user = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
            msg = orderSecretService.getShopList(user);
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


