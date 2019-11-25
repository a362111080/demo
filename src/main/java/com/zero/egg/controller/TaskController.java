package com.zero.egg.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zero.egg.annotation.LoginToken;
import com.zero.egg.api.ApiConstants;
import com.zero.egg.cache.JedisUtil;
import com.zero.egg.enums.TaskEnums;
import com.zero.egg.enums.UserEnums;
import com.zero.egg.model.Bill;
import com.zero.egg.model.BillDetails;
import com.zero.egg.model.Goods;
import com.zero.egg.model.ShipmentGoods;
import com.zero.egg.model.Stock;
import com.zero.egg.model.Task;
import com.zero.egg.model.TaskProgram;
import com.zero.egg.model.UnloadGoods;
import com.zero.egg.requestDTO.LoginUser;
import com.zero.egg.requestDTO.QueryBlankBillGoodsRequestDTO;
import com.zero.egg.requestDTO.TaskRequest;
import com.zero.egg.responseDTO.UnloadReport;
import com.zero.egg.service.IGoodsService;
import com.zero.egg.service.IShipmentGoodsService;
import com.zero.egg.service.IStockService;
import com.zero.egg.service.ITaskProgramService;
import com.zero.egg.service.ITaskService;
import com.zero.egg.tool.Message;
import com.zero.egg.tool.ServiceException;
import com.zero.egg.tool.UtilConstants;
import com.zero.egg.tool.UuidUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 任务控制器
 * </p>
 *
 * @author Hhaifeng
 * @since 2019-03-25
 */
@RestController
@RequestMapping("/task")
@Api(value = "任务管理")
@Slf4j
public class TaskController {

    @Autowired
    private ITaskService taskService;
    @Autowired
    private ITaskProgramService taskProgramService;
    @Autowired
    private IShipmentGoodsService shipmentGoodsService;
    @Autowired
    private IStockService stockService;
    @Autowired
    private IGoodsService goodsService;
    @Autowired
    private HttpServletRequest request;

    @Autowired
    private JedisUtil.Strings jedisStrings;

    @Autowired
    private JedisUtil.Keys jedisKeys;

    @LoginToken
    @ApiOperation(value = "查询任务（卸货出货通用）")
    @RequestMapping(value = "/unloadlist.data", method = RequestMethod.POST)
    public Message<PageInfo<Task>> unloadList(
            @RequestBody @ApiParam(required = false, name = "task", value = "查询字段：企业主键、店铺主键") TaskRequest task) {
        Message<PageInfo<Task>> ms = new Message<PageInfo<Task>>();
        //当前登录用户
        LoginUser user = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
        task.setCompanyId(user.getCompanyId());
        task.setShopId(user.getShopId());
        PageHelper.startPage(task.getCurrent().intValue(), task.getSize().intValue());
        List<Task> ResponseDto = taskService.QueryTaskList(task);
        PageInfo<Task> pageInfo = new PageInfo<>(ResponseDto);
        ms.setData(pageInfo);
        ms.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
        ms.setMessage(UtilConstants.ResponseMsg.SUCCESS);
        return ms;
    }

    @LoginToken
    @ApiOperation(value = "新增卸货任务")
    @RequestMapping(value = "/unloadadd", method = RequestMethod.POST)
    public Message unloadAdd(@RequestBody Task task) {
        Message message = new Message();
        //当前登录用户
        LoginUser user = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
        try {

            if (taskService.GetActiveTaskBySupplier(task.getCussupId()) < 1) {
                task.setId(UuidUtil.get32UUID());
                task.setCreatetime(new Date());
                task.setModifytime(new Date());
                task.setStatus(TaskEnums.Status.Execute.index().toString());
                task.setType(TaskEnums.Type.Unload.index().toString());
                task.setModifier(user.getId());
                task.setCreator(user.getId());
                task.setShopId(user.getShopId());
                task.setEquipmentNo("");
                task.setCompanyId(user.getCompanyId());
                task.setDr(false);
                if (taskService.save(task)) {
                    //卸货任务新增成功后，写入任务方案表
                    TaskProgram taskProgram = new TaskProgram();
                    taskProgram.setId(UuidUtil.get32UUID());
                    taskProgram.setProgramId(task.getProgramId());
                    taskProgram.setTaskId(task.getId());
                    taskProgram.setActive(true);
                    if (taskProgramService.save(taskProgram)) {
                        message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
                        message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
                    } else {
                        message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
                        message.setMessage(UtilConstants.ResponseMsg.FAILED);
                    }
                } else {
                    message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
                    message.setMessage("新增卸货任务失败");
                }
            } else {
                message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
                message.setMessage("当前供应商存在未完成的任务，无法新建任务");
            }
        } catch (Exception e) {
            message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
            message.setMessage(UtilConstants.ResponseMsg.FAILED);
            return message;
        }
        return message;
    }


