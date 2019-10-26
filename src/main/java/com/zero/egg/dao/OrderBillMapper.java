package com.zero.egg.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zero.egg.model.OrderBill;
import com.zero.egg.model.OrderBillDetail;
import com.zero.egg.requestDTO.OrderGoodsRequestDTO;
import com.zero.egg.responseDTO.OrderBillDetailResponseDTO;
import com.zero.egg.responseDTO.OrderBillListResponseDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author lym
 */
@Mapper
public interface OrderBillMapper extends BaseMapper<OrderBill> {

    IPage<OrderBillListResponseDTO> selectBillList(Page<OrderBill> page, @Param(Constants.WRAPPER) QueryWrapper<OrderBill> queryWrapper);

    List<String> selectBillListPics(@Param("userId") String userId,@Param("orderId") String orderId);

    OrderBillDetailResponseDTO getOrderBillDetail(@Param("userId") String userId,@Param("orderId") String orderId);

    List<OrderBill> queryshoporder(OrderGoodsRequestDTO model);

    List<OrderBillDetail> GetOrderGoodDelList(OrderBill orderBill);
}
