package com.zero.egg.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zero.egg.model.Supplier;
import com.zero.egg.service.SupplierService;
import com.zero.egg.tool.Message;
import com.zero.egg.tool.UtilConstants;
@RestController
@RequestMapping("/SupplierManage")
public class SupplierManageController {
	@Autowired
    private SupplierService supplierService;
	/**
	 * @Description 新增合作单位  可分为供应商/合作商
	 * @Param    strSupplierPerSon   姓名
	 * @Param	  strSupplierPhone	  联系方式
	 * @param    strSupplierKindCode  类型（SK01供应商/SK02 合作商）
	 * @param	  strSupplierShortName  代号
	 * @param    strSupplierTypeCode  合作单位种类（固定客户/散户/不稳定客户）
	 * @param    strStoreCode         店铺编码
	 * @param    strDesc			  备注信息
	 * @Return   是否成功
	 **/ 
	
	 @RequestMapping(value = "/AddSupplier",method = RequestMethod.POST)
	 public Message AddSupplier(String strSupplierTypeCode, String strSupplierName,String strSupplierPerSon,String strSupplierPhone,String strSupplierKindCode,String strSupplierShortName,String strStoreCode,String strSupplierDesc,int LngState){	 
		 	 Message message = new Message();
			 try {  
				 //店铺编码  实际根据界面传值，测试使用SC00001
				  Supplier model=new Supplier();
				  model.setStrSupplierName(strSupplierName);
				  model.setStrSupplierKindCode(strSupplierKindCode);
				  model.setStrSupplierPerSon(strSupplierPerSon);
				  model.setStrSupplierPhone(strSupplierPhone);
				  model.setStrSupplierShortName(strSupplierShortName);
				  model.setStrSupplierDesc(strSupplierDesc);
				  model.setStrSupplierTypeCode(strSupplierTypeCode);
				  model.setStrStoreCode(strStoreCode);
				  model.setLngState(LngState);
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
		 * @Description 更新合作单位(删除共用，不作物理删除，更新状态为无效状态)  可分为供应商/合作商
		 * @param    strSupplierCode     合作单位编码
		 * @param	  strSupplierName     合作单位名称
		 * @Param    strSupplierPerSon   姓名
		 * @param    strSupplierTypeCode  合作单位种类（固定客户/散户/不稳定客户）
		 * @Param	  strSupplierPhone	  联系方式
		 * @param    strSupplierKindCode  类型（SK01供应商/SK02 合作商）
		 * @param	  strSupplierShortName  代号
		 * @param    strStoreCode         店铺编码
		 * @param    strDesc			  备注信息
		 * @Return   是否成功
		 **/ 
	 @RequestMapping(value = "/UpdateSupplier",method = RequestMethod.POST)
	  public Message UpdateSupplier(String strSupplierTypeCode, String strSupplierCode, String strSupplierName,String strSupplierPerSon,String strSupplierPhone,String strSupplierKindCode,String strSupplierShortName,String strStoreCode,String strSupplierDesc,int LngState) {
		 Message message = new Message();
		 try {  
			 //店铺编码  实际根据界面传值，测试使用SC00001
			  Supplier model=new Supplier();
			  model.setStrSupplierCode(strSupplierCode);
			  model.setStrSupplierName(strSupplierName);
			  model.setStrSupplierKindCode(strSupplierKindCode);
			  model.setStrSupplierPerSon(strSupplierPerSon);
			  model.setStrSupplierPhone(strSupplierPhone);
			  model.setStrSupplierShortName(strSupplierShortName);
			  model.setStrSupplierDesc(strSupplierDesc);
			  model.setStrStoreCode(strStoreCode);
			  model.setLngState(LngState);
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
}


