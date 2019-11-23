package com.zero.egg.controller;

import com.github.pagehelper.PageInfo;
import com.zero.egg.annotation.LoginToken;
import com.zero.egg.api.ApiConstants;
import com.zero.egg.enums.ModeTypeEnum;
import com.zero.egg.requestDTO.AddCartGoodRequestDTO;
import com.zero.egg.requestDTO.DeleteCartGoodsRequestDTO;
import com.zero.egg.requestDTO.LoginUser;
import com.zero.egg.requestDTO.OrderCartListRequestDTO;
import com.zero.egg.requestDTO.UpdateCartGoodsCheckRequestDTO;
import com.zero.egg.requestDTO.UpdateCartGoodsNumRequestDTO;
import com.zero.egg.requestDTO.UpdateCartGoodsSpecificationRequestDTO;
import com.zero.egg.service.OrderCartService;
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
 * @ClassName OrderCartController
 * @Description
 * @Author lyming
 * @Date 2019/9/30 3:24 下午
 **/
@RestController
@RequestMapping("/cart")
@Api(value = "购物车管理")
@Slf4j
public class OrderCartController {

    @Autowired
    private OrderCartService orderCartService;

    @Autowired
    HttpServletRequest request;

    /**
     * 加入购物车
     *
     * @return
     */
    @PostMapping(value = "/addproducttocart")
    @LoginToken
    @ApiOperation("加入商品到购物车")
    public Message addProductToCart(@RequestBody AddCartGoodRequestDTO addCartGoodRequestDTO) {
        BeanValidator.check(addCartGoodRequestDTO);
        Message msg = new Message();
        try {
            String result = ModeTypeEnum.getValueByKey(Integer.parseInt(addCartGoodRequestDTO.getWeightMode()));
            if (result.equals("")) {
                throw new ServiceException("计重方式参数错误");
            }
            LoginUser loginUser = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
            orderCartService.addProductToCart(addCartGoodRequestDTO, loginUser);
            msg.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
            msg.setMessage(UtilConstants.ResponseMsg.SUCCESS);
        } catch (Exception e) {
            log.error("addProductToCart controller error:" + e);
            msg.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
            if (e instanceof ServiceException) {
                msg.setMessage(e.getMessage());
            }
            msg.setMessage((UtilConstants.ResponseMsg.FAILED));
        }
        return msg;
    }

    @PostMapping(value = "/cartlist")
    @LoginToken
    @ApiOperation("购物车列表")
    public Message cartList(@RequestBody OrderCartListRequestDTO orderCartListRequestDTO) {
        //参数校验
        BeanValidator.check(orderCartListRequestDTO);
        Message msg = new Message();
        try {
            LoginUser loginUser = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
            orderCartListRequestDTO.setUserId(loginUser.getId());
            PageInfo pageInfo = orderCartService.cartList(orderCartListRequestDTO);
            msg.setData(pageInfo);
            msg.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
            msg.setMessage(UtilConstants.ResponseMsg.SUCCESS);
        } catch (Exception e) {
            log.error("cartlists controller error:" + e);
            msg.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
            if (e instanceof ServiceException) {
                msg.setMessage(e.getMessage());
            }
            msg.setMessage((UtilConstants.ResponseMsg.FAILED));
        }
        return msg;
    }

    @PostMapping(value = "/deletecartgoods")
    @LoginToken
    @ApiOperation("删除购物车商品")
    public Message deleteCartGoods(@RequestBody DeleteCartGoodsRequestDTO deleteCartGoodsRequestDTO) {
        //参数校验
        BeanValidator.check(deleteCartGoodsRequestDTO);
        Message msg = new Message();
        try {
            LoginUser loginUser = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
            deleteCartGoodsRequestDTO.setUserId(loginUser.getId());
            orderCartService.deleteCartGoods(deleteCartGoodsRequestDTO);
            msg.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
            msg.setMessage(UtilConstants.ResponseMsg.SUCCESS);
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

    @PostMapping(value = "/updatecartgoodsspecifacation")
    @LoginToken
    @ApiOperation("更新购物车商品规格")
    public Message updateCartGoodsSpecifacation(@RequestBody UpdateCartGoodsSpecificationRequestDTO updateCartGoodsSpecificationRequestDTO) {
        //参数校验
        BeanValidator.check(updateCartGoodsSpecificationRequestDTO);
        Message msg = new Message();
        try {
            LoginUser loginUser = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
            updateCartGoodsSpecificationRequestDTO.setUserId(loginUser.getId());
            orderCartService.updateCartGoodsSpecifacation(updateCartGoodsSpecificationRequestDTO);
            msg.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
            msg.setMessage(UtilConstants.ResponseMsg.SUCCESS);
        } catch (Exception e) {
            log.error("updatecartgoodsspecifacation controller error:" + e);
            msg.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
            if (e instanceof ServiceException) {
                msg.setMessage(e.getMessage());
            }
            msg.setMessage((UtilConstants.ResponseMsg.FAILED));
        }
        return msg;
    }

    @PostMapping(value = "/updatecartgoodsnum")
    @LoginToken
    @ApiOperation("更新购物车商品数量")
    public Message updateCartGoodsNum(@RequestBody UpdateCartGoodsNumRequestDTO updateCartGoodsNumRequestDTO) {
        //参数校验
        BeanValidator.check(updateCartGoodsNumRequestDTO);
        Message msg = new Message();
        try {
            LoginUser loginUser = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
            updateCartGoodsNumRequestDTO.setUserId(loginUser.getId());
            orderCartService.updateCartGoodsNum(updateCartGoodsNumRequestDTO);
            msg.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
            msg.setMessage(UtilConstants.ResponseMsg.SUCCESS);
        } catch (Exception e) {
            log.error("updatecartgoodsnum controller error:" + e);
            msg.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
            if (e instanceof ServiceException) {
                msg.setMessage(e.getMessage());
            }
            msg.setMessage((UtilConstants.ResponseMsg.FAILED));
        }
        return msg;
    }

    @PostMapping(value = "/updatecartgoodscheck")
    @LoginToken
    @ApiOperation("更新购物车商品是否被勾选")
    public Message updateCartGoodsCheck(@RequestBody UpdateCartGoodsCheckRequestDTO updateCartGoodsCheckRequestDTO) {
        //参数校验
        BeanValidator.check(updateCartGoodsCheckRequestDTO);
        Message msg = new Message();
        try {
            LoginUser loginUser = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
            updateCartGoodsCheckRequestDTO.setUserId(loginUser.getId());
            orderCartService.updateCartGoodsCheck(updateCartGoodsCheckRequestDTO);
            msg.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
            msg.setMessage(UtilConstants.ResponseMsg.SUCCESS);
        } catch (Exception e) {
            log.error("updatecartgoodscheck controller error:" + e);
            msg.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
            if (e instanceof ServiceException) {
                msg.setMessage(e.getMessage());
            }
            msg.setMessage((UtilConstants.ResponseMsg.FAILED));
        }
        return msg;
    }
}
