package com.zero.egg.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.zero.egg.model.Goods;
import com.zero.egg.responseDTO.GoodsResponse;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author Hhaifeng
 * @since 2019-04-23
 */
public interface GoodsMapper extends BaseMapper<Goods> {

    List<GoodsResponse> listByCondition(@Param(Constants.WRAPPER) QueryWrapper<Goods> queryWrapper);

    /**
     * 出货时查询库存中的对应商品信息
     *
     * @param queryWrapper
     * @return
     */
    GoodsResponse queryGoodWhileShiping(@Param(Constants.WRAPPER) QueryWrapper<Goods> queryWrapper);
}
