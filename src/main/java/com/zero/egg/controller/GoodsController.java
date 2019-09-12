package com.zero.egg.controller;


import com.zero.egg.tool.ImageHolder;
import com.zero.egg.tool.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

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
    public Map<String, Object> uploadpic(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        ImageHolder thumbnail = null;
        //从session中获取servletContext,相当于tomcat容器了,然后转换成spring的CommonsMultipartResolver
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        try {
            //如果CommonsMultipartResolver里面有文件
            if (commonsMultipartResolver.isMultipart(request)) {
                //将servlet中的request转换成spring中的MultipartHttpServletRequest(spring)
                MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
                //取出缩略图并构建ImageHolder对象,从MultipartHttpServletRequest中
                CommonsMultipartFile thumbnailFile = (CommonsMultipartFile) multipartHttpServletRequest.getFile("thumbnail");

            }
        } catch (Exception e) {
            log.error("uploadpic" + e);
            throw new ServiceException("uploadpic exception");
        }
        return null;
    }


}
