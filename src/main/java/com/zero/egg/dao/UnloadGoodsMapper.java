package com.zero.egg.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zero.egg.model.UnloadGoods;
import com.zero.egg.responseDTO.UnLoadGoodsQueryResponseDto;
import com.zero.egg.responseDTO.UnLoadResponseDto;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

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

    List<UnLoadGoodsQueryResponseDto> QueryUnloadGood(String taskId);

    List<UnLoadGoodsQueryResponseDto> QueryUnloadGoodForDay(@Param("shopId") String shopId, @Param("unloadTime") String unloadTime);

    int GetTaskUnloadCount(String taskId);
}
