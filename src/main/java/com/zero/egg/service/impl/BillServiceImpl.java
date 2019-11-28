package com.zero.egg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zero.egg.dao.BillDetailsMapper;
import com.zero.egg.dao.BillMapper;
import com.zero.egg.dao.BrokenGoodsMapper;
import com.zero.egg.dao.GoodsMapper;
import com.zero.egg.dao.ShipmentGoodsMapper;
import com.zero.egg.dao.StockMapper;
import com.zero.egg.dao.TaskMapper;
import com.zero.egg.enums.BillEnums;
import com.zero.egg.enums.TaskEnums;
import com.zero.egg.model.Bill;
import com.zero.egg.model.BillDetails;
import com.zero.egg.model.BrokenGoods;
import com.zero.egg.model.Customer;
import com.zero.egg.model.Goods;
import com.zero.egg.model.ShipmentGoods;
import com.zero.egg.model.Stock;
import com.zero.egg.model.Supplier;
import com.zero.egg.model.Task;
import com.zero.egg.requestDTO.BillRequest;
import com.zero.egg.requestDTO.BlankBillRequestDTO;
import com.zero.egg.requestDTO.CancelShipmentBillRequestDTO;
import com.zero.egg.requestDTO.CustomerRequestDTO;
import com.zero.egg.requestDTO.LoginUser;
import com.zero.egg.requestDTO.SupplierRequestDTO;
import com.zero.egg.responseDTO.BlankBillDTO;
import com.zero.egg.responseDTO.CategorySum;
import com.zero.egg.service.IBillService;
import com.zero.egg.tool.Message;
import com.zero.egg.tool.ServiceException;
import com.zero.egg.tool.UtilConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
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
 * @since 2019-03-14
 */
@Service
@Slf4j
public class BillServiceImpl extends ServiceImpl<BillMapper, Bill> implements IBillService {


    @Autowired
    private BillMapper mapper;

    @Autowired
    private BillDetailsMapper billDetailsMapper;

    @Autowired
    private StockMapper stockMapper;

    @Autowired
    private ShipmentGoodsMapper shipmentGoodsMapper;

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private BrokenGoodsMapper brokenGoodsMapper;


    @Override
    public List<Supplier> GetSupplierList(SupplierRequestDTO model) {
        return mapper.GetSupplierList(model);
    }

    @Override
    public List<Customer> GetCustomerList(CustomerRequestDTO model) {
        return mapper.GetCustomerList(model);

    }

    @Override
    public List<Bill> getBilllist(BillRequest model) {
        return mapper.getBilllist(model);
    }

    @Override
    public List<CategorySum> getBillCategorySum(String id) {
        return mapper.getBillCategorySum(id);
    }

