package com.zero.egg.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zero.egg.model.OrderCart;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderCartMapper extends BaseMapper<OrderCart> {
}