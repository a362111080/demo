package com.zero.egg.service;

import com.zero.egg.model.Goods;
import com.zero.egg.model.Stock;
import com.zero.egg.model.Task;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zero.egg.model.UnloadGoods;
import com.zero.egg.requestDTO.TaskRequest;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Hhaifeng
 * @since 2019-03-25
 */
public interface ITaskService extends IService<Task> {

    List<Task> QueryTaskList(TaskRequest task);

    boolean UpdateUnloadDetl(String id);

    List<UnloadGoods> GetUnloadDetl(String id);

    boolean InsertGoods(Goods igoods);

    int IsExtis(String SpecificationId);

    boolean insertStock(Stock istock);

    boolean updateStock(String specificationId);
}
