package com.zero.egg.controller;



import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zero.egg.model.Employee;
import com.zero.egg.model.EmployeeQuery;
import com.zero.egg.service.EmployeeService;


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
 public List<Employee>  get(String QueryCode,String QueryStoreCode){
	 
	 	String QueryName="";
		//查询员工状态  默认有效
		int QueryLngState=1;
		//查询员工绩效开始时间
		String  QueryBeginTime="";
		//查询员工绩效结束时间
		String  QueryEndTime="";
		//查询店铺编码
		 QueryStoreCode="SC00001"; //测试使用
		EmployeeQuery  query=new EmployeeQuery();
		query.QueryCode=QueryCode;
		query.QueryName=QueryName;
		query.QueryLngState=QueryLngState;
		query.QueryStoreCode=QueryStoreCode;
		query.QueryBeginTime=QueryBeginTime;
		query.QueryEndTime=QueryEndTime;
		List<Employee> Employee=employeeService.getEmployee(query);
        return  Employee;
 }
	 
	 
/**
 * @Description 更新员工状态（可扩展其他字段更新）
 * @Param    员工管理条件类
 * @Return   是否成功
 **/ 
 
 @RequestMapping(value = "/UpdateEmployee",method = RequestMethod.POST)
 public String UpdateEmployee(String QueryCode,Integer QueryLngState,String QueryStoreCode){	 	
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
				return "sucess";
			}
        	else
        	{  return "failed"; }		 	 
	            
		 } catch (Exception e) {
	            return e.getMessage();
		 }
	        
 }
}