    @LoginToken
    @ApiOperation(value = "更换卸货任务方案")
    @RequestMapping(value = "/unloadprochange", method = RequestMethod.POST)
    public Message unloadprochange(@RequestBody Task task) {
        Message message = new Message();

        try {
            //停用当前任务所有方案
            taskService.UnloadProStop(task.getId());

            if (taskService.IsExtisUnloadTaskProgram(task) > 0) {
                //新方案本身存在，更新活跃状态
                taskService.UnloadProChange(task);
                message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
                message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
            } else {
                //新方案不存在，新增任务方案
                TaskProgram taskProgram = new TaskProgram();
                taskProgram.setId(UuidUtil.get32UUID());
                taskProgram.setProgramId(task.getNewProgram());
                taskProgram.setTaskId(task.getId());
                taskProgram.setActive(true);
                if (taskProgramService.save(taskProgram)) {
                    message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
                    message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
                } else {
                    message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
                    message.setMessage("更换卸货方案失败");
                }
            }
        } catch (Exception e) {
            message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
            message.setMessage(UtilConstants.ResponseMsg.FAILED);
            return message;
        }
        return message;
    }


    @LoginToken
    @ApiOperation(value = "更换任务状态（不可用）")
    @PostMapping(value = "/changeprogram.do")
    public Message<Object> changeProgram(HttpServletRequest request
            , @RequestBody @ApiParam(required = true, name = "taskProgram", value = "任务主键、方案主键,状态（true-活动/false-不活动）") TaskProgram taskProgram) {
        //BaseResponse<Object> response = new BaseResponse<>(ApiConstants.ResponseCode.EXECUTE_ERROR, ApiConstants.ResponseMsg.EXECUTE_ERROR);
        Message<Object> message = new Message<Object>();
        //当前登录用户
        LoginUser loginUser = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
        Task task = taskService.getById(taskProgram.getTaskId());
        if (task != null) {

            Task changeTask = new Task();
            changeTask.setId(taskProgram.getTaskId());
            changeTask.setModifier(loginUser.getId());
            changeTask.setModifytime(new Date());
            if (taskProgram.getActive() == false) {//如果为不活动，，则为任务完成，
                changeTask.setStatus(TaskEnums.Status.Finish.index().toString());
            } else {//如果为活动，，则为任务执行中
                changeTask.setStatus(TaskEnums.Status.Execute.index().toString());
            }
            //修改任务状态
            if (taskService.updateById(changeTask)) {
                if (taskProgram.getActive() == false) {
                    if (TaskEnums.Type.Unload.index().toString().equals(task.getType())) {//卸货任务
                        //将卸货商品表中新增至库存和商品表

                    } else if (TaskEnums.Type.Shipment.index().toString().equals(task.getType())) {//出货任务
                        //从出货商品表中去更新库存和商品表
                        QueryWrapper<ShipmentGoods> shipmentWrapper = new QueryWrapper<>();
                        shipmentWrapper.eq("task_id", task.getId());
                        List<ShipmentGoods> shipmentlist = shipmentGoodsService.list(shipmentWrapper);
                        if (shipmentlist != null && shipmentlist.size() > 0) {
                            for (ShipmentGoods shipmentGoods : shipmentlist) {
                                //减去相应的库存
                                QueryWrapper<Stock> stockWrapper = new QueryWrapper<>();
                                stockWrapper.eq("specification_id", shipmentGoods.getSpecificationId());
                                Stock stock = stockService.getOne(stockWrapper);
                                stock.setQuantity(stock.getQuantity().subtract(new BigDecimal(1)));
                                stockService.updateById(stock);
                                //逻辑删除相对应的商品表
                                Goods goods = new Goods();
                                goods.setDr(true);
                                QueryWrapper<Goods> goodsWrapper = new QueryWrapper<>();
                                goodsWrapper.eq("goods_no", shipmentGoods.getGoodsNo());
                                goodsService.update(goods, goodsWrapper);
                            }
                        }
                    }
                }
            }
            //修改任务方案状态
            UpdateWrapper<TaskProgram> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("task_id", taskProgram.getTaskId())
                    .eq("program_id", taskProgram.getProgramId());
            if (taskProgramService.update(taskProgram, updateWrapper)) {
                message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
                message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
            } else {
                message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
                message.setMessage(UtilConstants.ResponseMsg.FAILED);
            }
        } else {
            message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
            message.setMessage("任务不存在");
        }
        return message;
    }

