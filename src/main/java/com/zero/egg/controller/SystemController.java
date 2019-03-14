package com.zero.egg.controller;
import com.zero.egg.api.dto.SysConstants;
import com.zero.egg.model.LoginInfo;
import com.zero.egg.responseDTO.EmployeeLoginResponseDTO;
import com.zero.egg.service.SystemService;
import com.zero.egg.tool.Message;
import com.zero.egg.tool.UtilConstants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@Api(value="系统基础功能")
@RestController
@RequestMapping("/system")
public class SystemController {

    @Autowired
    private SystemService  systemService;
    @ApiOperation(value="老板后台PC端登录")
    @RequestMapping( value = "/login",method = RequestMethod.POST)
    public Message checklogin(@RequestBody LoginInfo  requestLogin,HttpSession session ){
        Message ms = new Message();
        try {
            LoginInfo info=systemService.checklogin(requestLogin.getStrPassName(),requestLogin.getStrPassword());
            if (null != info.getStrstorecode()) {
                info.setStrPassName(requestLogin.getStrPassName());
                info.setStrPassword(requestLogin.getStrPassword());
                session.setAttribute(SysConstants.LOGIN_USER, info);
                ms.setData(info);
                ms.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
                ms.setMessage(UtilConstants.ResponseMsg.SUCCESS);
            }
            else
            {
                ms.setData("登录失败!，请联系管理员");
                ms.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
                ms.setMessage(UtilConstants.ResponseMsg.FAILED);
            }

        } catch (Exception e) {
            ms.setData("登录失败!，请联系管理员");
            ms.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
            ms.setMessage(UtilConstants.ResponseMsg.FAILED);
        }
        return  ms;
    }

    @ApiOperation(value="移动端登录")
    @RequestMapping( value = "/mobilelogin",method = RequestMethod.POST)
    public  Message  Mobilelogin(@RequestBody LoginInfo  requestLogin )
    {
        Message ms = new Message();
        try{

            EmployeeLoginResponseDTO  info=systemService.Mobilelogin(requestLogin.getStrPassName(),requestLogin.getStrPassword());
            if (null != info.getStrUserCode()) {
                ms.setData(info);
                ms.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
                ms.setMessage(UtilConstants.ResponseMsg.SUCCESS);
            }
            else
            {
                ms.setData("登录失败!，请联系管理员");
                ms.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
                ms.setMessage(UtilConstants.ResponseMsg.FAILED);
            }
        }
        catch (Exception e) {
            ms.setData("登录失败!，请联系管理员");
            ms.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
            ms.setMessage(UtilConstants.ResponseMsg.FAILED);
        }
        return  ms;
    }

}
