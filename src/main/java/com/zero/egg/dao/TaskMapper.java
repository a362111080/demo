package com.zero.egg.dao;

import com.zero.egg.model.Goods;
import com.zero.egg.model.Stock;
import com.zero.egg.model.Task;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zero.egg.model.UnloadGoods;
import com.zero.egg.requestDTO.TaskRequest;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Hhaifeng
 * @since 2019-03-25
 */
public interface TaskMapper extends BaseMapper<Task> {

    List<Task> QueryTaskList(TaskRequest task);

    boolean UpdateUnloadDetl(String id);

    List<UnloadGoods> GetUnloadDetl(String id);

    boolean InsertGoods(Goods igoods);

    int IsExtis(String specificationId);

    boolean updateStock(String specificationId);

    boolean insertStock(Stock istock);

}
