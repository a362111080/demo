package com.zero.egg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zero.egg.cache.JedisUtil;
import com.zero.egg.dao.BillDetailsMapper;
import com.zero.egg.dao.BillMapper;
import com.zero.egg.dao.CategoryMapper;
import com.zero.egg.dao.CustomerMapper;
import com.zero.egg.dao.GoodsMapper;
import com.zero.egg.dao.ShipmentGoodsMapper;
import com.zero.egg.dao.SpecificationMapper;
import com.zero.egg.dao.StockMapper;
import com.zero.egg.dao.TaskMapper;
import com.zero.egg.enums.BillEnums;
import com.zero.egg.enums.TaskEnums;
import com.zero.egg.model.Bill;
import com.zero.egg.model.BillDetails;
import com.zero.egg.model.Category;
import com.zero.egg.model.Customer;
import com.zero.egg.model.Goods;
import com.zero.egg.model.ShipmentGoods;
import com.zero.egg.model.Specification;
import com.zero.egg.model.Stock;
import com.zero.egg.model.Task;
import com.zero.egg.model.UnloadGoods;
import com.zero.egg.requestDTO.QueryBlankBillGoodsRequestDTO;
import com.zero.egg.requestDTO.TaskRequest;
import com.zero.egg.responseDTO.BlankBillDTO;
import com.zero.egg.responseDTO.BlankBillResponseDTO;
import com.zero.egg.responseDTO.GoodsResponse;
import com.zero.egg.responseDTO.NewShipmentTaskResponseDTO;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private SpecificationMapper specificationMapper;


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
            Task tempTask = mapper.selectOne(new QueryWrapper<Task>()
                    .eq("dr", 0)
                    .eq("shop_id", task.getShopId())
                    .eq("company_id", task.getCompanyId())
                    .eq("cussup_id", task.getCussupId())
                    .in("status", TaskEnums.Status.Execute.index().toString()
                            , TaskEnums.Status.Unexecuted.index().toString()));
            if (null != tempTask) {
                message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
                message.setMessage(UtilConstants.ResponseMsg.TAST_EXIST);
                message.setData(tempTask);
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
            String goodsJson = jedisStrings.get(UtilConstants.RedisPrefix.SHIPMENTGOOD_TASK + task.getCompanyId() + task.getShopId() + customerId + taskId);

            /**
             * 如果出货列表为空,返回前端禁止完成任务的提示,并提示它可以取消任务,避免生成冗杂账单
             */
            if (null == goodsJson || "".equals(goodsJson)) {
                message.setState(UtilConstants.ResponseCode.SHIPMENTGOODS_NULL);
                message.setMessage(UtilConstants.ResponseMsg.NULL_SHIPMENTGOODS);
                return message;
            }
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

            /**
             * 如果出货列表为空,返回前端禁止完成任务的提示,并提示它可以取消任务,避免生成冗杂账单
             */
            if (null == goodsJson || "".equals(goodsJson)) {
                message.setState(UtilConstants.ResponseCode.SHIPMENTGOODS_NULL);
                message.setMessage(UtilConstants.ResponseMsg.NULL_SHIPMENTGOODS);
                return message;
            }
            List<GoodsResponse> goodsResponseList = JsonUtils.jsonToList(goodsJson, GoodsResponse.class);
            ShipmentGoods shipmentGoods = null;
            Goods goods = null;
            Stock stock = null;
            BigDecimal quantity;
            BigDecimal subOne = new BigDecimal(BigDecimal.ROUND_DOWN);
            Set<String> categoryNameSet = new HashSet<>();
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
                categoryNameSet.add(goodsResponse.getCategoryName());
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
            String categoryName = String.join("/", categoryNameSet);
            DecimalFormat g1 = new DecimalFormat("00000000");
            Bill bill = new Bill();
            bill.setBillNo("BLS" + (g1.format(shipmentBillCount)));
            bill.setCategoryname(categoryName);
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
            NewShipmentTaskResponseDTO responseDTO = new NewShipmentTaskResponseDTO();
            responseDTO.setBillId(billId);
            message.setData(responseDTO);
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

    @Override
    @Transactional
    public Message queryBlankGoods(QueryBlankBillGoodsRequestDTO requestDTO) {
        Message message = new Message();
        try {
            String customerId = mapper.selectOne(new QueryWrapper<Task>()
                    .select("cussup_id")
                    .eq("id", requestDTO.getTaskId())
                    .eq("shop_id", requestDTO.getShopId())
                    .eq("company_id", requestDTO.getCompanyId()))
                    .getCussupId();
            String customerName = customerMapper.selectOne(new QueryWrapper<Customer>().select("name")
                    .eq("id", customerId)
                    .eq("shop_id", requestDTO.getShopId())
                    .eq("company_id", requestDTO.getCompanyId())
                    .eq("dr", 0))
                    .getName();
            Bill bill = billMapper.selectOne(new QueryWrapper<Bill>().select("id,bill_no")
                    .eq("task_id", requestDTO.getTaskId())
                    .eq("shop_id", requestDTO.getShopId())
                    .eq("company_id", requestDTO.getCompanyId()));
            //1.根据任务id等信息,查询出货任务出的所有货物信息
            List<ShipmentGoods> goodsList = shipmentGoodsMapper.selectList(new QueryWrapper<ShipmentGoods>()
                    .eq("task_id", requestDTO.getTaskId())
                    .eq("shop_id", requestDTO.getShopId())
                    .eq("company_id", requestDTO.getCompanyId())
                    .eq("dr", 0));
            //2.将货物信息根据 品种 分类  品种id-->货物集合
            Map<String, List<ShipmentGoods>> categoryGoodsList = new HashMap<>();
            for (ShipmentGoods shipmentGoods : goodsList) {
                String categoryId = shipmentGoods.getGoodsCategoryId();
                if (categoryGoodsList.keySet().contains(categoryId)) {
                    categoryGoodsList.get(categoryId).add(shipmentGoods);
                } else {
                    List<ShipmentGoods> tempShipmentList = new ArrayList<>();
                    tempShipmentList.add(shipmentGoods);
                    categoryGoodsList.put(categoryId, tempShipmentList);
                }
            }
            //3. 再把货物集合根据标记方式分类   标记方式id-->货物集合
            Map<String, List<ShipmentGoods>> specificationListMap = new HashMap<>();
            for (List<ShipmentGoods> shipmentGoodsList : categoryGoodsList.values()) {
                for (ShipmentGoods shipmentGoods : shipmentGoodsList) {
                    String specificationId = shipmentGoods.getSpecificationId();
                    if (specificationListMap.keySet().contains(specificationId)) {
                        specificationListMap.get(specificationId).add(shipmentGoods);
                    } else {
                        List<ShipmentGoods> tempShipmentList = new ArrayList<>();
                        tempShipmentList.add(shipmentGoods);
                        specificationListMap.put(specificationId, tempShipmentList);
                    }
                }
            }
            //4 开始整理BlankBillGoodsDetail
            Specification specification;
            BlankBillResponseDTO blankBillResponseDTO = new BlankBillResponseDTO();
            BlankBillDTO blankBillDTO;
            List<BlankBillDTO> blankBillDTOList = new ArrayList<>();
            for (Map.Entry<String, List<ShipmentGoods>> entryOut : categoryGoodsList.entrySet()) {
                String categoryName = categoryMapper.selectOne(new QueryWrapper<Category>().select("name")
                        .eq("id", entryOut.getKey())
                        .eq("shop_id", requestDTO.getShopId())
                        .eq("company_id", requestDTO.getCompanyId())
                        .eq("dr", 0))
                        .getName();
                for (Map.Entry<String, List<ShipmentGoods>> entryIn : specificationListMap.entrySet()) {
                    //如果内循环的品种id和外循环的品种id一致,才进行归类操作
                    if ((entryIn.getValue().get(0).getGoodsCategoryId()).equals(entryOut.getKey())) {

                        blankBillDTO = new BlankBillDTO();
                        blankBillDTO.setBillId(bill.getId());
                        blankBillDTO.setBillNo(bill.getBillNo());
                        blankBillDTO.setCustomerId(customerId);
                        blankBillDTO.setCustomerName(customerName);
                        blankBillDTO.setCategoryId(entryOut.getKey());
                        blankBillDTO.setCategoryName(categoryName);
                        blankBillDTO.setQuantity((long) entryIn.getValue().size());
                        blankBillDTO.setMode(Integer.parseInt(entryIn.getValue().get(0).getMode()));

                        BigDecimal totalWeight = BigDecimal.ZERO;
                        String specificationId = entryIn.getKey();
                        blankBillDTO.setSpecificationId(specificationId);

                        specification = specificationMapper.selectOne(new QueryWrapper<Specification>()
                                .eq("id", specificationId)
                                .eq("shop_id", requestDTO.getShopId())
                                .eq("company_id", requestDTO.getCompanyId())
                                .eq("dr", 0));
                        blankBillDTO.setProgramId(specification.getProgramId());

                        //如果mode为1(去皮),则需要做额外处理
                        if (blankBillDTO.getMode() == 1) {
                            //循环出货商品,累加重量
                            for (ShipmentGoods shipmentGoods : entryIn.getValue()) {
                                totalWeight = totalWeight.add(shipmentGoods.getWeight());
                                //需要减去去皮值*数量
                                BigDecimal needToSubtract = new BigDecimal(specification.getNumerical() * blankBillDTO.getQuantity());
                                totalWeight = totalWeight.subtract(needToSubtract);
                            }
                            blankBillDTO.setMarker("实重(" + specification.getWeightMin() + "~" + specification.getWeightMax() + ")");
                            blankBillDTO.setTotalWeight(totalWeight);

                        } else {
                            blankBillDTO.setMarker(specification.getMarker());
                            blankBillDTO.setTotalWeight(BigDecimal.ZERO);
                        }
                        blankBillDTOList.add(blankBillDTO);
                        blankBillDTO = null;
                    }
                }
            }

            blankBillResponseDTO.setBlankBillDTOList(blankBillDTOList);

            message.setData(blankBillResponseDTO);
            message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
            message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
        } catch (Exception e) {
            log.error("queryBlankGoods failed:" + e);
            throw new ServiceException("queryBlankGoods failed");
        }

        return message;
    }


}