    @LoginToken
    @ApiOperation(value = "取消出货任务(PC端或者老板移动端取消任务)")
    @PostMapping(value = "/cancel-shipment")
    public Message cancelShipment(@RequestBody @ApiParam(required = true, name = "task", value = "1.cussupId(客商主键) 2.任务主键") Task task) {
        Message message = new Message<Object>();
        //当前登录用户
        LoginUser loginUser = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
        try {

            /**
             * 权限判断,只有PC端和老板移动端能取消任务
             */
            if (request.getAttribute(ApiConstants.USER_TYPE) != UserEnums.Type.Pc.index()
                    && request.getAttribute(ApiConstants.USER_TYPE) != UserEnums.Type.Boss.index()) {
                message = new Message();
                message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
                message.setMessage(UtilConstants.ResponseMsg.NO_PERMISSION);
                return message;
            }

            /**
             * 非空判断
             */
            if (task == null || null == task.getId()
                    || null == task.getCussupId()) {
                message = new Message();
                message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
                message.setMessage(UtilConstants.ResponseMsg.PARAM_ERROR);
                return message;
            }
            //任务id
            String taskId = task.getId();
            //客户id
            String customerId = task.getCussupId();
            task.setModifier(loginUser.getId());
            //企业和店铺id以登陆者信息为准
            task.setCompanyId(loginUser.getCompanyId());
            task.setShopId(loginUser.getShopId());
            /**
             * 如果redis里面所存对应的任务状态为已完成或已取消,返回对应消息
             */
            if (!jedisKeys.exists(UtilConstants.RedisPrefix.SHIPMENTGOOD_TASK
                    + loginUser.getCompanyId() + ":" + loginUser.getShopId() + ":" + customerId + ":" + taskId + ":" + "status")
                    || TaskEnums.Status.Finish.index().toString().equals(jedisStrings.get(UtilConstants.RedisPrefix.SHIPMENTGOOD_TASK
                    + loginUser.getCompanyId() + ":" + loginUser.getShopId() + ":" + customerId + ":" + taskId + ":" + "status"))
                    || TaskEnums.Status.CANCELED.index().toString().equals(jedisStrings.get(UtilConstants.RedisPrefix.SHIPMENTGOOD_TASK
                    + loginUser.getCompanyId() + ":" + loginUser.getShopId() + ":" + customerId + ":" + taskId + ":" + "status"))) {
                message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
                message.setMessage(UtilConstants.ResponseMsg.TASK_FINISH_OR_CANCELED);
                return message;
            }
            message = taskService.cancelShipmentTask(task, taskId, customerId);
        } catch (Exception e) {
            log.error("cancel-shipment failed:" + e);
            message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
            //如果是service层抛出的ServiceExeption,则将错误信息装进返回消息中
            if (e instanceof ServiceException) {
                message.setMessage(e.getMessage());
            } else {
                message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
                message.setMessage(UtilConstants.ResponseMsg.FAILED);
            }
        }
        return message;
    }


