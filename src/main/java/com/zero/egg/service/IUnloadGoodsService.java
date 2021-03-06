package com.zero.egg.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zero.egg.model.BarCode;
import com.zero.egg.model.UnloadGoods;
import com.zero.egg.requestDTO.UnloadGoodsRequest;
import com.zero.egg.responseDTO.UnLoadGoodsQueryResponseDto;
import com.zero.egg.responseDTO.UnLoadResponseDto;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Hhaifeng
 * @since 2019-03-25
 */
public interface IUnloadGoodsService extends IService<UnloadGoods> {

    int AddUnloadDetl(UnloadGoods model);

    UnLoadResponseDto CheckWeight(BigDecimal weight, String programId);

    List<UnLoadGoodsQueryResponseDto> QueryUnloadGood(String taskId);

    List<UnLoadGoodsQueryResponseDto> QueryUnloadGoodForDay(String shopId, String unloadTime);

    int GetTaskUnloadCount(String taskId);

    List<UnloadGoods> GetUnloadList(UnloadGoodsRequest unloadGoods);

    String GetTaskStatusBySupplier(String supplierId);


    UnLoadGoodsQueryResponseDto GetTaskProgram(String taskId);

    BarCode GetBarCodeInfo(String qrCode);

    UnLoadResponseDto CheckWeightForWarning(String programId);

    int GoodNoIsExists(String currentCode);

    void RepaireUnloadTask(String taskId);

    int RemoveUnloadGood(UnloadGoods model);

    List<UnLoadGoodsQueryResponseDto> QueryUnloadGoodForTimeSpan(UnloadGoods model);
}
