package com.zero.egg.controller;


import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zero.egg.annotation.LoginToken;
import com.zero.egg.api.ApiConstants;
import com.zero.egg.enums.TaskEnums;
import com.zero.egg.model.Goods;
import com.zero.egg.model.ShipmentGoods;
import com.zero.egg.model.Stock;
import com.zero.egg.model.Task;
import com.zero.egg.model.TaskProgram;
import com.zero.egg.requestDTO.LoginUser;
import com.zero.egg.requestDTO.TaskRequest;
import com.zero.egg.service.IGoodsService;
import com.zero.egg.service.IShipmentGoodsService;
import com.zero.egg.service.IStockService;
import com.zero.egg.service.ITaskProgramService;
import com.zero.egg.service.ITaskService;
import com.zero.egg.tool.Message;
import com.zero.egg.tool.UtilConstants;
import com.zero.egg.tool.UuidUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * <p>
 *  任务控制器
 * </p>
 *
 * @author Hhaifeng
 * @since 2019-03-25
 */
@RestController
@RequestMapping("/task")
@Api(value="任务管理")
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
	
	
	@LoginToken
	@ApiOperation(value="查询卸货任务")
	@RequestMapping(value="/unloadlist.data",method=RequestMethod.POST)
	public Message<PageInfo<Task>> unloadList(
			@RequestBody @ApiParam(required=false,name="task",value="查询字段：企业主键、店铺主键,状态（1执行中/-1完成）") TaskRequest task) {
		Message<PageInfo<Task>> ms = new Message<PageInfo<Task>>();
		PageHelper.startPage(task.getCurrent().intValue(), task.getSize().intValue());
		List<Task> ResponseDto=taskService.QueryTaskList(task);
		PageInfo<Task> pageInfo = new PageInfo<>(ResponseDto);
		ms.setData(pageInfo);
		ms.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
		ms.setMessage(UtilConstants.ResponseMsg.SUCCESS);
		return  ms;
	}
	
	@LoginToken
	@ApiOperation(value="新增卸货任务")
	@RequestMapping(value="/unloadadd.do",method=RequestMethod.POST)
	public Message<Object> unloadAdd(@RequestParam @ApiParam(required = true,name="programId",value="方案主键") String programId
			,@RequestBody @ApiParam(required=true,name="task",value="店铺主键、企业主键、供应商主键、备注，设备号") Task task
			,HttpServletRequest request) {
		//BaseResponse<Object> response = new BaseResponse<>(ApiConstants.ResponseCode.EXECUTE_ERROR, ApiConstants.ResponseMsg.EXECUTE_ERROR);
		Message<Object> message = new Message<Object>();
		task.setId(UuidUtil.get32UUID());
		task.setCreatetime(new Date());
		task.setModifytime(new Date());
		task.setStatus(TaskEnums.Status.Unexecuted.index().toString());
		task.setType(TaskEnums.Type.Unload.index().toString());
		//当前登录用户
		LoginUser loginUser = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
		task.setModifier(loginUser.getId());
		task.setCreator(loginUser.getId());
		task.setDr(false);
		if (taskService.save(task)) {
			TaskProgram taskProgram = new TaskProgram();
			taskProgram.setId(UuidUtil.get32UUID());
			taskProgram.setProgramId(programId);
			taskProgram.setTaskId(task.getId());
			taskProgram.setActive(true);
			if (taskProgramService.save(taskProgram)) {
				message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
				message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
			}else {
				message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
				message.setMessage(UtilConstants.ResponseMsg.FAILED);
			}
		}
		return message;
	}
	
	@LoginToken
	@ApiOperation(value="更换任务状态")
	@PostMapping(value="/changeprogram.do")
	public Message<Object> changeProgram(HttpServletRequest request
			,@RequestBody @ApiParam(required=true,name="taskProgram",value="任务主键、方案主键,状态（true-活动/false-不活动）") TaskProgram taskProgram) {
		//BaseResponse<Object> response = new BaseResponse<>(ApiConstants.ResponseCode.EXECUTE_ERROR, ApiConstants.ResponseMsg.EXECUTE_ERROR);
		Message<Object> message = new Message<Object>();
		//当前登录用户
		LoginUser loginUser = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
		Task task = taskService.getById(taskProgram.getTaskId());
		if ( task != null) {
			
			Task changeTask = new Task();
			changeTask.setId(taskProgram.getTaskId());
			changeTask.setModifier(loginUser.getId());
			changeTask.setModifytime(new Date());
			if (taskProgram.getActive() == false) {//如果为不活动，，则为任务完成，
				changeTask.setStatus(TaskEnums.Status.Finish.index().toString());
			}else {//如果为活动，，则为任务执行中
				changeTask.setStatus(TaskEnums.Status.Execute.index().toString());
			}
			//修改任务状态
			if (taskService.updateById(changeTask)) {
				if (taskProgram.getActive() == false) {
					if (TaskEnums.Type.Unload.index().toString().equals(task.getType())) {//卸货任务
						//将卸货商品表中新增至库存和商品表
						
					}else if (TaskEnums.Type.Shipment.index().toString().equals(task.getType())) {//出货任务
						//从出货商品表中去更新库存和商品表
						QueryWrapper<ShipmentGoods> shipmentWrapper = new QueryWrapper<>();
						shipmentWrapper.eq("task_id", task.getId());
						List<ShipmentGoods> shipmentlist =shipmentGoodsService.list(shipmentWrapper);
						if (shipmentlist != null && shipmentlist.size()>0) {
							for (ShipmentGoods shipmentGoods : shipmentlist) {
								//减去相应的库存
								QueryWrapper<Stock> stockWrapper = new QueryWrapper<>();
								stockWrapper.eq("specification_id", shipmentGoods.getSpecificationId());
								Stock stock =stockService.getOne(stockWrapper);
								stock.setQuantity(stock.getQuantity().subtract(new BigDecimal(1)));
								stockService.updateById(stock);
								//逻辑删除相对应的商品表
								Goods goods  = new Goods();
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
			}else {
				message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
				message.setMessage(UtilConstants.ResponseMsg.FAILED);
			}
		}else {
			message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
			message.setMessage("任务不存在");
		}
		return message;
	}
	@LoginToken
	@ApiOperation(value="取消出货任务")
	@PostMapping(value="/cancel-shipment.do")
	public Message<Object> cancelShipment(HttpServletRequest request
			,@RequestBody @ApiParam(required=true,name="task",value="任务主键") Task task) {
		//BaseResponse<Object> response = new BaseResponse<>(ApiConstants.ResponseCode.EXECUTE_ERROR, ApiConstants.ResponseMsg.EXECUTE_ERROR);
		Message<Object> message = new Message<Object>();
		//当前登录用户
		//LoginUser loginUser = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
		if (taskService.getById(task.getId()) != null) {
			task.setStatus(TaskEnums.Status.Unexecuted.index().toString());
			taskService.updateById(task);
			QueryWrapper<ShipmentGoods> queryWrapper = new QueryWrapper<>();
			queryWrapper.eq("task_id", task.getId());
			if (shipmentGoodsService.remove(queryWrapper)) {
				message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
				message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
			}else {
				message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
				message.setMessage(UtilConstants.ResponseMsg.FAILED);
			}
		}else {
			message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
			message.setMessage("任务不存在");
		}
		return message;
	}
	
	@LoginToken
	@ApiOperation(value="查询出货任务")
	@RequestMapping(value="/shipment-task-list.data",method=RequestMethod.POST)
	public Message<IPage<Task>> shipmentlist(
			@RequestBody @ApiParam(required=false,name="task",value="查询字段：企业主键、店铺主键,状态（1执行中/-1完成）") TaskRequest task) {
		//ListResponse<Task> response = new ListResponse<>(ApiConstants.ResponseCode.EXECUTE_ERROR, ApiConstants.ResponseMsg.EXECUTE_ERROR);
		Message<IPage<Task>> message = new Message<IPage<Task>>();
		Page<Task> page = new Page<>();
		page.setCurrent(task.getCurrent());
		page.setSize(task.getSize());
		QueryWrapper<Task> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("dr", false);//查询未删除信息
		if (task != null) {
			queryWrapper.eq(StringUtils.isNotBlank(task.getCompanyId()),"company_id", task.getCompanyId())
			.eq(StringUtils.isNotBlank(task.getShopId()),"shop_id", task.getShopId())
			.eq(StringUtils.isNotBlank(task.getStatus()),"status", task.getStatus())
			.eq("type", TaskEnums.Type.Shipment.index().toString());
		}
		IPage<Task> list = taskService.page(page, queryWrapper);
		message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
		message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
		message.setData(list);
		return message;
		
	}
	
	@LoginToken
	@ApiOperation(value="新增出货任务")
	@RequestMapping(value="/shipment-task-add.do",method=RequestMethod.POST)
	public Message<Object> shipmentTaskAdd(@RequestParam @ApiParam(required = true,name="programId",value="方案主键") String programId
			,@RequestBody @ApiParam(required=true,name="task",value="店铺主键、企业主键、供应商主键、备注，设备号") Task task
			,HttpServletRequest request) {
		//BaseResponse<Object> response = new BaseResponse<>(ApiConstants.ResponseCode.EXECUTE_ERROR, ApiConstants.ResponseMsg.EXECUTE_ERROR);
		Message<Object> message = new Message<Object>();
		task.setId(UuidUtil.get32UUID());
		task.setCreatetime(new Date());
		task.setModifytime(new Date());
		task.setStatus(TaskEnums.Status.Unexecuted.index().toString());
		task.setType(TaskEnums.Type.Shipment.index().toString());
		//当前登录用户
		LoginUser loginUser = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
		task.setModifier(loginUser.getId());
		task.setCreator(loginUser.getId());
		task.setDr(false);
		if (taskService.save(task)) {
			TaskProgram taskProgram = new TaskProgram();
			taskProgram.setId(UuidUtil.get32UUID());
			taskProgram.setProgramId(programId);
			taskProgram.setTaskId(task.getId());
			taskProgram.setActive(true);
			if (taskProgramService.save(taskProgram)) {
				message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
				message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
			}else {
				message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
				message.setMessage(UtilConstants.ResponseMsg.FAILED);
			}
		}
		return message;
	}
	
	
	
	

}
