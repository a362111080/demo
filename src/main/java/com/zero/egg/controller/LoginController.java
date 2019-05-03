package com.zero.egg.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zero.egg.annotation.PassToken;
import com.zero.egg.api.ApiConstants;
import com.zero.egg.api.dto.BaseResponse;
import com.zero.egg.enums.CompanyUserEnums;
import com.zero.egg.enums.UserEnums;
import com.zero.egg.model.CompanyUser;
import com.zero.egg.model.User;
import com.zero.egg.model.WechatAuth;
import com.zero.egg.service.ICompanyUserService;
import com.zero.egg.service.IUserService;
import com.zero.egg.service.WechatAuthService;
import com.zero.egg.tool.AESUtil;
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

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;


@Api(value = "系统基础功能")
@RestController
@RequestMapping("/system")
public class LoginController {

    @Autowired
    private IUserService userService;
    @Autowired
    private ICompanyUserService companyUserService;
    @Autowired
    private WechatAuthService wechatAuthService;

    @PassToken
    @ApiOperation(value = "登录")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public BaseResponse<Object> checklogin(@RequestParam @ApiParam(required = true, name = "loginname", value = "登录名") String loginname
            , @RequestParam @ApiParam(required = true, name = "loginPwd", value = "登录密码") String loginPwd, HttpServletRequest request) {
        BaseResponse<Object> response;
        try {
            String pwd = MD5Utils.encode(loginPwd);
            QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
            userQueryWrapper.eq("loginname", loginname)
                    .eq("password", pwd)
                    .eq("dr", false)
                    .eq("status", UserEnums.Status.Normal.index().toString());
            User user = userService.getOne(userQueryWrapper);
            Map<String, Object> map;
            //需要绑定的本地设备id(除了企业
            String bindId = null;
            if (null != user) {
                response = new BaseResponse<>();
                int type = user.getType();
                //生成token
                String accessToken = TokenUtils.createJwtToken(user.getId());
                map = new HashMap<>();
                map.put("token", accessToken);
                map.put("userType", type);
                map.put("userTypeName", UserEnums.Type.note(type));
                map.put("user", user);
                response.setData(map);
                response.setCode(ApiConstants.ResponseCode.SUCCESS);
                response.setMsg("登录成功");
            } else {
                response = new BaseResponse<>();
                QueryWrapper<CompanyUser> cUserQueryWrapper = new QueryWrapper<>();
                cUserQueryWrapper.eq("loginname", loginname)
                        .eq("password", pwd)
                        .eq("dr", false)
                        .eq("status", CompanyUserEnums.Status.Normal.index().toString());
                CompanyUser companyUser = companyUserService.getOne(cUserQueryWrapper);
                if (companyUser != null) {
                    //生成token
                    String accessToken = TokenUtils.createJwtToken(companyUser.getId());
                    map = new HashMap<>();
                    map.put("token", accessToken);
                    map.put("user", companyUser);
                    map.put("userTypeName", "企业用户");
                    response.setData(map);
                    response.setCode(ApiConstants.ResponseCode.SUCCESS);
                    response.setMsg("登录成功");
                } else {
                    response = new BaseResponse<>(ApiConstants.ResponseCode.EXECUTE_ERROR, ApiConstants.ResponseMsg.EXECUTE_ERROR);
                    response.setMsg("登录失败!，请联系管理员");
                }
            }
            //如果能从header里面取到wxSessionkey,则证明该用户需要绑定本地账户
            if (null != request.getHeader("wxSessionkey") && !"".equals(request.getHeader("wxSessionkey"))) {
                map = (Map<String, Object>) response.getData();
                String userTypeName = (String) map.get("userTypeName");
                //如果为企业用户或者PC端,则返回错误提示:企业用户和PC端不能用小程序登录
                if (userTypeName.equals("企业用户") || userTypeName.equals(UserEnums.Type.Pc.note())) {
                    response = new BaseResponse<>(ApiConstants.ResponseCode.EXECUTE_ERROR, ApiConstants.ResponseMsg.EXECUTE_ERROR);
                    response.setMsg("企业用户和PC端不能用小程序登录");
                    return response;
                }
                //AES解密获取openid
                String openid = AESUtil.decrypt(request.getHeader("wxSessionkey"), AESUtil.KEY);
                String userId = user.getId();
                /** 检验用户是否已被绑定*/
                if (wechatAuthService.getCountByUserId(userId) > 0) {
                    response = new BaseResponse<>(ApiConstants.ResponseCode.EXECUTE_ERROR, ApiConstants.ResponseMsg.EXECUTE_ERROR);
                    response.setMsg("该账号已被其他微信绑定!");
                    return response;
                }
                WechatAuth wechatAuth = new WechatAuth();
                wechatAuth.setOpenid(openid);
                wechatAuth.setType(UserEnums.Type.index(userTypeName));
                wechatAuth.setUserId(userId);
                wechatAuthService.bindWechatAuth(wechatAuth);

            }
            return response;

        } catch (Exception e) {
            response = new BaseResponse<>(ApiConstants.ResponseCode.EXECUTE_ERROR, ApiConstants.ResponseMsg.EXECUTE_ERROR);
            response.setMsg("登录失败!，请联系管理员");
            return response;
        }


    }

}