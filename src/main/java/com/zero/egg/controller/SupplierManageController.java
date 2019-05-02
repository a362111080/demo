package com.zero.egg.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zero.egg.model.Supplier;
import com.zero.egg.requestDTO.SupplierRequestDTO;
import com.zero.egg.service.SupplierService;
import com.zero.egg.tool.Message;
import com.zero.egg.tool.UtilConstants;
import com.zero.egg.tool.UtilConstants.ResponseCode;
import com.zero.egg.tool.UtilConstants.ResponseMsg;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Api(value="供应商管理")
@RestController
@RequestMapping("/suppliermanage")
public class SupplierManageController {
	@Autowired
    private SupplierService supplierService;
	/**
	 * @Description 新增供应商
	 * @Return   是否成功
	 **/
	 @ApiOperation(value="新增供应商")
	 @RequestMapping(value = "/addsupplier",method = RequestMethod.POST)
	 public Message AddSupplier(@RequestBody  Supplier model){
		 	 Message message = new Message();
			 try {  
				 	//实际根据界面传值
				 	model.setCreatetime(new Date());
				 	model.setModifytime(new Date());
				    int strval=supplierService.AddSupplier(model);
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
		            //message.setMessage(e.getMessage());
		            return message;
			 	}
		        
	 	}
	 
		/**
		 * @Description 更新供应商(删除共用，不作物理删除，更新状态为无效状态)  可分为供应商/合作商
		 * @Return   是否成功
		 **/
		@ApiOperation(value="修改供应商信息",notes="供应商id不能为空")
	  @RequestMapping(value = "/updatesupplier",method = RequestMethod.POST)
	  public Message UpdateSupplier(@RequestBody  Supplier model) {
		 Message message = new Message();
		 try {  
			 //店铺编码  实际根据界面传值
				 model.setModifytime(new Date());
			    int strval=supplierService.UpdateSupplier(model);
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
	            //message.setMessage(e.getMessage());
	            return message;
		 	}
		
	  }

	 @ApiOperation(value="查询供应商列表",notes="分页查询，各种条件查询")
	 @RequestMapping(value = "/getsupplierlist",method = RequestMethod.POST)
	 public Message GetSupplierList(@RequestBody  SupplierRequestDTO model) {
		  Message ms = new Message();
		  PageHelper.startPage(model.getCurrent().intValue(),model.getSize().intValue());
		  List<Supplier> Supplier=supplierService.GetSupplierList(model);
		  PageInfo<Supplier> pageInfo = new PageInfo<>(Supplier);
		  ms.setData(pageInfo);
		  ms.setState(ResponseCode.SUCCESS_HEAD);
		  ms.setMessage(ResponseMsg.SUCCESS);
	      return  ms;
	 }

	/**
	 * @Description 批量删除供应商
	 * @Param [SupplierRequestDTO]
	 * @Return java.lang.String
	 **/
	@ApiOperation(value = "批量删除供应商")
	@RequestMapping(value = "/delsupplier", method = RequestMethod.POST)
	public Message batchDeleteEggType(@RequestBody SupplierRequestDTO supplier) {
		Message message = new Message();
		try {
			if (null != supplier.getIds()) {
				supplierService.DeleteSupplier(supplier);
				message.setState(ResponseCode.SUCCESS_HEAD);
				message.setMessage(ResponseMsg.SUCCESS);
			} else {
				message.setState(ResponseCode.EXCEPTION_HEAD);
				message.setMessage(ResponseMsg.ATLEAST_ONE);
			}
			return message;
		} catch (Exception e) {
			message.setState(ResponseCode.EXCEPTION_HEAD);
			message.setMessage(ResponseMsg.FAILED);
			return message;
		}
	}


}


