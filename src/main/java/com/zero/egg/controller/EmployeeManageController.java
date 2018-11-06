package com.zero.egg.controller;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
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
     * @Description 获取员工信息
     * @Param 
     * @Return 
     **/
	 @RequestMapping("/getEmployee")
	 public List<Employee>  get(){
		 
		 	String QueryCode="U000002";
		 	String QueryName="";
			//查询员工状态  默认有效
			int QueryLngState=1;
			//查询员工绩效开始时间
			String  QueryBeginTime="";
			//查询员工绩效结束时间
			String  QueryEndTime="";
			//查询店铺编码
			String QueryStoreCode="SC00001";
			
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
	     * @Description 更新员工状态
	     * @Param    员工管理条件类
	     * @Return   是否成功
	     **/
	 
	 @RequestMapping(value = "/UpdateEmployee", method = RequestMethod.POST)
	 public String UpdateEmployee(){
		    String QueryCode="U000002";
		 	String QueryName="";
			//员工状态
			int QueryLngState=3;
			//查询员工绩效开始时间
			String  QueryBeginTime="";
			//查询员工绩效结束时间
			String  QueryEndTime="";
			//查询店铺编码
			String QueryStoreCode="SC00001";
			EmployeeQuery  query=new EmployeeQuery();
			query.QueryCode=QueryCode;
			query.QueryName=QueryName;
			query.QueryLngState=QueryLngState;
			query.QueryStoreCode=QueryStoreCode;
			query.QueryBeginTime=QueryBeginTime;
			query.QueryEndTime=QueryEndTime;
			String result = "sucess";
			 try {
		           
		                //应该是从session里面获取,暂时写死
		            	int strval=employeeService.UpdateEmployee(query);
		            	if (strval>0) {
							return result;
						}
		            	else
		            	{
		            		return "failed";
		            	}
		            
		        } catch (Exception e) {
		            return e.getMessage();
		 }
	 }
}



