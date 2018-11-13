package com.zero.egg.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.zero.egg.model.EmployeeQuery;
import com.zero.egg.requestDTO.EmployeeRequestDTO;
import com.zero.egg.service.EmployeeService;
import com.zero.egg.tool.Message;
import com.zero.egg.tool.UtilConstants;


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
 public Message  getEmployee(@RequestBody EmployeeRequestDTO  employeeRequestDTO ){
	 	Message message = new Message();
	 	 try {
	 		 	message=employeeService.getEmployee(employeeRequestDTO);
	            return message;
	        } catch (Exception e) {
	            message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
	            message.setMessage(UtilConstants.ResponseMsg.FAILED);
	            return message;
	        }
        
 }
	 
	 
/**
 * @Description 更新员工状态（可扩展其他字段更新）
 * @Param    员工管理条件类
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



