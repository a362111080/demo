package com.zero.egg.controller;

import com.zero.egg.requestDTO.EggTypeRequestDTO;
import com.zero.egg.requestDTO.StandardDataRequestDTO;
import com.zero.egg.requestDTO.StandardDetlRequestDTO;
import com.zero.egg.service.EggTypeService;
import com.zero.egg.service.StandardDataService;
import com.zero.egg.service.StandardDetlService;
import com.zero.egg.tool.Message;
import com.zero.egg.tool.UtilConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName BaseInfoController
 * @Description 基础信息模块Controller
 * @Author lyming
 * @Date 2018/11/1 17:44
 **/
@RestController
@RequestMapping("/baseInfo")
public class BaseInfoController {

    @Autowired
    private EggTypeService eggTypeService;

    @Autowired
    private StandardDataService standardDataService;

    @Autowired
    private StandardDetlService standardDetlService;

    /**
     * @param saveEggTypeRequestDTO
     * @return
     * @Description 添加鸡蛋类型
     */
    @RequestMapping(value = "/variety/add", method = RequestMethod.POST)
    public Message saveEggType(@RequestBody EggTypeRequestDTO saveEggTypeRequestDTO) {
        Message message;
        try {
            /**
             * 后期如果加验证方法,这里可以省略
             */
            if (null != saveEggTypeRequestDTO && null != saveEggTypeRequestDTO.getStrEggTypeName() && null != saveEggTypeRequestDTO.getStrEggTypeCode()) {
                //应该是从session里面获取,暂时写死
                saveEggTypeRequestDTO.setStrCreateUser("老王");
                message = eggTypeService.saveEggType(saveEggTypeRequestDTO);
            } else {
                message = new Message();
                message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
                message.setMessage(UtilConstants.ResponseMsg.PARAM_MISSING);
            }
            return message;
        } catch (Exception e) {
            message = new Message();
            message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
            message.setMessage(UtilConstants.ResponseMsg.FAILED);
            return message;
        }
    }

