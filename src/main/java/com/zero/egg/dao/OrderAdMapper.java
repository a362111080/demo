package com.zero.egg.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zero.egg.model.OrderAd;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author lym
 */
@Mapper
public interface OrderAdMapper extends BaseMapper<OrderAd> {
    int addorderad(OrderAd model);

    int editorderad(OrderAd model);
}