    @LoginToken
    @ApiOperation(value = "新增出货任务")
    @RequestMapping(value = "/shipmenttaskadd", method = RequestMethod.POST)
    public Message shipmentTaskAdd(@RequestBody @ApiParam(required = true, name = "task",
            value = "cussupId(客商主键)") Task task) {
        Message message;
        try {
            //非空判断
            if (null == task || null == task.getCussupId() || "".equals(task.getCussupId())) {
                message = new Message();
                message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
                message.setMessage(UtilConstants.ResponseMsg.PARAM_MISSING);
                return message;
            }
            //当前登录用户
            LoginUser loginUser = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
            task.setModifier(loginUser.getId());
            task.setCreator(loginUser.getId());
            task.setCompanyId(loginUser.getCompanyId());
            task.setShopId(loginUser.getShopId());
            message = taskService.addShipmentTask(task);

        } catch (Exception e) {
            message = new Message();
            message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
            message.setMessage(e.getMessage());
        }
        return message;
    }


    /**
     * @Description 卸货任务结束/取消
     * @Return 是否成功
     **/
    @LoginToken
    @ApiOperation(value = "卸货任务结束/取消", notes = "任务id不能为空")
    @RequestMapping(value = "/UnloadTaskState", method = RequestMethod.POST)
    public Message UnloadTaskState(@RequestBody Task model) {
        Message message = new Message();
        LoginUser user = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
        try {
            model.setModifier(user.getId());
            model.setModifytime(new Date());
            //获取当前任务状态
            TaskRequest ckdto=new TaskRequest();
            ckdto.setId(model.getId());
            List<Task> res = taskService.QueryTaskList(ckdto);
            if (res.get(0).getStatus().equals(model.getStatusBefore())) {
                if (null != model.getId()) {
                    if (model.getStatus().equals(TaskEnums.Status.CANCELED.index().toString())) {
                        //取消任务
                        //1.更改任务状态；
                        if (taskService.updateById(model)) {
                            //2.更改卸货明细状态

                            taskService.UpdateUnloadDetl(model.getId());
                            message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
                            message.setMessage(UtilConstants.ResponseMsg.SUCCESS);

                        } else {
                            message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
                            message.setMessage(UtilConstants.ResponseMsg.FAILED);
                        }
                    } else if (model.getStatus().equals(TaskEnums.Status.Unexecuted.index().toString())) {
                        //1.更改任务状态； 暂停
                        if (taskService.updateById(model)) {
                            message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
                            message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
                        } else {
                            message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
                            message.setMessage(UtilConstants.ResponseMsg.FAILED);
                        }
                    } else if (model.getStatus().equals(TaskEnums.Status.Execute.index().toString())) {
                        //1.更改任务状态；执行中
                        if (taskService.updateById(model)) {
                            message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
                            message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
                        } else {
                            message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
                            message.setMessage(UtilConstants.ResponseMsg.FAILED);
                        }
                    } else if (model.getStatus().equals(TaskEnums.Status.Finish.index().toString())) {
                        //卸货完成
                        BigDecimal sumQuantity = BigDecimal.ZERO;
                        BigDecimal Amount = BigDecimal.ZERO;
                        List<UnloadGoods> IUnloadList = taskService.GetUnloadDetl(model.getId());
                        for (int k = 0; k < model.getUnloadDetails().size(); k++) {
                            sumQuantity = sumQuantity.add(model.getUnloadDetails().get(k).getQuantity());
                            Amount = Amount.add(model.getUnloadDetails().get(k).getPrice().multiply(model.getUnloadDetails().get(k).getQuantity()));
                        }
                        if (IUnloadList.size() > 0) {
                            if (IUnloadList.size() == sumQuantity.intValue()) {
                                //1.更改任务状态；
                                if (taskService.updateById(model)) {
                                    //写入账单明细
                                    String SupplierId = IUnloadList.get(0).getSupplierId();
                                    for (int m = 0; m < IUnloadList.size(); m++) {
                                        //写入商品表
                                        Goods Igoods = new Goods();
                                        Igoods.setId(UuidUtil.get32UUID());
                                        Igoods.setShopId(IUnloadList.get(m).getShopId());
                                        Igoods.setCompanyId(IUnloadList.get(m).getCompanyId());
                                        Igoods.setSpecificationId(IUnloadList.get(m).getSpecificationId());
                                        Igoods.setGoodsCategoryId(IUnloadList.get(m).getGoodsCategoryId());
                                        Igoods.setGoodsNo(IUnloadList.get(m).getGoodsNo());
                                        Igoods.setMarker(IUnloadList.get(m).getMarker());
                                        Igoods.setMode(IUnloadList.get(m).getMode());
                                        Igoods.setCreator(user.getId());
                                        Igoods.setModifier(user.getId());
                                        Igoods.setCreatetime(new Date());
                                        Igoods.setModifytime(new Date());
                                        Igoods.setWeight(IUnloadList.get(m).getWeight());
                                        Igoods.setSupplierId(IUnloadList.get(m).getSupplierId());
                                        taskService.InsertGoods(Igoods);
                                    }
                                    //写入库存表
                                    String Billcategoryname = "";
                                    List<UnloadReport> UnloadReport = taskService.GetUnloadReport(model.getId());
                                    for (int u = 0; u < UnloadReport.size(); u++) {

                                        //拼接账单品种
                                        String Categoryname = UnloadReport.get(u).getCategoryname();
                                        if (null != Categoryname) {
                                            if (!Billcategoryname.contains(Categoryname) && Billcategoryname != "") {
                                                Billcategoryname = Billcategoryname + "/" + Categoryname;
                                            } else {
                                                Billcategoryname = Categoryname;
                                            }
                                        }

                                        //写入库存表
                                        if (taskService.IsExtis(UnloadReport.get(u).getSpecificationId()) > 0) {
                                            //库存已存在该批次，原基础数量+1
                                            taskService.updateStock(UnloadReport.get(u));
                                        } else {
                                            //库存不存在规格，新增库存规格   数量为1
                                            Stock Istock = new Stock();
                                            Istock.setId(UuidUtil.get32UUID());
                                            Istock.setShopId(UnloadReport.get(u).getShopId());
                                            Istock.setCompanyId(UnloadReport.get(u).getCompanyId());
                                            Istock.setSpecificationId(UnloadReport.get(u).getSpecificationId());
                                            Istock.setQuantity(UnloadReport.get(u).getSumval());
                                            Istock.setRemark("卸货新增");
                                            Istock.setCreator(user.getId());
                                            Istock.setModifier(user.getId());
                                            Istock.setCreatetime(new Date());
                                            Istock.setModifytime(new Date());
                                            Istock.setDr(true);
                                            taskService.insertStock(Istock);
                                        }
                                    }

                                    String Billid = UuidUtil.get32UUID();
                                    for (int n = 0; n < model.getUnloadDetails().size(); n++) {
                                        //写入账单明细
                                        BillDetails IDetails = new BillDetails();
                                        IDetails.setId(UuidUtil.get32UUID());
                                        IDetails.setShopId(user.getShopId());
                                        IDetails.setCompanyId(user.getCompanyId());
                                        IDetails.setBillId(Billid);
                                        IDetails.setGoodsCategoryId(model.getUnloadDetails().get(n).getGoodsCategoryId());
                                        IDetails.setProgramId(model.getUnloadDetails().get(n).getProgramId());
                                        String SpecificationId = model.getUnloadDetails().get(n).getSpecificationId();
                                        if (null != SpecificationId && "" != SpecificationId) {
                                            IDetails.setSpecificationId(model.getUnloadDetails().get(n).getSpecificationId());
                                        } else {
                                            IDetails.setSpecificationId("");
                                        }
                                        IDetails.setPrice(model.getUnloadDetails().get(n).getPrice());
                                        IDetails.setQuantity(model.getUnloadDetails().get(n).getQuantity());
                                        IDetails.setAmount(model.getUnloadDetails().get(n).getPrice().multiply(model.getUnloadDetails().get(n).getQuantity()));
                                        IDetails.setCreatetime(new Date());
                                        IDetails.setCreator(user.getId());
                                        IDetails.setModifier(user.getId());
                                        IDetails.setModifytime(new Date());
                                        IDetails.setDr(true);
                                        taskService.insertBillDetails(IDetails);
                                    }
                                    //写入账单统计数据
                                    Bill Ibill = new Bill();
                                    Ibill.setId(Billid);
                                    Ibill.setShopId(user.getShopId());
                                    Ibill.setCompanyId(user.getCompanyId());
                                    Ibill.setCussupId(SupplierId);
                                    Ibill.setBillDate(new Date());
                                    Ibill.setTaskId(model.getId());
                                    Ibill.setType(TaskEnums.Type.Unload.index().toString());
                                    Ibill.setQuantity(sumQuantity);
                                    Ibill.setAmount(Amount);
                                    Ibill.setRealAmount(Amount);
                                    Ibill.setCategoryname(Billcategoryname);
                                    Ibill.setCreatetime(new Date());
                                    Ibill.setCreator(user.getId());
                                    Ibill.setModifier(user.getId());
                                    Ibill.setModifytime(new Date());
                                    Ibill.setDr(true);
                                    taskService.insertBill(Ibill);
                                    message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
                                    message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
                                } else {
                                    message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
                                    message.setMessage(UtilConstants.ResponseMsg.FAILED);
                                }
                            } else {
                                message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
                                message.setMessage("卸货数量不一致，请重新操作！");
                            }
                        } else {
                            message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
                            message.setMessage("无卸货记录，请重新操作！");
                        }
                    }
                } else {
                    message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
                    message.setMessage(UtilConstants.ResponseMsg.FAILED);
                }

            }
            else
            {
                message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
                message.setMessage("任务状态异常，请刷新后再操作！");
            }
            return message;
        } catch (Exception e) {
            message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
            message.setMessage(UtilConstants.ResponseMsg.FAILED);
            return message;
        }

    }

