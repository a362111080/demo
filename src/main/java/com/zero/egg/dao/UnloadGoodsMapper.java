package com.zero.egg.dao;

import com.zero.egg.model.UnloadGoods;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zero.egg.responseDTO.UnLoadResponseDto;

import java.math.BigDecimal;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Hhaifeng
 * @since 2019-03-25
 */
public interface UnloadGoodsMapper extends BaseMapper<UnloadGoods> {

    int AddUnloadDetl(UnloadGoods model);

    UnLoadResponseDto CheckWeight(BigDecimal weight, String programId);
}
