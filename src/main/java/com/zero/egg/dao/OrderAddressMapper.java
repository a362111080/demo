package com.zero.egg.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zero.egg.model.OrderAddress;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author lym
 */
@Mapper
public interface OrderAddressMapper extends BaseMapper<OrderAddress> {

    List<OrderAddress> getAddressListByUserId(@Param("userId") String userId);
}