    @LoginToken
    @ApiOperation(value = "员工完成出货任务")
    @RequestMapping(value = "/emplyeefinishtask", method = RequestMethod.POST)
    public Message emplyeeFinishTask(@RequestBody @ApiParam(required = true, name = "task", value = "1.cussupId(客商主键) 2.任务主键") Task task) {
        /**
         * 员工完成任务,实际是将出货任务状态变为暂停状态,防止其他进行同一出货任务的员工,能继续扫码出货
         */
        Message message = new Message<Object>();
        //当前登录用户
        LoginUser loginUser = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
        /**
         * 非空判断
         */
        if (task == null || null == task.getId()
                || null == task.getCussupId()) {
            message = new Message();
            message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
            message.setMessage(UtilConstants.ResponseMsg.PARAM_ERROR);
            return message;
        }
        try {
            //任务id
            String taskId = task.getId();
            //客户id
            String customerId = task.getCussupId();
            /**
             * 如果redis里面所存对应的任务状态为已完成或已取消或者已暂停,返回对应消息
             */
            if (!jedisKeys.exists(UtilConstants.RedisPrefix.SHIPMENTGOOD_TASK
                    + loginUser.getCompanyId() + ":" + loginUser.getShopId() + ":" + customerId + ":" + taskId + ":" + "status")
                    || TaskEnums.Status.Finish.index().toString().equals(jedisStrings.get(UtilConstants.RedisPrefix.SHIPMENTGOOD_TASK
                    + loginUser.getCompanyId() + ":" + loginUser.getShopId() + ":" + customerId + ":" + taskId + ":" + "status"))
                    || TaskEnums.Status.CANCELED.index().toString().equals(jedisStrings.get(UtilConstants.RedisPrefix.SHIPMENTGOOD_TASK
                    + loginUser.getCompanyId() + ":" + loginUser.getShopId() + ":" + customerId + ":" + taskId + ":" + "status"))
                    || TaskEnums.Status.Unexecuted.index().toString().equals(jedisStrings.get(UtilConstants.RedisPrefix.SHIPMENTGOOD_TASK
                    + loginUser.getCompanyId() + ":" + loginUser.getShopId() + ":" + customerId + ":" + taskId + ":" + "status"))) {
                message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
                message.setMessage(UtilConstants.ResponseMsg.TASK_FINISH_OR_CANCELED_OR_UNEXECUTED);
                return message;
            }
            task.setModifier(loginUser.getId());
            //企业和店铺id以登陆者信息为准
            task.setCompanyId(loginUser.getCompanyId());
            task.setShopId(loginUser.getShopId());
            message = taskService.emplyeeFinishTask(task, taskId, customerId);
        } catch (Exception e) {
            log.error("emplyeeFinishTask failed:" + e);
            message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
            //如果是service层抛出的ServiceExeption,则将错误信息装进返回消息中
            if (e instanceof ServiceException) {
                message.setMessage(e.getMessage());
            } else {
                message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
                message.setMessage(UtilConstants.ResponseMsg.FAILED);
            }
        }

        return message;
    }