    @Override
    @Transactional
    public Message updateBillAndDetails(BlankBillRequestDTO blankBillRequestDTO, LoginUser loginUser) {
        Message message = new Message();
        try {
            /*
             * 1.根据传入的账单status不为0,则不能修改
             * 2.新增账单细节,统计每个方案细节的应收金额和整个账单的应收金额
             * 3.更新账单信息 BillEnums.Status.Normal.toString()
             */
            //账单id
            String billId = blankBillRequestDTO.getBlankBillDTOList().get(0).getBillId();
            //实收金额
            BigDecimal realAmount = blankBillRequestDTO.getRealAmount();
            //如果账单是零售账单,则不能更新实收金额
            String type = mapper.selectOne(new QueryWrapper<Bill>()
                    .select("type")
                    .eq("id", billId))
                    .getType();
            if (TaskEnums.Type.Retail.index() == Integer.parseInt(type)) {
                realAmount = BigDecimal.ZERO;
            }
            String currentsStutus = mapper.selectOne(new QueryWrapper<Bill>()
                    .select("status")
                    .eq("id", billId)
                    .eq("shop_id", loginUser.getShopId())
                    .eq("company_id", loginUser.getCompanyId())
                    .eq("dr", 0))
                    .getStatus();
            if (BillEnums.Status.Not_Generated.index() != Integer.parseInt(currentsStutus)) {
                message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
                message.setMessage(UtilConstants.ResponseMsg.NOT_BLANK_BILL);
                return message;
            }

            String creator = loginUser.getId();
            String companyId = loginUser.getCompanyId();
            String shopId = loginUser.getShopId();

            //整个账单应收金额和数量
            BigDecimal amount = BigDecimal.ZERO;
            BigDecimal totalAuatity = BigDecimal.ZERO;
            List<BlankBillDTO> blankBillDTOList = blankBillRequestDTO.getBlankBillDTOList();
            BillDetails billDetails = null;
            for (BlankBillDTO blankBillDTO : blankBillDTOList) {
                String categoryId = blankBillDTO.getCategoryId();
                billDetails = new BillDetails();
                billDetails.setCompanyId(companyId);
                billDetails.setShopId(shopId);
                billDetails.setBillId(billId);
                billDetails.setGoodsCategoryId(categoryId);
                billDetails.setProgramId(blankBillDTO.getProgramId());
                billDetails.setGoodsCategoryId(categoryId);
                billDetails.setSpecificationId(blankBillDTO.getSpecificationId());
                //前端新输入的单价
                BigDecimal price = blankBillDTO.getPrice();
                BigDecimal quantity = new BigDecimal(blankBillDTO.getQuantity());
                /*
                 * 账单细节应收金额后端重新计算
                 * 如果货物原本是去皮方式,现在也是去皮方式,
                 *             如果没有重新输入去皮值 小计=价格*重量
                 *             如果重新输入去皮值 小计=价格*(去皮前重量-数量*新去皮值)
                 * 如果货物原本是包方式,现在也是包方式,小计=价格*数量
                 * 如果货物原本是去皮方式,现在是包方式,小计=价格*数量
                 * 如果货物原本是包方式,现在是去皮方式,小计=价格*(重量-数量*去皮值),去皮值=blankBillDTO.getNumberical()
                 */
                BigDecimal subTotal;
                /* 1:去皮 2:包*/
                if (1 == blankBillDTO.getCurrentMode()) {
                    billDetails.setCurrentMode(1);
                    if (1 == blankBillDTO.getMode()) {
                        billDetails.setTotalWeight(blankBillDTO.getTotalWeight());
                        /* 去皮进,按斤出 */
                        if (null == blankBillDTO.getNumberical()) {
                            subTotal = price.multiply(blankBillDTO.getTotalWeight());
                        } else {
                            subTotal = price.multiply(blankBillDTO.getTotalWeightBefore().subtract(quantity.multiply(blankBillDTO.getNumberical())));
                        }
                    } else if (2 == blankBillDTO.getMode()) {
                        billDetails.setTotalWeight(blankBillDTO.getTotalWeight());
                        billDetails.setNumberical(blankBillDTO.getNumberical());
                        /* 包进,按斤出 */
                        subTotal = price.multiply(blankBillDTO.getTotalWeight().subtract(quantity.multiply(blankBillDTO.getNumberical())));
                    } else {
                        log.error("param Exception,oriMode or currentMode is dissatisfied");
                        throw new ServiceException("param Exception,oriMode or currentMode is dissatisfied");
                    }
                } else if (2 == blankBillDTO.getCurrentMode()) {
                    billDetails.setCurrentMode(2);
                    billDetails.setTotalWeight(blankBillDTO.getTotalWeight());
                    /* 无论是包进,还是去皮进,如果按箱出,小计=单价*数量 */
                    subTotal = price.multiply(quantity);
                } else {
                    log.error("param Exception,oriMode or currentMode is dissatisfied");
                    throw new ServiceException("param Exception,oriMode or currentMode is dissatisfied");
                }
                billDetails.setPrice(price);
                billDetails.setQuantity(quantity);
                billDetails.setAmount(subTotal);
                billDetails.setCreator(creator);
                billDetails.setCreatetime(new Date());
                billDetails.setModifier(creator);
                billDetails.setModifytime(new Date());
                billDetails.setDr(false);
                billDetailsMapper.insert(billDetails);
                amount = amount.add(subTotal);
                totalAuatity = totalAuatity.add(quantity);
            }
            Bill bill = new Bill();
            bill.setId(billId);
            bill.setRealAmount(realAmount);
            bill.setAmount(amount);
            bill.setModifier(creator);
            bill.setModifytime(new Date());
            bill.setQuantity(totalAuatity);
            bill.setStatus(BillEnums.Status.Normal.index().toString());
            mapper.updateById(bill);
            message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
            message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
            return message;
        } catch (Exception e) {
            log.error("updateBillAndDetails failed:" + e);
            throw new ServiceException("updateBillAndDetails failed");
        }
    }

