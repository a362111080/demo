package com.zero.egg.service.impl;

import com.zero.egg.model.*;
import com.zero.egg.dao.TaskMapper;
import com.zero.egg.requestDTO.TaskRequest;
import com.zero.egg.service.ITaskService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Hhaifeng
 * @since 2019-03-25
 */
@Service
public class TaskServiceImpl extends ServiceImpl<TaskMapper, Task> implements ITaskService {

    @Autowired
    private TaskMapper mapper;

    @Override
    public List<Task> QueryTaskList(TaskRequest task) {
        return mapper.QueryTaskList(task);
    }

    @Override
    public boolean UpdateUnloadDetl(String id) {
        return mapper.UpdateUnloadDetl(id);
    }

    @Override
    public List<UnloadGoods> GetUnloadDetl(String id) {
        return mapper.GetUnloadDetl(id);
    }

    @Override
    public boolean InsertGoods(Goods igoods) {
        return mapper.InsertGoods(igoods);
    }

    @Override
    public int IsExtis(String SpecificationId) {
        return mapper.IsExtis(SpecificationId);
    }

    @Override
    public boolean insertStock(Stock istock) {
        return mapper.insertStock(istock);
    }

    @Override
    public boolean updateStock(String specificationId) {
        return mapper.updateStock(specificationId);
}

    @Override
    public boolean insertBillDetails(BillDetails iDetails) {
        return mapper.insertBillDetails(iDetails);
    }

    @Override
    public boolean insertBill(Bill ibill) {
        return mapper.insertBill(ibill);
}

    @Override
    public int GetActiveTaskBySupplier(String supplierId) {
        return mapper.GetActiveTaskBySupplier(supplierId);
    }

    @Override
    public boolean UnloadProChange(Task task) {
        return mapper.UnloadProChange(task);
    }

    @Override
    public int IsExtisUnloadTaskProgram(Task Task) {
        return mapper.IsExtisUnloadTaskProgram(Task);
    }

    @Override
    public boolean UnloadProStop(String taskid) {
        return mapper.UnloadProStop(taskid);
    }
}