    /**
     * @param deleteEggTypeRequestDTO
     * @return
     * @Description 删除鸡蛋类型（单个删除）
     */
    @RequestMapping(value = "/variety/delete", method = RequestMethod.POST)
    public Message deleteEggTypeById(@RequestBody EggTypeRequestDTO deleteEggTypeRequestDTO) {
        Message message = new Message();
        try {
            if (null != deleteEggTypeRequestDTO.getId()) {
                eggTypeService.deleteEggTypeById(deleteEggTypeRequestDTO);
                message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
                message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
            } else {
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

    /**
     * @Description 批量删除鸡蛋类型
     * @Param [eggTypeRequestDTO]
     * @Return java.lang.String
     **/
    @RequestMapping(value = "/variety/batchDelete", method = RequestMethod.POST)
    public Message batchDeleteEggType(@RequestBody EggTypeRequestDTO eggTypeRequestDTO) {
        Message message = new Message();
        try {
            if (null != eggTypeRequestDTO.getIds()) {
                eggTypeService.batchDeleteEggType(eggTypeRequestDTO);
                message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
                message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
            } else {
                message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
                message.setMessage(UtilConstants.ResponseMsg.ATLEAST_ONE);
            }
            return message;
        } catch (Exception e) {
            message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
            message.setMessage(UtilConstants.ResponseMsg.FAILED);
            return message;
        }
    }

    /**
     * 查询鸡蛋列表(带分页模糊查询)
     *
     * @Param [eggType]
     * @Return com.zero.egg.tool.Message
     **/
    @RequestMapping(value = "/variety/listEggType", method = RequestMethod.POST)
    public Message SelectEggTypeList(@RequestBody EggTypeRequestDTO eggTypeRequestDTO) {
        Message message = new Message();
        try {
            message = eggTypeService.listEggType(eggTypeRequestDTO);
            return message;
        } catch (Exception e) {
            message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
            message.setMessage(UtilConstants.ResponseMsg.FAILED);
            return message;
        }
    }


    /**
     * 新增方案
     *
     * @Param [standardDataRequestDTO]
     * @Return com.zero.egg.tool.Message
     **/
    @RequestMapping(value = "/standard/addStandardData", method = RequestMethod.POST)
    public Message addStandard(@RequestBody StandardDataRequestDTO standardDataRequestDTO) {
        Message message = new Message();
        try {
            if (null != standardDataRequestDTO && null != standardDataRequestDTO.getStrStandName() && null != standardDataRequestDTO.getStrStandCode()) {
                message = standardDataService.addStandardData(standardDataRequestDTO);
            } else {
                message = new Message();
                message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
                message.setMessage(UtilConstants.ResponseMsg.PARAM_MISSING);
            }
            return message;
        } catch (Exception e) {
            message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
            message.setMessage(UtilConstants.ResponseMsg.FAILED);
            return message;
        }
    }

    /**
     * 删除方案
     *
     * @Param [standardDataRequestDTO]
     * @Return com.zero.egg.tool.Message
     **/
    @RequestMapping(value = "/standard/deleteStandardData", method = RequestMethod.POST)
    public Message deleteStandard(@RequestBody StandardDataRequestDTO standardDataRequestDTO) {
        Message message = new Message();
        try {
            if (null != standardDataRequestDTO && null != standardDataRequestDTO.getId()) {
                message = standardDataService.deleteStandardDataById(standardDataRequestDTO);
            } else {
                message = new Message();
                message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
                message.setMessage(UtilConstants.ResponseMsg.PARAM_MISSING);
            }
            return message;
        } catch (Exception e) {
            message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
            message.setMessage(UtilConstants.ResponseMsg.FAILED);
            return message;
        }
    }

    /**
     * 列出所有方案及其细节
     *
     * @Param [standardDataRequestDTO]
     * @Return com.zero.egg.tool.Message
     **/
    @RequestMapping(value = "/standard/listStandarddDataAndDetl", method = RequestMethod.POST)
    public Message listStandard(@RequestBody StandardDataRequestDTO standardDataRequestDTO) {
        Message message = new Message();
        try {
            if (null != standardDataRequestDTO && null != standardDataRequestDTO.getStrEggTypeCode()) {
                message = standardDataService.listDataAndDetl(standardDataRequestDTO);
            } else {
                message = new Message();
                message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
                message.setMessage(UtilConstants.ResponseMsg.PARAM_MISSING);
            }
            return message;
        } catch (Exception e) {
            message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
            message.setMessage(UtilConstants.ResponseMsg.FAILED);
            return message;
        }
    }

    /**
     * 添加一条方案细节
     *
     * @Param [standardDetlRequestDTO]
     * @Return com.zero.egg.tool.Message
     **/
    @RequestMapping(value = "/standard/addStandardDetl", method = RequestMethod.POST)
    public Message addStandardDetl(@RequestBody StandardDetlRequestDTO standardDetlRequestDTO) {
        Message message = new Message();
        try {
            if (null != standardDetlRequestDTO && null != standardDetlRequestDTO.getStrStandCode() && null != standardDetlRequestDTO.getStrStandDetlCode()) {
                message = standardDetlService.addStandardDetl(standardDetlRequestDTO);
            } else {
                message = new Message();
                message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
                message.setMessage(UtilConstants.ResponseMsg.PARAM_MISSING);
            }
            return message;
        } catch (Exception e) {
            message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
            message.setMessage(UtilConstants.ResponseMsg.FAILED);
            return message;
        }
    }


    @RequestMapping(value = "/standard/listStandardDetel", method = RequestMethod.POST)
    public Message listStandardDetel(@RequestBody StandardDetlRequestDTO standardDetlRequestDTO) {
        Message message = new Message();
        try {
            if (null != standardDetlRequestDTO && null != standardDetlRequestDTO.getStrStandCode()) {
                message = standardDetlService.listStandardDetlByStandDetlCode(standardDetlRequestDTO);
            } else {
                message = new Message();
                message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
                message.setMessage(UtilConstants.ResponseMsg.PARAM_MISSING);
            }
            return message;
        } catch (Exception e) {
            message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
            message.setMessage(UtilConstants.ResponseMsg.FAILED);
            return message;
        }
    }


}
