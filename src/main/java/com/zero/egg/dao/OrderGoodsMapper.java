package com.zero.egg.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zero.egg.model.OrderGoods;
import com.zero.egg.requestDTO.OrderGoodsRequestDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author lym
 */
@Mapper
public interface OrderGoodsMapper extends BaseMapper<OrderGoods> {

    List<OrderGoods> getAllByShopId(OrderGoodsRequestDTO model);

    List<OrderGoods> getAllByShopIdAndCategoryId(OrderGoodsRequestDTO model);
}
