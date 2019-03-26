package com.zero.egg.controller;


import java.time.LocalDateTime;

import javax.servlet.http.HttpSession;

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
import com.zero.egg.api.ApiConstants;
import com.zero.egg.api.dto.BaseResponse;
import com.zero.egg.api.dto.response.ListResponse;
import com.zero.egg.enums.TaskEnums;
import com.zero.egg.model.Task;
import com.zero.egg.model.TaskProgram;
import com.zero.egg.service.ITaskProgramService;
import com.zero.egg.service.ITaskService;
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
	
	
	@ApiOperation(value="查询卸货任务")
	@RequestMapping(value="/unloadlist.data",method=RequestMethod.POST)
	public ListResponse<Task> unloadList(@RequestParam @ApiParam(required =true,name ="pageNum",value="页码") int pageNum,
			@RequestParam @ApiParam(required=true,name="pageSize",value="页大小") int pageSize,
			@RequestBody @ApiParam(required=false,name="task",value="查询字段：企业主键、店铺主键,状态（1执行中/-1完成）") Task task) {
		ListResponse<Task> response = new ListResponse<>(ApiConstants.ResponseCode.EXECUTE_ERROR, ApiConstants.ResponseMsg.EXECUTE_ERROR);
		Page<Task> page = new Page<>();
		page.setPages(pageNum);
		page.setSize(pageSize);
		QueryWrapper<Task> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("dr", false);//查询未删除信息
		if (task != null) {
			queryWrapper.eq(StringUtils.isNotBlank(task.getCompanyId()),"company_id", task.getCompanyId())
			.eq(StringUtils.isNotBlank(task.getShopId()),"shop_id", task.getShopId())
			.eq(StringUtils.isNotBlank(task.getStatus()),"status", task.getStatus())
			.eq("type", TaskEnums.Type.Unload.index().toString());
		}
		IPage<Task> list = taskService.page(page, queryWrapper);
		response.getData().setData(list.getRecords());
		response.getData().setTotal(list.getTotal());
		return response;
		
	}
	
	
	@ApiOperation(value="新增卸货任务")
	@RequestMapping(value="/unloadadd.do",method=RequestMethod.POST)
	public BaseResponse<Object> unloadAdd(@RequestParam @ApiParam(required = true,name="programId",value="方案主键") String programId
			,@RequestBody @ApiParam(required=true,name="task",value="店铺主键、企业主键、供应商主键、备注") Task task
			,HttpSession session) {
		BaseResponse<Object> response = new BaseResponse<>(ApiConstants.ResponseCode.EXECUTE_ERROR, ApiConstants.ResponseMsg.EXECUTE_ERROR);
		task.setId(UuidUtil.get32UUID());
		task.setCreatetime(LocalDateTime.now());
		task.setModifytime(LocalDateTime.now());
		task.setStatus(TaskEnums.Status.Execute.index().toString());
		task.setType(TaskEnums.Type.Unload.index().toString());
		/*LoginInfo loginUser = (LoginInfo) session.getAttribute(SysConstants.LOGIN_USER);*/
		task.setModifier("1");
		task.setCreator("1");
		task.setDr(false);
		if (taskService.save(task)) {
			TaskProgram taskProgram = new TaskProgram();
			taskProgram.setId(UuidUtil.get32UUID());
			taskProgram.setProgramId(programId);
			taskProgram.setTaskId(task.getId());
			taskProgram.setActive(true);
			if (taskProgramService.save(taskProgram)) {
				response.setCode(ApiConstants.ResponseCode.SUCCESS);
				response.setMsg("添加成功");
			}
		}
		return response;
	}
	
	@ApiOperation(value="更换卸货任务方案状态")
	@PostMapping(value="/changeprogram.do")
	public BaseResponse<Object> changeProgram(@RequestBody @ApiParam(required=true,name="taskProgram",value="任务主键、方案主键,状态（true-活动/false-不活动）") TaskProgram taskProgram) {
		BaseResponse<Object> response = new BaseResponse<>(ApiConstants.ResponseCode.EXECUTE_ERROR, ApiConstants.ResponseMsg.EXECUTE_ERROR);
		if (taskService.getById(taskProgram.getTaskId()) != null) {
			UpdateWrapper<TaskProgram> updateWrapper = new UpdateWrapper<>();
			updateWrapper.eq("task_id", taskProgram.getTaskId())
			.eq("program_id", taskProgram.getProgramId());
			if (taskProgramService.update(taskProgram, updateWrapper)) {
				response.setCode(ApiConstants.ResponseCode.SUCCESS);
				response.setMsg("修改成功");
			}
		}else {
			response.setMsg("任务不存在");
		}
		return response;
	}
	
	
	

}
