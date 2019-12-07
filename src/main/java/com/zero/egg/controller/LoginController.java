package com.zero.egg.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zero.egg.annotation.LoginToken;
import com.zero.egg.annotation.PassToken;
import com.zero.egg.api.ApiConstants;
import com.zero.egg.api.dto.BaseResponse;
import com.zero.egg.cache.JedisUtil;
import com.zero.egg.dao.BdCityMapper;
import com.zero.egg.dao.ShopMapper;
import com.zero.egg.enums.CompanyUserEnums;
import com.zero.egg.enums.UserEnums;
import com.zero.egg.model.BdCity;
import com.zero.egg.model.Company;
import com.zero.egg.model.CompanyUser;
import com.zero.egg.model.SaasUser;
import com.zero.egg.model.Shop;
import com.zero.egg.model.User;
import com.zero.egg.model.WechatAuth;
import com.zero.egg.requestDTO.LoginUser;
import com.zero.egg.service.ICompanyService;
import com.zero.egg.service.ICompanyUserService;
import com.zero.egg.service.IUserService;
import com.zero.egg.service.SaasUserService;
import com.zero.egg.service.WechatAuthService;
import com.zero.egg.tool.AESUtil;
import com.zero.egg.tool.MD5Utils;
import com.zero.egg.tool.Message;
import com.zero.egg.tool.TokenUtils;
import com.zero.egg.tool.UtilConstants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Api(value = "系统基础功能")
@RestController
@RequestMapping("/system")
@Slf4j
public class LoginController {

    @Autowired
    private IUserService userService;
    @Autowired
    private ICompanyUserService companyUserService;
    @Autowired
    private WechatAuthService wechatAuthService;
    @Autowired
    private JedisUtil.Keys jediskeys;
    @Autowired
    private JedisUtil.Strings jedisStrings;
    @Autowired
    private SaasUserService saasUserService;
    @Autowired
    private ShopMapper shopMapper;
    @Autowired
    private BdCityMapper bdCityMapper;
    @Autowired
    private ICompanyService iCompanyService;

    @PassToken
    @ApiOperation(value = "鸡蛋系统登录")
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

