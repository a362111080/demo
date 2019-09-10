package com.zero.egg.controller;

import com.zero.egg.annotation.LoginToken;
import com.zero.egg.api.ApiConstants;
import com.zero.egg.requestDTO.LoginUser;
import com.zero.egg.requestDTO.QueryRetailDeatilsRequestDTO;
import com.zero.egg.requestDTO.SaveRetailDeatilsRequestDTO;
import com.zero.egg.service.BdRetailService;
import com.zero.egg.tool.BeanValidator;
import com.zero.egg.tool.Message;
import com.zero.egg.tool.UtilConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @ClassName RetailController
 * @Description 零售记录Controller
 * @Author lyming
 * @Date 2019-07-31 15:40
 **/
@RestController
@RequestMapping(value = "/retail")
@Slf4j
public class RetailController {

    @Resource
    private BdRetailService bdRetailService;

    @LoginToken
    @PostMapping(value = "/getretaildetails")
    public Message getRetailDetails(HttpServletRequest request,@RequestBody QueryRetailDeatilsRequestDTO queryRetailDeatilsRequestDTO) {
        Message message = new Message();
        BeanValidator.check(queryRetailDeatilsRequestDTO);
        try {
            LoginUser user = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
            queryRetailDeatilsRequestDTO.setCompanyId(user.getCompanyId());
            queryRetailDeatilsRequestDTO.setShopId(user.getShopId());
            message = bdRetailService.listRetailDetails(queryRetailDeatilsRequestDTO);
        } catch (Exception e) {
            message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
            message.setMessage(UtilConstants.ResponseMsg.FAILED);
        }
        return message;
    }

    @LoginToken
    @PostMapping(value = "retailgoods")
    public Message retailGoods(HttpServletRequest request,@RequestBody SaveRetailDeatilsRequestDTO saveRetailDeatilsRequestDTO) {
        Message message = new Message();
        BeanValidator.check(saveRetailDeatilsRequestDTO);
        try {
            LoginUser user = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
            saveRetailDeatilsRequestDTO.setCompanyId(user.getCompanyId());
            saveRetailDeatilsRequestDTO.setShopId(user.getShopId());
            saveRetailDeatilsRequestDTO.setCreator(user.getId());
            saveRetailDeatilsRequestDTO.setCreatetime(new Date());
            message = bdRetailService.insertRetailDetails(saveRetailDeatilsRequestDTO);
        } catch (Exception e) {
            message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
            message.setMessage(UtilConstants.ResponseMsg.FAILED);
        }
        return message;
    }
}