    @Override
    @Transactional
    public Message updateBillAndDetails(Bill bill, LoginUser loginUser) throws ServiceException {
        Message message = new Message();
        try {
            BigDecimal amount = BigDecimal.ZERO;
            //账单细节列表
            List<BillDetails> details = bill.getUnloadDetails();
            //如果账单是零售账单,则不能更新实收金额
            String type = mapper.selectOne(new QueryWrapper<Bill>()
                    .select("type")
                    .eq("id", bill.getId()))
                    .getType();
            if (TaskEnums.Type.Retail.index() == Integer.parseInt(type) && null != bill.getRealAmount()) {
                bill.setRealAmount(null);
            }
            //需要更新库的账单细节对象
            BillDetails IDetails = new BillDetails();
            BigDecimal subTotal;
            BigDecimal numberical;
            for (BillDetails billDetails : details) {
                IDetails.setId(billDetails.getId());
                IDetails.setPrice(billDetails.getPrice());
                //如果当前计重方式为去皮,小计=单价*(重量-去皮值*数量)
                if (1 == billDetails.getCurrentMode()) {
                    //去皮值可能为null
                    if (null == billDetails.getNumberical() || billDetails.getNumberical().compareTo(BigDecimal.ZERO) == 0) {
                        numberical = BigDecimal.ZERO;
                    } else {
                        numberical = billDetails.getNumberical();
                    }
                    subTotal = billDetails.getPrice()
                            .multiply(billDetails.getTotalWeight()
                                    .subtract(billDetails.getQuantity()
                                            .multiply(numberical)));
                } else if (2 == billDetails.getCurrentMode()) {
                    subTotal = billDetails.getPrice().multiply(billDetails.getQuantity());
                    numberical = BigDecimal.ZERO;
                } else {
                    //如果缺少参数就返回前端并提示
                    message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
                    message.setMessage(UtilConstants.ResponseMsg.PARAM_MISSING);
                    return message;
                }
                amount = amount.add(subTotal);
                IDetails.setCurrentMode(billDetails.getCurrentMode());
                IDetails.setNumberical(numberical);
                IDetails.setAmount(subTotal);
                billDetailsMapper.updateDetails(IDetails);
            }
            //更新总价
            bill.setAmount(amount);
            bill.setModifier(loginUser.getId());
            bill.setModifytime(new Date());
            mapper.updateById(bill);
            message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
            message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            log.error("updateBillAndDetails error" + e);
            throw new ServiceException("updateBillAndDetails error");
        }
        return message;
    }

