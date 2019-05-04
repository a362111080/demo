package com.zero.egg.controller;

import com.zero.egg.annotation.LoginToken;
import com.zero.egg.api.ApiConstants;
import com.zero.egg.model.BarCodeInfoDTO;
import com.zero.egg.requestDTO.BarCodeRequestDTO;
import com.zero.egg.requestDTO.LoginUser;
import com.zero.egg.service.BarCodeService;
import com.zero.egg.tool.Message;
import com.zero.egg.tool.TransferUtil;
import com.zero.egg.tool.UtilConstants;
import com.zero.egg.tool.UtilConstants.ResponseCode;
import com.zero.egg.tool.UtilConstants.ResponseMsg;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Api(value = "条码管理")
@RestController
@RequestMapping("/barcode")
@Slf4j
public class BarCodeController {
    @Autowired
    private BarCodeService bcService;

    @ApiOperation(value = "新增条码")
    @RequestMapping(value = "/addbarcode", method = RequestMethod.POST)
    @LoginToken
    public Message AddSupplier(@RequestBody BarCodeRequestDTO barCodeRequestDTO, HttpServletRequest request) {
        Message message = new Message();
        try {
            LoginUser user = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);

            barCodeRequestDTO.setCompanyId(user.getCompanyId());
            barCodeRequestDTO.setShopId(user.getShopId());
            barCodeRequestDTO.setCreator(user.getName());
            barCodeRequestDTO.setModifier(user.getName());
            //非空判断
            if (null != barCodeRequestDTO.getCode()
                    && null != barCodeRequestDTO.getCategoryId() && null != barCodeRequestDTO.getSupplierId()
                    && null != barCodeRequestDTO.getSupplierName()) {
                barCodeRequestDTO.setCreatetime(new Date());
                barCodeRequestDTO.setModifytime(new Date());

                message = bcService.AddBarCode(barCodeRequestDTO);

            } else {
                message.setState(ResponseCode.EXCEPTION_HEAD);
                message.setMessage(ResponseMsg.PARAM_MISSING);
            }
            return message;
        } catch (Exception e) {
            message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
            message.setMessage(UtilConstants.ResponseMsg.FAILED);
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

    @ApiOperation(value = "查询条码列表", notes = "分页查询(根据供应商名称模糊查询")
    @RequestMapping(value = "/getbarcodelist", method = RequestMethod.POST)
    @LoginToken
    public Message GetBarCodeList(@RequestBody BarCodeListRequestDTO listRequestDTO, HttpServletRequest request) {
        Message message = null;
        LoginUser user = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
        try {
            if (null != user.getCompanyId() && null != user.getShopId()) {
                listRequestDTO.setCompanyId(user.getCompanyId());
                listRequestDTO.setShopId(user.getShopId());
                message = bcService.GetBarCodeList(listRequestDTO);
            } else {
                message = new Message();
                message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
                message.setMessage(UtilConstants.ResponseMsg.FAILED);
            }
        } catch (Exception e) {
            log.error("GetBarCodeList failed:" + e);
            message = new Message();
            message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
            message.setMessage(UtilConstants.ResponseMsg.FAILED);
        }
        return message;
    }


    /**
     * 前端需要把母二维码信息加上printNum后请求改接口
     *
     * @param infoDTO
     * @return
     */
    @RequestMapping(value = "/printbarcode", method = RequestMethod.POST)
    @LoginToken
    public Message PrintBarCode(@RequestBody BarCodeInfoDTO infoDTO, HttpServletRequest request) {
        Message message = new Message();
        try {

            if (null != infoDTO && null != infoDTO.getPrintNum() && 0 != infoDTO.getPrintNum()) {
                /**
                 * 覆盖母条码的创建人,创建时间,修改人,修改时间
                 */
                LoginUser user = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
                BarCodeRequestDTO barCodeRequestDTO = new BarCodeRequestDTO();
                TransferUtil.copyProperties(barCodeRequestDTO, infoDTO);
                barCodeRequestDTO.setCompanyId(user.getCompanyId());
                barCodeRequestDTO.setShopId(user.getShopId());
                barCodeRequestDTO.setCreator(user.getName());
                barCodeRequestDTO.setModifier(user.getName());
                barCodeRequestDTO.setCreatetime(new Date());
                barCodeRequestDTO.setModifytime(new Date());
                message = bcService.PrintBarCode(barCodeRequestDTO, infoDTO);
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
