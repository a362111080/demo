package com.zero.egg.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zero.egg.model.Employee;
import com.zero.egg.model.EmployeeQuery;
import com.zero.egg.service.EmployeeService;
import com.zero.egg.tool.Message;
import com.zero.egg.tool.UtilConstants;
import com.zero.egg.tool.UtilConstants.ResponseCode;
import com.zero.egg.tool.UtilConstants.ResponseMsg;


@RestController
@RequestMapping("/EmployeeManage")
public class EmployeeManageController
{
	@Autowired
    private EmployeeService employeeService;
/**
 * @Description 获取员工信息（员工绩效相关信息待账单处理方式确认后补充）
 * @Param 
 * @Return 
 **/
 @RequestMapping( value = "/getEmployee",method = RequestMethod.POST)
 public Message  getEmployee(int pageNum,int pageSize,String QueryCode,String QueryStoreCode,String QueryName,Integer QueryLngState,String QueryBeginTime,String QueryEndTime ){
		Message ms = new Message();
		EmployeeQuery  query=new EmployeeQuery();
		PageHelper.startPage(pageNum, pageSize);
		//员工编码
		query.QueryCode=QueryCode;
		//员工名称
		query.QueryName=QueryName;
		//查询员工状态
		query.QueryLngState=QueryLngState;
		//查询店铺编码
		query.QueryStoreCode=QueryStoreCode;
		//查询员工绩效开始时间
		query.QueryBeginTime=QueryBeginTime;
		//查询员工绩效结束时间
		query.QueryEndTime=QueryEndTime;
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
 
 @RequestMapping(value = "/UpdateEmployee",method = RequestMethod.POST)
 public Message UpdateEmployee(String QueryCode,Integer QueryLngState,String QueryStoreCode){	 
	 	 Message message = new Message();
		 try {  
			 
			 EmployeeQuery  updateModel=new EmployeeQuery();
			//员工编码
			updateModel.QueryCode =QueryCode;
			//员工状态
			updateModel.QueryLngState=QueryLngState;
			//查询店铺编码
			updateModel.QueryStoreCode=QueryStoreCode;
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