    @Override
    @Transactional
    public Message cancelShipmentBill(CancelShipmentBillRequestDTO requestDTO) throws ServiceException {
        Message message = new Message();
        /**
         * 如果账单已经存在报损任务,则不能取消账单
         * 1.根据账单id查出该账单信息,主要是相关联的出货任务id,逻辑删除该账单信息(包括账单细节表里的关联信息)
         * 2.根据任务id查询出货商品表相关已出货物信息,主要是商品编号,方案细节(规格)id,并将dr置位1
         * 3.将商品表里的商品编号所对应的的商品的dr属性重置为0
         * 4.将库存表里面的对应规格的库存数量加上去
         */
        try {
            Bill bill = mapper.selectOne(new QueryWrapper<Bill>()
                    .select("id,cussup_id,task_id")
                    .eq("company_id", requestDTO.getCompanyId())
                    .eq("shop_id", requestDTO.getShopId())
                    .eq("id", requestDTO.getBillId())
                    .eq("dr", 0));
            //合作商id
            String customerId = bill.getCussupId();
            //出货任务id
            String taskId = bill.getTaskId();
            Task task = taskMapper.selectById(taskId);
            //如果查询不到该任务或者该任务不是出货任务,
            if (null == task || !"2".equals(task.getType())) {
                message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
                message.setMessage(UtilConstants.ResponseMsg.PARAM_ERROR);
                return message;
            }
            //查出对应出货商品集合
            List<ShipmentGoods> shipmentGoodsList = shipmentGoodsMapper.selectList(new QueryWrapper<ShipmentGoods>()
                    .select("id,specification_id,goods_no")
                    .eq("task_id", taskId)
                    .eq("customer_id", customerId)
                    .eq("company_id", requestDTO.getCompanyId())
                    .eq("shop_id", requestDTO.getShopId())
                    .eq("dr", 0));
            //出货商品id集合
            Set<String> shipmentGoodsIdSet = new HashSet<>();
            //规格集合
            Map<String, Long> specificationMap = shipmentGoodsList.stream()
                    .collect(Collectors.groupingBy(ShipmentGoods::getSpecificationId, Collectors.counting()));
            //商品编号集合
            Set<String> goodsNoSet = new HashSet<>();
            for (ShipmentGoods shipmentGoods : shipmentGoodsList) {
                shipmentGoodsIdSet.add(shipmentGoods.getId());
                goodsNoSet.add(shipmentGoods.getGoodsNo());
            }
            //验证每箱货是否已经被报损,如果是,则要返回这些已经报损的货物编号,提示删除后再结束任务
            //坏掉的货物商品编号
            List<String> brokenGoods = brokenGoodsMapper.selectList(new QueryWrapper<BrokenGoods>()
                    .eq("company_id", requestDTO.getCompanyId())
                    .eq("shop_id", requestDTO.getShopId())
                    .eq("dr", false)
                    .in("goods_no", goodsNoSet))
                    .stream()
                    .map(v -> v.getGoodsNo())
                    .collect(Collectors.toList());
            brokenGoods.removeAll(Collections.singleton(null));
            if (brokenGoods.size() > 0) {
                message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
                message.setMessage(UtilConstants.ResponseMsg.BROKEN_GOODS_IN_TASK);
                message.setData(brokenGoods);
                return message;
            }
            bill.setDr(true);
            //对应账单逻辑删除
            mapper.updateById(bill);
            BillDetails billDetails = new BillDetails();
            //对应账单细节逻辑删除
            billDetailsMapper.update(new BillDetails().setDr(true), new UpdateWrapper<BillDetails>()
                    .eq("bill_id", requestDTO.getBillId()));

            //逻辑删除已出货物信息
            shipmentGoodsMapper.update(new ShipmentGoods().setDr(true), new UpdateWrapper<ShipmentGoods>()
                    .eq("company_id", requestDTO.getCompanyId())
                    .eq("shop_id", requestDTO.getShopId())
                    .in("id", shipmentGoodsIdSet));
            //已出货物重新入库
            goodsMapper.update(new Goods().setDr(false), new UpdateWrapper<Goods>()
                    .eq("company_id", requestDTO.getCompanyId())
                    .eq("shop_id", requestDTO.getShopId())
                    .in("goods_no", goodsNoSet));
            //库存数量恢复
            Stock stock;
            BigDecimal currentQuantity;
            for (Map.Entry<String, Long> entry : specificationMap.entrySet()) {
                stock = stockMapper.selectOne(new QueryWrapper<Stock>()
                        .select("id,quantity")
                        .eq("company_id", requestDTO.getCompanyId())
                        .eq("shop_id", requestDTO.getShopId())
                        .eq("specification_id", entry.getKey())
                        .eq("dr", false));
                currentQuantity = stock.getQuantity();
                stock.setQuantity(currentQuantity.add(BigDecimal.valueOf(entry.getValue())));
                stockMapper.update(stock, new UpdateWrapper<Stock>()
                        .eq("id", stock.getId())
                        .eq("company_id", requestDTO.getCompanyId())
                        .eq("shop_id", requestDTO.getShopId())
                        .eq("dr", false));
            }
            message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
            message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            log.error("cancelShipmentBill service error:" + e);
            throw new ServiceException("cancelShipmentBill service error");
        }
        return message;
    }

}
