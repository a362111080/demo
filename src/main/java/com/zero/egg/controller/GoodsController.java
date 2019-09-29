package com.zero.egg.controller;


import com.zero.egg.annotation.LoginToken;
import com.zero.egg.api.ApiConstants;
import com.zero.egg.requestDTO.LoginUser;
import com.zero.egg.tool.ImageHolder;
import com.zero.egg.tool.ImageUtil;
import com.zero.egg.tool.Message;
import com.zero.egg.tool.PathUtil;
import com.zero.egg.tool.ServiceException;
import com.zero.egg.tool.UtilConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Hhaifeng
 * @since 2019-04-23
 */
@RestController
@RequestMapping("/goods")
@Slf4j
public class GoodsController {

    @RequestMapping(value = "/uploadpic", method = RequestMethod.POST)
    @ResponseBody
    @LoginToken
    public Message uploadpic(HttpServletRequest request) {
        LoginUser user = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
        Message message = new Message();
        ImageHolder thumbnail = null;
        String thumbnailAddr;
        //从session中获取servletContext,相当于tomcat容器了,然后转换成spring的CommonsMultipartResolver
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        try {
            //如果CommonsMultipartResolver里面有文件
            if (commonsMultipartResolver.isMultipart(request)) {
                //将servlet中的request转换成spring中的MultipartHttpServletRequest(spring)
                MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
                //取出缩略图并构建ImageHolder对象,从MultipartHttpServletRequest中
                CommonsMultipartFile thumbnailFile = (CommonsMultipartFile) multipartHttpServletRequest.getFile("thumbnail");
                if (thumbnailFile != null) {
                    thumbnail = new ImageHolder(thumbnailFile.getOriginalFilename(), thumbnailFile.getInputStream());
                }
                //如果商品缩略图不为null,则添加
                if (thumbnail != null && null != thumbnail.getImage()) {
                    thumbnailAddr = addThumbnail(user, thumbnail);
                    message.setData(thumbnailAddr);
                }
                message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
                message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
            }
        } catch (Exception e) {
            log.error("uploadpic" + e);
            throw new ServiceException("uploadpic exception");
        }
        return message;
    }

    /**
     * 生成缩略图
     *
     * @param loginUser     当前登录用户
     * @param imageHolder 图片处理的封装类
     */
    private String addThumbnail(LoginUser loginUser, ImageHolder imageHolder) {
        String dest = PathUtil.getShopImagePath(loginUser.getShopId());
        String thumbnailAddr = ImageUtil.generateThumbnal(imageHolder, dest);
        return thumbnailAddr;
    }


}