            if (null != user) {
                /**
                 * 根据账号密码生成数字签名作为rediskey
                 */
                QueryWrapper<Company> CompanyQueryWrapper = new QueryWrapper<>();
                CompanyQueryWrapper.eq("id", user.getCompanyId()).eq("dr", false);
                //验证企业是否过期
                Company company = iCompanyService.getOne(CompanyQueryWrapper);
                if (company != null) {
                    String redisKey = MD5Utils.encodeWithFixSalt(loginname + pwd);
                    response = new BaseResponse<>();
                    int type = user.getType();
                    //登录信息额外加入当前店铺地区信息
                    Shop shop = shopMapper.selectOne(new QueryWrapper<Shop>().select("city_id").eq("id", user.getShopId()));
                    StringBuffer sb = new StringBuffer();
                    //查出cityId上两级
                    String parent2 = bdCityMapper.selectOne(new QueryWrapper<BdCity>()
                            .select("parent_id")
                            .eq("id", shop.getCityId()))
                            .getParentId();
                    String parent1 = bdCityMapper.selectOne(new QueryWrapper<BdCity>()
                            .select("parent_id")
                            .eq("id", parent2))
                            .getParentId();
                    sb.append(parent1);
                    sb.append("," + parent2);
                    sb.append("," + shop.getCityId());
                    //生成token
                    String accessToken = TokenUtils.createJwtToken(user.getId());
                    jedisStrings.set(UtilConstants.RedisPrefix.USER_REDIS + redisKey, accessToken);
                    map = new HashMap<>();
                    map.put("shopAddress", sb);
                    map.put("token", redisKey);
                    map.put("userType", type);
                    map.put("userTypeName", UserEnums.Type.note(type));
                    map.put("user", user);
                    response.setData(map);
                    response.setCode(ApiConstants.ResponseCode.SUCCESS);
                    response.setMsg("登录成功");
                } else {
                    response = new BaseResponse<>(ApiConstants.ResponseCode.EXECUTE_ERROR, ApiConstants.ResponseMsg.EXECUTE_ERROR);
                    response.setMsg("您的账号已到期,请联系管理员续费");
                }
            } else {
                response = new BaseResponse<>();
                QueryWrapper<CompanyUser> cUserQueryWrapper = new QueryWrapper<>();
                cUserQueryWrapper.eq("loginname", loginname)
                        .eq("password", pwd)
                        .eq("dr", false)
                        .eq("status", CompanyUserEnums.Status.Normal.index().toString());
                CompanyUser companyUser = companyUserService.getOne(cUserQueryWrapper);

                QueryWrapper<Company> CompanyQueryWrapper = new QueryWrapper<>();
                CompanyQueryWrapper.eq("id", companyUser.getCompanyId()).eq("dr", false);
                //验证企业是否过期
                Company company = iCompanyService.getOne(CompanyQueryWrapper);
                if (companyUser != null && company != null) {
                    /**
                     * 根据账号密码生成数字签名作为rediskey
                     */
                    String redisKey = MD5Utils.encodeWithFixSalt(loginname + pwd);
                    //生成token
                    String accessToken = TokenUtils.createJwtToken(companyUser.getId());
                    jedisStrings.set(UtilConstants.RedisPrefix.USER_REDIS + redisKey, accessToken);
                    map = new HashMap<>();
                    map.put("token", redisKey);
                    map.put("user", companyUser);
                    map.put("userTypeName", "企业用户");
                    List<Shop> shopList = shopMapper.getShopListByCompanid(companyUser.getCompanyId());
                    map.put("shopList", shopList);
                    response.setData(map);
                    response.setCode(ApiConstants.ResponseCode.SUCCESS);
                    response.setMsg("登录成功");
                } else {
                    response = new BaseResponse<>(ApiConstants.ResponseCode.EXECUTE_ERROR, ApiConstants.ResponseMsg.EXECUTE_ERROR);
                    response.setMsg("您的账号已到期,请联系管理员续费");
                }
            }
            //如果能从header里面取到wxSessionkey,则证明该用户需要绑定本地账户
            if (null != request.getHeader("wxSessionkey") && !"".equals(request.getHeader("wxSessionkey"))) {
                map = (Map<String, Object>) response.getData();
                String userTypeName = (String) map.get("userTypeName");
                //如果为企业用户或者PC端,则返回错误提示:企业用户和PC端不能用小程序登录
                if ("企业用户".equals(userTypeName) || UserEnums.Type.Pc.note().equals(userTypeName)) {
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

    @PostMapping(value = "/logout")
    public Message logout(HttpServletRequest request) {
        Message message = new Message();
        try {
            /**
             * 拦截器里已经获取过一遍,这里一定能获取到,否则不会进方法
             */
            String redisKey = request.getHeader("token");
            /** 清除redis里的token信息 */
            jediskeys.del(UtilConstants.RedisPrefix.USER_REDIS + redisKey);
            //如果header里面有request.getHeader("wxSessionkey"),则解除绑定
            if (null != request.getHeader("wxSessionkey") || !"".equals(request.getHeader("wxSessionkey"))) {
                String openid = AESUtil.decrypt(request.getHeader("wxSessionkey"), AESUtil.KEY);
                wechatAuthService.cancelBind(openid);
            }
            message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
            message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
        } catch (Exception e) {
            log.error("logout exception:" + e);
            message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
            message.setMessage(UtilConstants.ResponseMsg.FAILED);
        }
        return message;
    }

    @PassToken
    @ApiOperation(value = "Saas系统登录")
    @RequestMapping(value = "/saaslogin", method = RequestMethod.POST)
    public BaseResponse<Object> saasLogin(@RequestParam @ApiParam(required = true, name = "loginname", value = "登录名") String loginname
            , @RequestParam @ApiParam(required = true, name = "loginPwd", value = "登录密码") String loginPwd, HttpServletRequest request) {
        BaseResponse<Object> response;
        Map<String, Object> map;
        try {
            String pwd = MD5Utils.encode(loginPwd);
            SaasUser saasUser = saasUserService.saasLogin(loginname, pwd);
            if (null != saasUser) {
                response = new BaseResponse<>();
                String redisKey = MD5Utils.encodeWithFixSalt(loginname + pwd);
                //生成token
                String accessToken = TokenUtils.createJwtToken(saasUser.getId());
                jedisStrings.set(UtilConstants.RedisPrefix.USER_REDIS + redisKey, accessToken);
                map = new HashMap<>();
                map.put("token", redisKey);
                map.put("user", saasUser);
                response.setData(map);
                response.setCode(ApiConstants.ResponseCode.SUCCESS);
                response.setMsg("登录成功");
            } else {
                response = new BaseResponse<>(ApiConstants.ResponseCode.EXECUTE_ERROR, ApiConstants.ResponseMsg.EXECUTE_ERROR);
                response.setMsg("登录失败!，请联系管理员");
            }
        } catch (Exception e) {
            log.error("saasLogin error:" + e);
            response = new BaseResponse<>(ApiConstants.ResponseCode.EXECUTE_ERROR, ApiConstants.ResponseMsg.EXECUTE_ERROR);
            response.setMsg("登录失败!，请联系管理员");
        }
        return response;
    }

    @LoginToken
    @ApiOperation(value = "订货平台获取合作的相关店铺列表")
    @RequestMapping(value = "/ordershoplist", method = RequestMethod.POST)
    public Message orderShopList(@RequestParam HttpServletRequest request) {
        Message message;
        try {
            message = new Message();
            //当前登录用户
            LoginUser loginUser = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
            List<Shop> shopList = wechatAuthService.getSecretBindInfo(new WechatAuth().setWechatAuthId(loginUser.getId()));
            message.setData(shopList);
            message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
            message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
            return message;
        } catch (Exception e) {
            log.error("ordershoplist failed:", e);
            message = new Message();
            message.setState(UtilConstants.WXState.FAIL);
            message.setMessage(UtilConstants.ResponseMsg.FAILED);
            return message;
        }
    }


}
