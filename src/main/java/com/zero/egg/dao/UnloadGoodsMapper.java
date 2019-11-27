package com.zero.egg.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zero.egg.model.BarCode;
import com.zero.egg.model.Specification;
import com.zero.egg.model.Task;
import com.zero.egg.model.UnloadGoods;
import com.zero.egg.requestDTO.UnloadGoodsRequest;
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

    List<UnloadGoods> GetUnloadList(UnloadGoodsRequest unloadGoods);

    String GetTaskStatusBySupplier(String supplierId);

    UnLoadGoodsQueryResponseDto GetTaskProgram(String taskId);

    BarCode GetBarCodeInfo(String qrCode);

    UnLoadResponseDto CheckWeightForWarning(String programId);

    int GoodNoIsExists(String currentCode);

    void RepaireUnloadTask(String taskId);

    Task GetTaskinfo(String taskId);

    int RemoveUnloadGood(UnloadGoods model);

    List<UnLoadGoodsQueryResponseDto> QueryUnloadGoodForTimeSpan(UnloadGoods model);

    int SpecificationIsUsed(Specification specification);
}
