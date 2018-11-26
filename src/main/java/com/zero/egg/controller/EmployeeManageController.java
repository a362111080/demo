package com.zero.egg.controller;

import java.util.List;

import com.zero.egg.requestDTO.EmployeeRequestDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zero.egg.model.Employee;
import com.zero.egg.service.EmployeeService;
import com.zero.egg.tool.Message;
import com.zero.egg.tool.UtilConstants;
import com.zero.egg.tool.UtilConstants.ResponseCode;
import com.zero.egg.tool.UtilConstants.ResponseMsg;

@Api(value="员工管理")
@RestController
@RequestMapping("/employeemanage")
public class EmployeeManageController
{
	@Autowired
    private EmployeeService employeeService;
/**
 * @Description 获取员工信息（员工绩效相关信息待账单处理方式确认后补充）
 * @Param 
 * @Return 
 **/
@ApiOperation(value="查询员工列表",notes="分页查询，各种条件查询")
@ApiImplicitParams({
		@ApiImplicitParam(paramType="query",name="页码",value="pageNum",dataType="int"),
		@ApiImplicitParam(paramType="query",name="页大小",value="pageSize",dataType="int")
})
 @RequestMapping( value = "/getemployee",method = RequestMethod.POST)
 public Message  getEmployee(@RequestParam int pageNum,@RequestParam int pageSize, EmployeeRequestDTO query ){
		Message ms = new Message();
		PageHelper.startPage(pageNum, pageSize);
		List<Employee> Employee=employeeService.getEmployee(query);		
		PageInfo<Employee> pageInfo = new PageInfo<>(Employee);
		ms.setData(pageInfo);
		ms.setState(ResponseCode.SUCCESS_HEAD);
		ms.setMessage(ResponseMsg.SUCCESS);
        return  ms;
 }
	 
	 
/**
 * @Description 更新员工状态（可扩展其他字段更新）
 * @Param   员工编码,员工状态，员工所属店铺
 * @Return   是否成功
 **/

@ApiOperation(value="修改员工信息")
 @RequestMapping(value = "/updateemployee",method = RequestMethod.POST)
 public Message UpdateEmployee(@RequestBody EmployeeRequestDTO updateModel ){
	 	 Message message = new Message();
		 try {
        	int strval=employeeService.UpdateEmployee(updateModel);
        	if (strval>0) {
        		 message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
                 message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
			}
        	else
        	{  
        		  message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
                  message.setMessage(UtilConstants.ResponseMsg.FAILED);
                 
        	}	
        	return message;
	            
		 } catch (Exception e) {
			 	message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
	            message.setMessage(UtilConstants.ResponseMsg.FAILED);
	            return message;
		 }
	        
 	}
}