    @PostMapping(value = "/finishshipmenttask")
    @LoginToken
    @ApiOperation(value = "完成出货任务(PC端或者老板移动端完成任务)")
    public Message finishShipmentTask(@RequestBody @ApiParam(required = true, name = "task", value = "1.cussupId(客商主键) 2.任务主键") Task task) {
        Message message = new Message<Object>();
        //当前登录用户
        LoginUser loginUser = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
        try {
            /**
             * 权限判断,只有PC端和老板移动端能完成任务
             */
            if (request.getAttribute(ApiConstants.USER_TYPE) != UserEnums.Type.Pc.index()
                    && request.getAttribute(ApiConstants.USER_TYPE) != UserEnums.Type.Boss.index()) {
                message = new Message();
                message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
                message.setMessage(UtilConstants.ResponseMsg.NO_PERMISSION);
                return message;
            }
            /**
             * 非空判断
             */
            if (task == null || null == task.getId()
                    || null == task.getCussupId()) {
                message = new Message();
                message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
                message.setMessage(UtilConstants.ResponseMsg.PARAM_ERROR);
                return message;
            }
            //任务id
            String taskId = task.getId();
            //客户id
            String customerId = task.getCussupId();
            /**
             * 如果任务还在进行中,表示员工没有完成(暂停)任务
             */
            if (!jedisKeys.exists(UtilConstants.RedisPrefix.SHIPMENTGOOD_TASK
                    + loginUser.getCompanyId() + ":" + loginUser.getShopId() + ":" + customerId + ":" + taskId)
                    || TaskEnums.Status.Execute.index().toString().equals(jedisStrings.get(UtilConstants.RedisPrefix.SHIPMENTGOOD_TASK
                    + loginUser.getCompanyId() + ":" + loginUser.getShopId() + ":" + customerId + ":" + taskId + ":" + "status"))) {
                message = new Message();
                message.setState(UtilConstants.ResponseCode.EMPLOYEE_NOT_FINISH);
                message.setMessage(UtilConstants.ResponseMsg.ASURE_EMPLOYEE_FINISH);
                return message;
            }
            /**
             * 如果任务已经取消,返回前端信息,方便刷新页面
             */
            else if (TaskEnums.Status.CANCELED.index().toString().equals(jedisStrings.get(UtilConstants.RedisPrefix.SHIPMENTGOOD_TASK
                    + loginUser.getCompanyId() + ":" + loginUser.getShopId() + ":" + customerId + ":" + taskId + ":" + "status"))) {
                message = new Message();
                message.setState(UtilConstants.ResponseCode.TASK_CANCLED);
                message.setMessage(UtilConstants.ResponseMsg.TASK_CANCLED_MSG);
                return message;
            }
            /**
             * 如果任务已经完成,返回前端信息,方便跳转账单页面
             */
            else if (TaskEnums.Status.Finish.index().toString().equals(jedisStrings.get(UtilConstants.RedisPrefix.SHIPMENTGOOD_TASK
                    + loginUser.getCompanyId() + ":" + loginUser.getShopId() + ":" + customerId + ":" + taskId + ":" + "status"))) {
                message = new Message();
                message.setState(UtilConstants.ResponseCode.TASK_FINISHED);
                message.setMessage(UtilConstants.ResponseMsg.TASK_FINISHED_MSG);
                return message;
            }
            /**
             * 正常完成出货流程
             */
            //企业和店铺id以登陆者信息为准
            task.setCompanyId(loginUser.getCompanyId());
            task.setShopId(loginUser.getShopId());
            task.setCreator(loginUser.getId());
            message = taskService.RealFinishTask(task, taskId, customerId);
        } catch (Exception e) {
            log.error("FinishShipmentTask failed:" + e);
            message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
            //如果是service层抛出的ServiceExeption,则将错误信息装进返回消息中
            if (e instanceof ServiceException) {
                message.setMessage(e.getMessage());
            } else {
                message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
                message.setMessage(UtilConstants.ResponseMsg.FAILED);
            }
        }
        return message;
    }

