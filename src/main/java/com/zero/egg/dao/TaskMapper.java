package com.zero.egg.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zero.egg.model.Bill;
import com.zero.egg.model.BillDetails;
import com.zero.egg.model.Goods;
import com.zero.egg.model.Stock;
import com.zero.egg.model.Task;
import com.zero.egg.model.UnloadGoods;
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

    /**
     * 查正在进行的任务和员工停止的(暂停状态)的出货任务列表
     *
     * @param task
     * @return
     */
    List<Task> QueryShipmentTaskList(TaskRequest task);

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
