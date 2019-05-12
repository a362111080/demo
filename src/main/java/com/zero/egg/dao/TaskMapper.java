package com.zero.egg.dao;

import com.zero.egg.model.*;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zero.egg.requestDTO.TaskRequest;
import com.zero.egg.responseDTO.UnloadReport;

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

    boolean updateStock(UnloadReport specificationId);

    boolean insertStock(Stock istock);

    boolean insertBillDetails(BillDetails iDetails);

    boolean insertBill(Bill ibill);

    int GetActiveTaskBySupplier(String supplierId);

    boolean UnloadProChange(Task task);

    int IsExtisUnloadTaskProgram(Task newProgrem);

    boolean UnloadProStop(String taskid);

    List<UnloadReport> GetUnloadReport(String taskid);

}
