package com.zero.egg.controller;


import java.util.Date;

import javax.servlet.http.HttpServletRequest;

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
import com.zero.egg.model.ShipmentGoods;
import com.zero.egg.model.Task;
import com.zero.egg.model.TaskProgram;
import com.zero.egg.requestDTO.LoginUser;
import com.zero.egg.requestDTO.TaskRequest;
import com.zero.egg.service.IShipmentGoodsService;
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
	
	@LoginToken
	@ApiOperation(value="查询卸货任务")
	@RequestMapping(value="/unloadlist.data",method=RequestMethod.POST)
	public Message<IPage<Task>> unloadList(
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
			.eq("type", TaskEnums.Type.Unload.index().toString());
		}
		IPage<Task> list = taskService.page(page, queryWrapper);
		message.setData(list);
		message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
		message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
		return message;
		
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
	@ApiOperation(value="更换任务方案状态")
	@PostMapping(value="/changeprogram.do")
	public Message<Object> changeProgram(HttpServletRequest request
			,@RequestBody @ApiParam(required=true,name="taskProgram",value="任务主键、方案主键,状态（true-活动/false-不活动）") TaskProgram taskProgram) {
		//BaseResponse<Object> response = new BaseResponse<>(ApiConstants.ResponseCode.EXECUTE_ERROR, ApiConstants.ResponseMsg.EXECUTE_ERROR);
		Message<Object> message = new Message<Object>();
		//当前登录用户
		LoginUser loginUser = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
		if (taskService.getById(taskProgram.getTaskId()) != null) {
			
			Task task = new Task();
			task.setId(taskProgram.getTaskId());
			task.setModifier(loginUser.getId());
			task.setModifytime(new Date());
			if (taskProgram.getActive() == false) {//如果为不活动，，则为任务完成，
				task.setStatus(TaskEnums.Status.Finish.index().toString());
			}else {//如果为活动，，则为任务执行中
				task.setStatus(TaskEnums.Status.Execute.index().toString());
			}
			//修改任务状态
			taskService.updateById(task);
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