    @PostMapping(value = "/forcefinishshipmenttask")
    @LoginToken
    @ApiOperation(value = "老板强制完成出货任务(PC端或者老板移动端完成任务)")
    public Message forceFinishShipmentTask(@RequestBody @ApiParam(required = true, name = "task", value = "1.cussupId(客商主键) 2.任务主键") Task task) {
        Message message = new Message<Object>();
        //当前登录用户
        LoginUser loginUser = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
        try {
            /**
             * 权限判断,只有PC端和老板移动端能完成任务
             */
            if (request.getAttribute(ApiConstants.USER_TYPE) != UserEnums.Type.Pc.index()
                    && request.getAttribute(ApiConstants.USER_TYPE) != UserEnums.Type.Boss.index()) {
                message = new Message();
                message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
                message.setMessage(UtilConstants.ResponseMsg.NO_PERMISSION);
                return message;
            }
            /**
             * 非空判断
             */
            if (task == null || null == task.getId()
                    || null == task.getCussupId()) {
                message = new Message();
                message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
                message.setMessage(UtilConstants.ResponseMsg.PARAM_ERROR);
                return message;
            }
            //任务id
            String taskId = task.getId();
            //客户id
            String customerId = task.getCussupId();
            //获取当前任务状态
            String status = jedisStrings.get(UtilConstants.RedisPrefix.SHIPMENTGOOD_TASK + loginUser.getCompanyId() + ":"
                    + loginUser.getShopId() + ":" + customerId + ":" + taskId + ":" + "status");
            if (status.equals(TaskEnums.Status.Finish.index().toString())
                    || status.equals(TaskEnums.Status.CANCELED.index().toString())) {
                message = new Message();
                message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
                message.setMessage(UtilConstants.ResponseMsg.TASK_FINISH_OR_CANCELED);
                return message;
            }
            /**
             * 正常完成出货流程
             */
            //企业和店铺id以登陆者信息为准
            task.setCompanyId(loginUser.getCompanyId());
            task.setShopId(loginUser.getShopId());
            task.setCreator(loginUser.getId());
            message = taskService.RealFinishTask(task, taskId, customerId);
        } catch (Exception e) {
            log.error("FinishShipmentTask failed:" + e);
            message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
            //如果是service层抛出的ServiceExeption,则将错误信息装进返回消息中
            if (e instanceof ServiceException) {
                message.setMessage(e.getMessage());
            } else {
                message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
                message.setMessage(UtilConstants.ResponseMsg.FAILED);
            }
        }
        return message;
    }

