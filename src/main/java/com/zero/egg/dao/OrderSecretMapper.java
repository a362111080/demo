package com.zero.egg.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zero.egg.model.OrderSecret;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OrderSecretMapper extends BaseMapper<OrderSecret> {

    Integer getCountOfShopBind(@Param("shopId") String shopId,@Param("userId") String userId);
}
