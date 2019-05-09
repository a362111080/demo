package com.zero.egg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zero.egg.dao.TaskMapper;
import com.zero.egg.enums.TaskEnums;
import com.zero.egg.model.Bill;
import com.zero.egg.model.BillDetails;
import com.zero.egg.model.Goods;
import com.zero.egg.model.Stock;
import com.zero.egg.model.Task;
import com.zero.egg.model.UnloadGoods;
import com.zero.egg.requestDTO.TaskRequest;
import com.zero.egg.service.ITaskService;
import com.zero.egg.tool.Message;
import com.zero.egg.tool.ServiceException;
import com.zero.egg.tool.UtilConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Hhaifeng
 * @since 2019-03-25
 */
@Service
@Slf4j
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

    @Override
    public Message addShipmentTask(Task task) {

        Message message = new Message();
        try {
            /**
             * 1.根据合作商id查询是否存在有效的(正在执行或者暂停)任务
             * 2.如果存在活动任务,则创建失败,并返回已有活动任务的提示
             * 3.如果不存在活动任务,则创建任务
             */
            int count = mapper.selectCount(new QueryWrapper<Task>()
                    .eq("dr", 0)
                    .eq("shop_id", task.getShopId())
                    .eq("company_id", task.getCompanyId())
                    .eq("cussup_id", task.getCussupId())
                    .in("status", TaskEnums.Status.Execute.index().toString()
                            , TaskEnums.Status.Unexecuted.index().toString()));
            if (count > 0) {
                message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
                message.setMessage(UtilConstants.ResponseMsg.TAST_EXIST);
            } else {
                task.setCreatetime(new Date());
                task.setModifytime(new Date());
                task.setStatus(TaskEnums.Status.Execute.index().toString());
                task.setType(TaskEnums.Type.Shipment.index().toString());
                task.setDr(false);
                mapper.insert(task);
                message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
                message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
            }
            return message;
        } catch (Exception e) {
            log.error("addShipmentTask failed:" + e);
            throw new ServiceException("addShipmentTask failed");
        }
    }
}