    @PostMapping(value = "/queryblanbillsortlist")
    @LoginToken
    @ApiOperation(value = "查询空账单详情)")
    public Message queryBlanBillSortList(@RequestBody @ApiParam(required = true, name = "task"
            , value = "1.任务主键 2.账单编号  3.账单id") QueryBlankBillGoodsRequestDTO requestDTO) {
        Message message = new Message<Object>();
        //当前登录用户
        LoginUser loginUser = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
        try {
            /**
             * 权限判断,只有PC端和老板移动端能完成任务
             */
            if (request.getAttribute(ApiConstants.USER_TYPE) != UserEnums.Type.Pc.index()
                    && request.getAttribute(ApiConstants.USER_TYPE) != UserEnums.Type.Boss.index()) {
                message = new Message();
                message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
                message.setMessage(UtilConstants.ResponseMsg.NO_PERMISSION);
                return message;
            }
            //非空判断
            if (requestDTO == null || null == requestDTO.getTaskId()) {
                message = new Message();
                message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
                message.setMessage(UtilConstants.ResponseMsg.PARAM_ERROR);
                return message;
            }
            //企业和店铺id以登陆者信息为准
            requestDTO.setCompanyId(loginUser.getCompanyId());
            requestDTO.setShopId(loginUser.getShopId());
            message = taskService.queryBlankGoods(requestDTO);

        } catch (Exception e) {
            log.error("FinishShipmentTask failed:" + e);
            message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
            //如果是service层抛出的ServiceExeption,则将错误信息装进返回消息中
            if (e instanceof ServiceException) {
                message.setMessage(e.getMessage());
            } else {
                message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
                message.setMessage(UtilConstants.ResponseMsg.FAILED);
            }
        }
        return message;
    }
}
