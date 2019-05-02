package com.zero.egg.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zero.egg.api.ApiConstants;
import com.zero.egg.model.BarCode;
import com.zero.egg.requestDTO.BarCodeRequestDTO;
import com.zero.egg.requestDTO.LoginUser;
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
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Api(value = "条码管理")
@RestController
@RequestMapping("/barcode")
public class BarCodeController {
    @Autowired
    private BarCodeService bcService;

    @ApiOperation(value = "新增条码")
    @RequestMapping(value = "/addbarcode", method = RequestMethod.POST)
    public Message AddSupplier(@RequestBody BarCodeRequestDTO barCodeRequestDTO, HttpServletRequest request) {
        Message message = new Message();
        try {
            barCodeRequestDTO.setCompanyId("37bc5bcf03d74e40b4093be33aa50870");
            barCodeRequestDTO.setShopId("02ba5d9530f34711be70bc7b6547fbd3");
            barCodeRequestDTO.setCreator("柳柳");
            barCodeRequestDTO.setModifier("柳柳");
            //登录接口有问题,暂时写死柳柳
//                barCodeRequestDTO.setCompanyId(user.getCompanyId());
//               barCodeRequestDTO.setShopId(user.getShopId());
//                barCodeRequestDTO.setCreator(user.getName());
//                barCodeRequestDTO.setModifier(user.getName());
            //非空判断
            if (null != barCodeRequestDTO && null != barCodeRequestDTO.getCode()
                    && null != barCodeRequestDTO.getCategoryId() && null != barCodeRequestDTO.getSupplierId()) {
                LoginUser user = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);


                barCodeRequestDTO.setCreatetime(new Date());
                barCodeRequestDTO.setModifytime(new Date());

                int strval = bcService.AddBarCode(barCodeRequestDTO);
                if (strval > 0) {
                    message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
                    message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
                } else {
                    message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
                    message.setMessage(UtilConstants.ResponseMsg.FAILED);
                }
            } else {
                message.setState(ResponseCode.EXCEPTION_HEAD);
                message.setMessage(ResponseMsg.PARAM_MISSING);
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
    @ApiOperation(value = "批量删除条码", notes = "批量删除 使用ids 传值，用,号拼接")
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

    @ApiOperation(value = "查询条码列表", notes = "分页查询，各种条件查询")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "页码", value = "pageNum", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "页大小", value = "pageSize", dataType = "int")
    })
    @RequestMapping(value = "/getbarcodelist", method = RequestMethod.POST)
    public Message GetBarCodeList(@RequestParam int pageNum, @RequestParam int pageSize, @RequestBody BarCode model) {
        Message ms = new Message();
        PageHelper.startPage(pageNum, pageSize);
        List<BarCodeResponseDTO> BarCode = bcService.GetBarCodeList(model);
        PageInfo<BarCodeResponseDTO> pageInfo = new PageInfo<>(BarCode);
        ms.setData(pageInfo);
        ms.setState(ResponseCode.SUCCESS_HEAD);
        ms.setMessage(ResponseMsg.SUCCESS);
        return ms;
    }


    @ApiOperation(value = "批量打印条码后更换当前最大编码值", notes = "条码主键、本次打印条码数量")
    @RequestMapping(value = "/printbarcode", method = RequestMethod.POST)
    public Message PrintBarCode(@RequestBody @ApiParam(required = true, name = "BarCodeRequestDTO",
            value = "id：条码主键,printNum:本次打印数量，其他字段不需要") BarCodeRequestDTO model) {
        Message message = new Message();
        try {
            if (null != model.getId()) {
                bcService.PrintBarCode(model);
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
