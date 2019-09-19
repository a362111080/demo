package com.zero.egg.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zero.egg.model.WechatAuth;
import org.apache.ibatis.annotations.Param;

/*
 * 微信账号dao接口
 * @Author lyming
 * @Date 2019/4/25 15:46
 **/
public interface WechatAuthMapper extends BaseMapper<WechatAuth> {
    /**
     * 取消绑定
     * @param openId
     * @return
     */
    int cancelBind(@Param("openId") String openId);
}
