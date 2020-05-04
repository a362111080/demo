package com.zero.egg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zero.egg.cache.JedisUtil;
import com.zero.egg.dao.BillDetailsMapper;
import com.zero.egg.dao.BillMapper;
import com.zero.egg.dao.BrokenGoodsMapper;
import com.zero.egg.dao.CategoryMapper;
import com.zero.egg.dao.CustomerMapper;
import com.zero.egg.dao.GoodsMapper;
import com.zero.egg.dao.OrderBillMapper;
import com.zero.egg.dao.ShipmentGoodsMapper;
import com.zero.egg.dao.SpecificationMapper;
import com.zero.egg.dao.StockMapper;
import com.zero.egg.dao.TaskMapper;
import com.zero.egg.enums.BillEnums;
import com.zero.egg.enums.TaskEnums;
import com.zero.egg.model.Bill;
import com.zero.egg.model.BillDetails;
import com.zero.egg.model.BrokenGoods;
import com.zero.egg.model.Category;
import com.zero.egg.model.Customer;
import com.zero.egg.model.Goods;
import com.zero.egg.model.OrderBill;
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
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    private JedisUtil.SortSets sortSets;

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

    @Autowired
    private BrokenGoodsMapper brokenGoodsMapper;

    @Autowired
    private OrderBillMapper orderBillMapper;


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
            if (null != tempTask && null == tempTask.getOrderId()) {
                message.setState(UtilConstants.ResponseCode.NORMAL_TAST_EXIST);
                message.setMessage(UtilConstants.ResponseMsg.TAST_EXIST);
                message.setData(tempTask);
            } else if (null != tempTask && null != tempTask.getOrderId()) {
                message.setState(UtilConstants.ResponseCode.ORDER_TAST_EXIST);
                message.setMessage(UtilConstants.ResponseMsg.ORDER_TAST_EXIST);
                message.setData(tempTask);
            } else {
                //不允许为零售商创建出货称重的任务
                if (task.getIsWeight() == 1) {
                    Customer customer = customerMapper.selectOne(new QueryWrapper<Customer>()
                            .select("is_retail")
                            .eq("dr", 0)
                            .eq("shop_id", task.getShopId())
                            .eq("company_id", task.getCompanyId())
                            .eq("id", task.getCussupId()));
                    if (null == customer) {
                        throw new ServiceException("合作商不存在或已经被删除！");
                    } else if (null != customer & customer.getIsRetail() == 1) {
                        throw new ServiceException("零售商不能创建出货称重任务！");
                    }

                }
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
                jedisStrings.set(UtilConstants.RedisPrefix.SHIPMENTGOOD_TASK + task.getCompanyId() + ":" + task.getShopId()
                                + ":" + task.getCussupId() + ":" + taskId + ":" + "status"
                        , TaskEnums.Status.Execute.index().toString());
                message.setData(task);
                message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
                message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
            }
            return message;
        } catch (Exception e) {
            log.error("addShipmentTask failed:" + e);
            if (e instanceof ServiceException) {
                throw e;
            }
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
            task.setModifytime(new Date());
            task.setStatus(TaskEnums.Status.CANCELED.index().toString());
            mapper.update(task, new UpdateWrapper<Task>()
                    .eq("id", taskId)
                    .eq("shop_id", task.getShopId())
                    .eq("company_id", task.getCompanyId())
                    .eq("cussup_id", customerId)
                    .eq("dr", 0));
            shipmentGoodsMapper.update(new ShipmentGoods().setDr(true), new UpdateWrapper<ShipmentGoods>()
                    .eq("shop_id", task.getShopId())
                    .eq("company_id", task.getCompanyId())
                    .eq("customer_id", customerId)
                    .eq("task_id", taskId));
            jedisStrings.set(UtilConstants.RedisPrefix.SHIPMENTGOOD_TASK + task.getCompanyId() + ":" + task.getShopId()
                    + ":" + task.getCussupId() + ":" + taskId + ":" + "status", TaskEnums.Status.CANCELED.index().toString());
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
            //出货列表数量
            long size = sortSets.zcard(UtilConstants.RedisPrefix.SHIPMENTGOOD_TASK
                    + task.getCompanyId() + ":" + task.getShopId() + ":" + customerId + ":" + taskId);

            /**
             * 如果出货列表为空,返回前端禁止完成任务的提示,并提示它可以取消任务,避免生成冗杂账单
             */
            if (size == 0) {
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
            jedisStrings.set(UtilConstants.RedisPrefix.SHIPMENTGOOD_TASK + task.getCompanyId() + ":" + task.getShopId()
                    + ":" + task.getCussupId() + ":" + taskId + ":" + "status", TaskEnums.Status.Unexecuted.index().toString());
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
            //出货列表数量
            long size = sortSets.zcard(UtilConstants.RedisPrefix.SHIPMENTGOOD_TASK
                    + task.getCompanyId() + ":" + task.getShopId() + ":" + customerId + ":" + taskId);

            /**
             * 如果出货列表为空,返回前端禁止完成任务的提示,并提示它可以取消任务,避免生成冗杂账单
             */
            if (size == 0) {
                message.setState(UtilConstants.ResponseCode.SHIPMENTGOODS_NULL);
                message.setMessage(UtilConstants.ResponseMsg.NULL_SHIPMENTGOODS);
                return message;
            }
            Set<String> goodsSet = sortSets.zrevrange(UtilConstants.RedisPrefix.SHIPMENTGOOD_TASK
                            + task.getCompanyId() + ":" + task.getShopId() + ":" + customerId + ":" + taskId
                    , 0, -1);
            List<GoodsResponse> goodsResponseList = new ArrayList<>();
            GoodsResponse redisGood;
            for (String jsonString : goodsSet) {
                redisGood = JsonUtils.jsonToPojo(jsonString, GoodsResponse.class);
                goodsResponseList.add(redisGood);
            }
            //商品编号集合
            Set<String> goodsNoSet = new HashSet<>();
            for (GoodsResponse goodsResponse : goodsResponseList) {
                goodsNoSet.add(goodsResponse.getGoodsNo());
            }
            //验证每箱货是否已经被报损,如果是,则要返回这些已经报损的货物编号,提示删除后再结束任务
            //坏掉的货物商品编号
            List<String> brokenGoods = brokenGoodsMapper.selectList(new QueryWrapper<BrokenGoods>()
                    .eq("company_id", task.getCompanyId())
                    .eq("shop_id", task.getShopId())
                    .eq("dr", false)
                    .in("goods_no", goodsNoSet))
                    .stream()
                    .map(v -> v.getGoodsNo())
                    .collect(Collectors.toList());
            brokenGoods.removeAll(Collections.singleton(null));
            //需要被换的货物商品编号
            List<String> chageGoods = brokenGoodsMapper.selectList(new QueryWrapper<BrokenGoods>()
                    .eq("company_id", task.getCompanyId())
                    .eq("shop_id", task.getShopId())
                    .eq("dr", false)
                    .in("change_goods_no", goodsNoSet))
                    .stream()
                    .map(v -> v.getChangeGoodsNo())
                    .collect(Collectors.toList());
            chageGoods.removeAll(Collections.singleton(null));
            List<String> collect = Stream.of(brokenGoods, chageGoods)
                    .flatMap(Collection::stream)
                    .distinct()
                    .collect(Collectors.toList());
            if (collect.size() > 0) {
                message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
                message.setMessage(UtilConstants.ResponseMsg.BROKEN_GOODS_IN_TASK);
                message.setData(collect);
                return message;
            }
            Goods goods = null;
            Stock stock = null;
            BigDecimal quantity;
            BigDecimal subOne = new BigDecimal(BigDecimal.ROUND_DOWN);
            Set<String> categoryNameSet = new HashSet<>();
            for (GoodsResponse goodsResponse : goodsResponseList) {
                categoryNameSet.add(goodsResponse.getCategoryName());
                /**
                 * 1.新增到出货商品表里(放到出货过程中了)
                 * 2.从商品表里删除
                 * 3.从库存里对应规格数量-1
                 * 4.//TODO 统计员工工作量
                 */
                goodsMapper.update(new Goods().setDr(true), new UpdateWrapper<Goods>()
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
            Integer isRetail = customerMapper.selectOne(new QueryWrapper<Customer>()
                    .select("is_retail")
                    .eq("id", customerId))
                    .getIsRetail();
            int shipmentBillCount = billMapper.selectCount(new QueryWrapper<Bill>()
                    .in("type", TaskEnums.Type.Shipment.index().toString(), TaskEnums.Type.Retail.index().toString()));
            /**8位编码前面补0格式*/
            DecimalFormat g1 = new DecimalFormat("00000000");
            Bill bill = new Bill();
            String categoryName = String.join("/", categoryNameSet);
            //查询订单编号
            Task orderTask = mapper.selectOne(new QueryWrapper<Task>()
                    .select("order_id")
                    .eq("id", taskId)
                    .eq("cussup_id", customerId));
            if (orderTask != null && StringUtils.isNotBlank(orderTask.getOrderId())) {
                String orderId = orderTask.getOrderId();
                String orderSn = orderBillMapper.selectOne(new QueryWrapper<OrderBill>()
                        .select("order_sn")
                        .eq("id", orderId))
                        .getOrderSn();
                bill.setOrderSn(orderSn);
                bill.setOrderId(orderId);
            }
            bill.setQuantity(new BigDecimal(goodsResponseList.size()));
            bill.setBillNo("BLS" + (g1.format(shipmentBillCount + 1)));
            bill.setCategoryname(categoryName);
            bill.setTaskId(taskId);
            bill.setCussupId(customerId);
            bill.setStatus(BillEnums.Status.Not_Generated.index().toString());
            if (isRetail == 0) {
                bill.setType(TaskEnums.Type.Shipment.index().toString());
            } else {
                bill.setType(TaskEnums.Type.Retail.index().toString());
            }
            bill.setCompanyId(task.getCompanyId());
            bill.setShopId(task.getShopId());
            bill.setBillDate(new Date());
            bill.setCreator(task.getCreator());
            bill.setCreatetime(new Date());
            billMapper.insert(bill);
            String billId = bill.getId();
            jedisStrings.set(UtilConstants.RedisPrefix.SHIPMENTGOOD_TASK + task.getCompanyId() + ":" + task.getShopId()
                    + ":" + task.getCussupId() + ":" + taskId + ":" + "status", TaskEnums.Status.Finish.index().toString());
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
            Customer customer = customerMapper.selectOne(new QueryWrapper<Customer>().select("name", "weight_mode")
                    .eq("id", customerId)
                    .eq("shop_id", requestDTO.getShopId())
                    .eq("company_id", requestDTO.getCompanyId()));
            String customerName = customer.getName();
            //用户习惯计重模式
            String weightMode = customer.getWeightMode();
            Bill bill = billMapper.selectOne(new QueryWrapper<Bill>()
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
                        .eq("company_id", requestDTO.getCompanyId()))
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
                        BigDecimal totalWeightBefore = BigDecimal.ZERO;
                        String specificationId = entryIn.getKey();
                        blankBillDTO.setSpecificationId(specificationId);

                        specification = specificationMapper.selectOne(new QueryWrapper<Specification>()
                                .eq("id", specificationId)
                                .eq("shop_id", requestDTO.getShopId())
                                .eq("company_id", requestDTO.getCompanyId()));
                        blankBillDTO.setProgramId(specification.getProgramId());
                        blankBillDTO.setNumbericalBefore(specification.getNumerical());

                        //循环出货商品,累加重量
                        for (ShipmentGoods shipmentGoods : entryIn.getValue()) {
                            totalWeight = totalWeight.add(shipmentGoods.getWeight());
                            totalWeightBefore = totalWeight;
                        }
                        //如果mode为1(去皮),则需要做额外处理
                        if (blankBillDTO.getMode() == 1) {
                            //需要加上去皮值*数量
                            BigDecimal needToSubtract = specification.getNumerical().multiply(new BigDecimal(blankBillDTO.getQuantity()));
                            totalWeight = totalWeight.add(needToSubtract);
                            blankBillDTO.setMarker("实重(" + specification.getWeightMin() + "~" + specification.getWeightMax() + ")");
                            blankBillDTO.setTotalWeight(totalWeight);
                            blankBillDTO.setTotalWeightBefore(totalWeightBefore);
                        } else {
                            blankBillDTO.setMarker(specification.getMarker());
                            blankBillDTO.setTotalWeight(totalWeight);
                            blankBillDTO.setTotalWeightBefore(totalWeightBefore);
                        }
                        blankBillDTOList.add(blankBillDTO);
                        blankBillDTO = null;
                    }
                }
            }
            blankBillResponseDTO.setWeightMode(weightMode);
            blankBillResponseDTO.setBillId(bill.getId());
            blankBillResponseDTO.setBillNo(bill.getBillNo());
            blankBillResponseDTO.setOrderSn(bill.getOrderSn());
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

    @Override
    public Message finishUnloadTask() {
        return null;
    }

    @Override
    @Transactional
    public Message addOrderShipmentTask(Task task) throws ServiceException {
        /**
         * pre:如果已经有和订单id相关联的账单,则不允许新建出货任务
         * 1.根据订货平台用户id查询合作商id
         * 2.根据合作商id查询是否存在有效的(正在执行或者暂停)任务
         * 3.如果存在活动任务,则创建失败,并返回已有活动任务的提示
         * 4.如果不存在活动任务,则创建任务
         */
        Message message = new Message();
        try {
            Bill bill = billMapper.selectOne(new QueryWrapper<Bill>()
                    .eq("order_id", task.getOrderId())
                    .eq("dr", false)
                    .eq("type", 2));
            if (null != bill) {
                throw new ServiceException("该合作商存在相关联的订单,编号为" + bill.getBillNo() + ",无法再新建出货任务");
            }
            Customer customer = mapper.selectOrderCustomer(task.getOrderUserId(), task.getOrderId());
            Task tempTask = mapper.selectOne(new QueryWrapper<Task>()
                    .eq("dr", 0)
                    .eq("shop_id", task.getShopId())
                    .eq("company_id", task.getCompanyId())
                    .eq("cussup_id", customer.getId())
                    .in("status", TaskEnums.Status.Execute.index().toString()
                            , TaskEnums.Status.Unexecuted.index().toString()));
            if (null != tempTask && null == tempTask.getOrderId()) {
                message.setState(UtilConstants.ResponseCode.NORMAL_TAST_EXIST);
                message.setMessage(UtilConstants.ResponseMsg.TAST_EXIST);
                message.setData(tempTask);
            } else if (null != tempTask && null != tempTask.getOrderId()) {
                String orderSn = orderBillMapper.selectOne(new QueryWrapper<OrderBill>()
                        .select("order_sn")
                        .eq("id", tempTask.getOrderId())
                        .eq("dr", 0)).getOrderSn();
                tempTask.setOrderSn(orderSn);
                message.setState(UtilConstants.ResponseCode.ORDER_TAST_EXIST);
                message.setMessage(UtilConstants.ResponseMsg.ORDER_TAST_EXIST);
                message.setData(tempTask);
            } else {
                task.setCreatetime(new Date());
                task.setModifytime(new Date());
                task.setStatus(TaskEnums.Status.Execute.index().toString());
                task.setType(TaskEnums.Type.Shipment.index().toString());
                task.setDr(false);
                task.setCussupId(customer.getId());
                mapper.insert(task);
                String taskId = task.getId();
                String orderSn = orderBillMapper.selectOne(new QueryWrapper<OrderBill>()
                        .select("order_sn")
                        .eq("id", task.getOrderId())
                        .eq("user_id", task.getOrderUserId()))
                        .getOrderSn();
                task.setOrderSn(orderSn);
                task.setCussupName(customer.getName());
                /**
                 * 把任务状态存入redis,用作出货前判断是否还能出货
                 */
                jedisStrings.set(UtilConstants.RedisPrefix.SHIPMENTGOOD_TASK + task.getCompanyId() + ":" + task.getShopId()
                                + ":" + task.getCussupId() + ":" + taskId + ":" + "status"
                        , TaskEnums.Status.Execute.index().toString());
                message.setData(task);
                message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
                message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
            }
            return message;
        } catch (Exception e) {
            log.error("订货平台订单新建任务失败:" + e);
            if (e instanceof ServiceException) {
                throw new ServiceException(e.getMessage());
            }
            throw new ServiceException("订货平台订单新建任务失败");
        }
    }


}
