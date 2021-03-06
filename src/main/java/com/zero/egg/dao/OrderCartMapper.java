package com.zero.egg.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zero.egg.model.OrderCart;
import com.zero.egg.model.OrderGoodsSpecification;
import com.zero.egg.responseDTO.CartGoodsResponseDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderCartMapper extends BaseMapper<OrderCart> {

    List<OrderCart> getCartList(@Param("shopId") String shopId, @Param("userId") String userId);

    List<OrderGoodsSpecification> getSpecificationList(@Param("shopId") String shopId, @Param("goodsId") String goodsId);

    OrderCart getOneCartGoodForBill(@Param("shopId") String shopId, @Param("userId") String userId, @Param("cartId") String cartId);
}
