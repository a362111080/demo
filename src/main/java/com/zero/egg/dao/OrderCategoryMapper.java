package com.zero.egg.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zero.egg.model.OrderCategory;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderCategoryMapper extends BaseMapper<OrderCategory> {
}