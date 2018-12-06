package com.zero.egg.controller;
import com.zero.egg.model.LoginInfo;
import com.zero.egg.service.SystemService;
import com.zero.egg.tool.Message;
import com.zero.egg.tool.UtilConstants;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@Api(value="系统基础功能")
@RestController
@RequestMapping("/system")
public class SystemController {

    @Autowired
    private SystemService  systemService;
    @RequestMapping( value = "/login",method = RequestMethod.POST)
    public Message checklogin(@RequestParam String strUserName,@RequestParam String passWord ){
        Message ms = new Message();
        try {
            LoginInfo info=systemService.checklogin(strUserName,passWord);
            if (null != info.getStrstorecode()) {
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
}
