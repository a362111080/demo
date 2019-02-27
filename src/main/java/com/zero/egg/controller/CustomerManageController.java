package com.zero.egg.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zero.egg.model.Customer;
import com.zero.egg.service.CustomerService;
import com.zero.egg.tool.Message;
import com.zero.egg.tool.UtilConstants;
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
            model.setCompanyid ("1");
            model.setShopid("1");
            model.setCreator("老陈");
            model.setCreatetime(new Date());
            model.setModifier("老陈");
            model.setModifytime(new Date());
            model.setStatus("正常");
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
     * @Description 更新供应商(删除共用，不作物理删除，更新状态为无效状态)  可分为供应商/合作商
     * @Return   是否成功
     **/
    @ApiOperation(value="修改供应商信息",notes="供应商id不能为空")
    @RequestMapping(value = "/updatecustomer",method = RequestMethod.POST)
    public Message UpdateSupplier(@RequestBody  Customer model) {
        Message message = new Message();
        try {
            //店铺编码  实际根据界面传值
            model.setId("d56b1af03a5611e9ab6200163e08ec43");
            model.setShopid("1");
            model.setModifier("老陈2号");
            model.setModifytime(new Date());
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

    @ApiOperation(value="查询供应商列表",notes="分页查询，各种条件查询")
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
}
