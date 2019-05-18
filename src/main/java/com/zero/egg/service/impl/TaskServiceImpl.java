package com.zero.egg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zero.egg.cache.JedisUtil;
import com.zero.egg.dao.BillDetailsMapper;
import com.zero.egg.dao.BillMapper;
import com.zero.egg.dao.CustomerMapper;
import com.zero.egg.dao.GoodsMapper;
import com.zero.egg.dao.ShipmentGoodsMapper;
import com.zero.egg.dao.StockMapper;
import com.zero.egg.dao.TaskMapper;
import com.zero.egg.enums.BillEnums;
import com.zero.egg.enums.TaskEnums;
import com.zero.egg.model.Bill;
import com.zero.egg.model.BillDetails;
import com.zero.egg.model.Customer;
import com.zero.egg.model.Goods;
import com.zero.egg.model.ShipmentGoods;
import com.zero.egg.model.Stock;
import com.zero.egg.model.Task;
import com.zero.egg.model.UnloadGoods;
import com.zero.egg.requestDTO.TaskRequest;
import com.zero.egg.responseDTO.GoodsResponse;
import com.zero.egg.responseDTO.UnloadReport;
import com.zero.egg.service.ITaskService;
import com.zero.egg.tool.JsonUtils;
import com.zero.egg.tool.Message;
import com.zero.egg.tool.ServiceException;
import com.zero.egg.tool.UtilConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.DecimalFormat;
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

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private BillMapper billMapper;

    @Autowired
    private BillDetailsMapper billDetailsMapper;

    @Autowired
    private JedisUtil.Strings jedisStrings;

    @Autowired
    private JedisUtil.Keys jedisKeys;

    @Autowired
    private StockMapper stockMapper;

    @Autowired
    private ShipmentGoodsMapper shipmentGoodsMapper;

    @Autowired
    private GoodsMapper goodsMapper;


    @Override
    public List<Task> QueryTaskList(TaskRequest task) {
        if (task.getType().equals(TaskEnums.Type.Shipment.index().toString())) {
            return mapper.QueryShipmentTaskList(task);
        } else {
            return mapper.QueryTaskList(task);
        }
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
    public boolean updateStock(UnloadReport unloadReport) {
        return mapper.updateStock(unloadReport);
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
    @Transactional
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
                String taskId = task.getId();
                String customerName = customerMapper.selectOne(new QueryWrapper<Customer>()
                        .select("name")
                        .eq("id", task.getCussupId()))
                        .getName();
                task.setCussupName(customerName);
                /**
                 * 把任务状态存入redis,用作出货前判断是否还能出货
                 */
                jedisStrings.set(UtilConstants.RedisPrefix.SHIPMENTGOOD_TASK + task.getCompanyId() + task.getShopId()
                                + task.getCussupId() + taskId + "status"
                        , TaskEnums.Status.Execute.index().toString());
                message.setData(task);
                message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
                message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
            }
            return message;
        } catch (Exception e) {
            log.error("addShipmentTask failed:" + e);
            throw new ServiceException("addShipmentTask failed");
        }
    }

    @Override
    @Transactional
    public Message cancelShipmentTask(Task task, String taskId, String customerId) throws ServiceException {
        Message message = new Message();
        try {
            /**
             * 1.在MySQL中对应的出货任务,status改为TaskEnums.Status.CANCELED.index().toString(),dr改为1(true)
             * 2.把key为UtilConstants.RedisPrefix.SHIPMENTGOOD_TASK + task.getCompanyId() + task.getShopId() + customerId + taskId +"status"
             *    的Redis数据改为TaskEnums.Status.CANCELED.index().toString()
             */
//            jedisKeys.del(UtilConstants.RedisPrefix.SHIPMENTGOOD_TASK + task.getCompanyId() + task.getShopId() + customerId + taskId);
//            jedisStrings.set(UtilConstants.RedisPrefix.SHIPMENTGOOD_TASK + task.getCompanyId() + task.getShopId() + customerId + taskId + "status", TaskEnums.Status.CANCELED.index().toString());
//            task.setDr(true);
            task.setModifytime(new Date());
            task.setStatus(TaskEnums.Status.CANCELED.index().toString());
            mapper.update(task, new UpdateWrapper<Task>()
                    .eq("id", taskId)
                    .eq("shop_id", task.getShopId())
                    .eq("company_id", task.getCompanyId())
                    .eq("cussup_id", customerId)
                    .eq("dr", 0));
            jedisStrings.set(UtilConstants.RedisPrefix.SHIPMENTGOOD_TASK + task.getCompanyId() + task.getShopId() + customerId + taskId + "status", TaskEnums.Status.CANCELED.index().toString());
            message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
            message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
        } catch (Exception e) {
            log.error("cancelShipmentTask failed:" + e);
            throw new ServiceException("cancelShipmentTask failed");
        }

        return message;
    }

    @Override
    @Transactional
    public Message emplyeeFinishTask(Task task, String taskId, String customerId) {
        Message message = new Message();
        try {
            /**
             * 1.在MySQL中对应的出货任务,status改为TaskEnums.Status.Unexecuted.index().toString()
             * 2.把key为UtilConstants.RedisPrefix.SHIPMENTGOOD_TASK + task.getCompanyId() + task.getShopId() + customerId + taskId +"status"
             *    的Redis数据改为TaskEnums.Status.Unexecuted.index().toString()
             */
            task.setStatus(TaskEnums.Status.Unexecuted.index().toString());
            task.setModifytime(new Date());
            mapper.update(task, new UpdateWrapper<Task>()
                    .eq("id", taskId)
                    .eq("shop_id", task.getShopId())
                    .eq("company_id", task.getCompanyId())
                    .eq("cussup_id", customerId)
                    .eq("dr", 0));
            jedisStrings.set(UtilConstants.RedisPrefix.SHIPMENTGOOD_TASK + task.getCompanyId() + task.getShopId() + customerId + taskId + "status", TaskEnums.Status.Unexecuted.index().toString());
            message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
            message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
        } catch (Exception e) {
            log.error("emplyeeFinishTask failed:" + e);
            throw new ServiceException("emplyeeFinishTask failed");
        }

        return message;
    }

    @Override
    @Transactional
    public Message RealFinishTask(Task task, String taskId, String customerId) throws ServiceException {
        Message message = new Message();
        try {
            /**
             * 1.把key为UtilConstants.RedisPrefix.SHIPMENTGOOD_TASK + task.getCompanyId() + task.getShopId() + customerId + taskId +"status"
             *    的Redis数据改为TaskEnums.Status.Finish.index().toString()   (放到最后一步,防止不回滚)
             * 2.把key为UtilConstants.RedisPrefix.SHIPMENTGOOD_TASK + task.getCompanyId() + task.getShopId() + customerId + taskId的数据取出来转换回列表
             * 3.统计里面的数据存入出货商品表同时减去库存中的数量
             * 4.在MySQL中对应的出货任务,status改为TaskEnums.Status.Finish.index().toString(),dr设为1(true)
             * 5,生成一条账单信息
             */
            String goodsJson = jedisStrings.get(UtilConstants.RedisPrefix.SHIPMENTGOOD_TASK + task.getCompanyId() + task.getShopId() + customerId + taskId);
            List<GoodsResponse> goodsResponseList = JsonUtils.jsonToList(goodsJson, GoodsResponse.class);
            if (null == goodsResponseList || goodsResponseList.size() == 0) {
                message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
                message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
                return message;
            }
            ShipmentGoods shipmentGoods = null;
            Goods goods = null;
            Stock stock = null;
            BigDecimal quantity;
            BigDecimal subOne = new BigDecimal(BigDecimal.ROUND_DOWN);
            for (GoodsResponse goodsResponse : goodsResponseList) {
                shipmentGoods = new ShipmentGoods();
                shipmentGoods.setCompanyId(task.getCompanyId());
                shipmentGoods.setShopId(task.getShopId());
                shipmentGoods.setCustomerId(customerId);
                shipmentGoods.setTaskId(taskId);
                shipmentGoods.setSpecificationId(goodsResponse.getSpecificationId());
                shipmentGoods.setGoodsCategoryId(goodsResponse.getGoodsCategoryId());
                shipmentGoods.setGoodsNo(goodsResponse.getGoodsNo());
                shipmentGoods.setMode(goodsResponse.getMode());
                shipmentGoods.setMarker(goodsResponse.getMarker());
                shipmentGoods.setWeight(goodsResponse.getWeight());
                shipmentGoods.setRemark(goodsResponse.getRemark());
                shipmentGoods.setCreatetime(new Date());
                shipmentGoods.setModifytime(new Date());
                shipmentGoods.setModifier(task.getCreator());
                shipmentGoods.setCreator(task.getCreator());
                shipmentGoods.setDr(false);
                /**
                 * 1.新增到出货商品表里
                 * 2.从商品表里删除
                 * 3.从库存里对应规格数量-1
                 * 4.//TODO 统计员工工作量
                 */
                shipmentGoodsMapper.insert(shipmentGoods);
                goods = new Goods();
                goods.setDr(true);
                goodsMapper.update(goods, new UpdateWrapper<Goods>()
                        .eq("goods_no", goodsResponse.getGoodsNo())
                        .eq("company_id", task.getCompanyId())
                        .eq("shop_id", task.getShopId())
                );
                stock = stockMapper.selectOne(new QueryWrapper<Stock>().select("id", "quantity")
                        .eq("specification_id", goodsResponse.getSpecificationId())
                        .eq("company_id", task.getCompanyId())
                        .eq("shop_id", task.getShopId())
                );
                quantity = stock.getQuantity().subtract(subOne);
                stock.setQuantity(quantity);
                stockMapper.updateById(stock);

            }
            //4.在MySQL中对应的出货任务,status改为TaskEnums.Status.Finish.index().toString(),dr设为1(true)
            task.setStatus(TaskEnums.Status.Finish.index().toString());
            task.setModifytime(new Date());
            mapper.update(task, new UpdateWrapper<Task>()
                    .eq("id", taskId)
                    .eq("shop_id", task.getShopId())
                    .eq("company_id", task.getCompanyId())
                    .eq("cussup_id", customerId)
                    .eq("dr", 0));
            //5.生成一条ststus为0(未生成的)账单
            int shipmentBillCount = billMapper.selectCount(new QueryWrapper<Bill>()
                    .eq("type", TaskEnums.Type.Shipment.index().toString()));
            /**8位编码前面补0格式*/
            DecimalFormat g1 = new DecimalFormat("00000000");
            Bill bill = new Bill();
            bill.setBillNo("BLS" + (g1.format(shipmentBillCount)));
            bill.setTaskId(taskId);
            bill.setCussupId(customerId);
            bill.setStatus(BillEnums.Status.Not_Generated.index().toString());
            bill.setType(TaskEnums.Type.Shipment.index().toString());
            bill.setCompanyId(task.getCompanyId());
            bill.setShopId(task.getShopId());
            bill.setBillDate(new Date());
            bill.setCreator(task.getCreator());
            bill.setCreatetime(new Date());
            billMapper.insert(bill);
            String billId = bill.getId();
            jedisStrings.set(UtilConstants.RedisPrefix.SHIPMENTGOOD_TASK + task.getCompanyId() + task.getShopId() + customerId + taskId + "status", TaskEnums.Status.Finish.index().toString());
            message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
            message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
            return message;
        } catch (Exception e) {
            log.error("RealFinishTask failed:" + e);
            throw new ServiceException("RealFinishTask failed");
        }
    }

    @Override
    public List<UnloadReport> GetUnloadReport(String taskid) {
        return mapper.GetUnloadReport(taskid);
    }


}
