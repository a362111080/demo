package com.zero.egg.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zero.egg.model.OrderBill;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author lym
 */
@Mapper
public interface OrderBillMapper extends BaseMapper<OrderBill> {

    IPage<OrderBill> selectBillList(Page<OrderBill> page, @Param(Constants.WRAPPER) QueryWrapper<OrderBill> queryWrapper);
}
