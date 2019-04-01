package com.zero.egg.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zero.egg.model.BarCode;
import com.zero.egg.requestDTO.BarCodeRequestDTO;
import com.zero.egg.responseDTO.BarCodeResponseDTO;
import com.zero.egg.service.BarCodeService;
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
@RequestMapping("/barcode")
public class BarCodeController {
    @Autowired
    private BarCodeService bcService;

    @ApiOperation(value="新增条码")
    @RequestMapping(value = "/addbarcode",method = RequestMethod.POST)
    public Message AddSupplier(@RequestBody BarCodeRequestDTO req) {
        Message message = new Message();
        try {
            //实际根据界面传值
            req.setShopId("1");
            req.setCompanyId("1");
            req.setCategoryId("1");
            req.setSupplierId("1");
            req.setCode("测试编码");
            req.setCreator("老陈");
            req.setCreatetime(new Date());
            req.setModifier("老陈");
            req.setModifytime(new Date());
            int strval=bcService.AddBarCode(req);
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
     * @Description 批量删除条码
     * @Param [SupplierRequestDTO]
     * @Return java.lang.String
     **/
    @ApiOperation(value = "批量删除条码")
    @RequestMapping(value = "/delbarcode", method = RequestMethod.POST)
    public Message DeleteBarCode(@RequestBody BarCodeRequestDTO model) {
        Message message = new Message();
        try {
            if (null != model.getIds()) {
                bcService.DeleteBarCode(model);
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

    @ApiOperation(value="查询条码列表",notes="分页查询，各种条件查询")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query",name="页码",value="pageNum",dataType="int"),
            @ApiImplicitParam(paramType="query",name="页大小",value="pageSize",dataType="int")
    })
    @RequestMapping(value = "/getbarcodelist",method = RequestMethod.POST)
    public Message GetBarCodeList(@RequestParam int pageNum, @RequestParam int pageSize, @RequestBody BarCode model) {
        Message ms = new Message();
        PageHelper.startPage(pageNum, pageSize);
        List<BarCodeResponseDTO> BarCode=bcService.GetBarCodeList(model);
        PageInfo<BarCodeResponseDTO> pageInfo = new PageInfo<>(BarCode);
        ms.setData(pageInfo);
        ms.setState(ResponseCode.SUCCESS_HEAD);
        ms.setMessage(ResponseMsg.SUCCESS);
        return  ms;
    }

}
