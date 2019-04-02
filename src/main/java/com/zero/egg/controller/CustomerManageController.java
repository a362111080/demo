package com.zero.egg.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zero.egg.model.Customer;
import com.zero.egg.model.city;
import com.zero.egg.requestDTO.CustomerRequestDTO;
import com.zero.egg.service.CustomerService;
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

@Api(value="客户管理")
@RestController
@RequestMapping("/customer")
public class CustomerManageController {
    @Autowired
    private CustomerService CustomerSv;

    /**
     * @Description 新增客户
     * @Return   是否成功
     **/
    @ApiOperation(value="新增客户")
    @RequestMapping(value = "/addcustomer",method = RequestMethod.POST)
    public Message AddCustomer(@RequestBody Customer model){
        Message message = new Message();
        try {
            //实际根据界面传值

            model.setCreatetime(new Date());

            model.setModifytime(new Date());

            int strval=CustomerSv.AddCustomer(model);
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
     * @Description 更新客户(删除共用，不作物理删除，更新状态为无效状态) 
     * @Return   是否成功
     **/
    @ApiOperation(value="修改客户信息",notes="客户id不能为空")
    @RequestMapping(value = "/updatecustomer",method = RequestMethod.POST)
    public Message UpdateSupplier(@RequestBody  Customer model) {
        Message message = new Message();
        try {
            //店铺编码  实际根据界面传值
            int strval=CustomerSv.UpdateCustomer(model);
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

    @ApiOperation(value="查询客户列表",notes="分页查询，各种条件查询")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query",name="页码",value="pageNum",dataType="int"),
            @ApiImplicitParam(paramType="query",name="页大小",value="pageSize",dataType="int")
    })
    @RequestMapping(value = "/getcustomerlist",method = RequestMethod.POST)
    public Message GetSupplierList(@RequestParam int pageNum, @RequestParam int pageSize, @RequestBody  Customer model) {
        Message ms = new Message();
        PageHelper.startPage(pageNum, pageSize);
        List<Customer> Customer=CustomerSv.GetCustomerList(model);
        PageInfo<Customer> pageInfo = new PageInfo<>(Customer);
        ms.setData(pageInfo);
        ms.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
        ms.setMessage(UtilConstants.ResponseMsg.SUCCESS);
        return  ms;
    }


    /**
     * @Description 批量删除客户
     * @Param [SupplierRequestDTO]
     * @Return java.lang.String
     **/
    @ApiOperation(value = "批量删除客户")
    @RequestMapping(value = "/delcustomer", method = RequestMethod.POST)
    public Message DeleteCustomer(@RequestBody CustomerRequestDTO Customer) {
        Message message = new Message();
        try {
            if (null != Customer.getIds()) {
                CustomerSv.DeleteCustomer(Customer);
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



    @ApiOperation(value="查询省份列表",notes="无参数返回省份列表，传id返回次级列表")
    @RequestMapping(value = "/getcitys",method = RequestMethod.POST)
    public Message GetCitysList(@RequestBody city model) {
        Message ms = new Message();
        if(model.getParentId()=="" || model.getParentId()==null)
        {
            model.setParentId("100000");
        }
        List<city> Citys=CustomerSv.GetCitys(model);
        ms.setData(Citys);
        ms.setState(ResponseCode.SUCCESS_HEAD);
        ms.setMessage(ResponseMsg.SUCCESS);
        return  ms;
    }

}
