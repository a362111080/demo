package com.zero.egg.controller;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zero.egg.annotation.PassToken;
import com.zero.egg.api.ApiConstants;
import com.zero.egg.api.dto.BaseResponse;
import com.zero.egg.model.CompanyUser;
import com.zero.egg.model.User;
import com.zero.egg.service.ICompanyUserService;
import com.zero.egg.service.IUserService;
import com.zero.egg.tool.MD5Utils;
import com.zero.egg.tool.TokenUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;


@Api(value="系统基础功能")
@RestController
@RequestMapping("/system")
public class LoginController {

	@Autowired
	private IUserService userService;
	@Autowired
	private ICompanyUserService companyUserService;
	
	@PassToken
    @ApiOperation(value="登录")
    @RequestMapping( value = "/login",method = RequestMethod.POST)
    public BaseResponse<Object> checklogin(@RequestParam @ApiParam(required=true,name="loginname",value="登录名") String loginname
    		,@RequestParam @ApiParam(required=true,name="loginPwd",value="登录密码") String loginPwd,HttpSession session ){
    	BaseResponse<Object> response = new BaseResponse<>(ApiConstants.ResponseCode.EXECUTE_ERROR, ApiConstants.ResponseMsg.EXECUTE_ERROR);
        try {
        	String pwd = MD5Utils.encode(loginPwd);
        	QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        	userQueryWrapper.eq("loginname", loginname)
        	.eq("password", pwd);
            User user=userService.getOne(userQueryWrapper);
            if (null != user) {
            	//生成token
                String accessToken=TokenUtils.createJwtToken(user.getId());
                Map<String, Object> map = new HashMap<>();
                map.put("token", accessToken);
                map.put("userType", 2);
                map.put("userTypeName", "店铺员工");
                map.put("user", user);
                response.setData(map);
                response.setCode(ApiConstants.ResponseCode.SUCCESS);
                response.setMsg("登录成功");
            }else{
            	QueryWrapper<CompanyUser> cUserQueryWrapper = new QueryWrapper<>();
            	cUserQueryWrapper.eq("loginname", loginname)
            	.eq("password", pwd);
            	CompanyUser companyUser = companyUserService.getOne(cUserQueryWrapper);
            	if (companyUser != null) {
            		//生成token
                    String accessToken=TokenUtils.createJwtToken(companyUser.getId());
                    Map<String, Object> map = new HashMap<>();
                    map.put("token", accessToken);
                    map.put("user", companyUser);
                    map.put("userType", 1);
                    map.put("userTypeName", "企业员工");
                    response.setData(map);
                    response.setCode(ApiConstants.ResponseCode.SUCCESS);
                    response.setMsg("登录成功");
				}else {
					 response.setMsg("登录失败!，请联系管理员");
				}
            }

        } catch (Exception e) {
        	 response.setMsg("登录失败!，请联系管理员");
        }
        return  response;
    }

}
