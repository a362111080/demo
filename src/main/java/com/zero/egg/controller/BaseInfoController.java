package com.zero.egg.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zero.egg.requestDTO.DeviceDataRequestDTO;
import com.zero.egg.requestDTO.EggTypeRequestDTO;
import com.zero.egg.requestDTO.StandardDataRequestDTO;
import com.zero.egg.requestDTO.StandardDetlRequestDTO;
import com.zero.egg.service.DeviceDataService;
import com.zero.egg.service.EggTypeService;
import com.zero.egg.service.StandardDataService;
import com.zero.egg.service.StandardDetlService;
import com.zero.egg.tool.Message;
import com.zero.egg.tool.UtilConstants;

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

    @Autowired
    private DeviceDataService dataService;

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


    /**
     * 列出所有方案及其细节
     *
     * @param standardDetlRequestDTO
     * @return
     */
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

    /**
     * 更新方案细节
     *
     * @param standardDetlRequestDTO
     * @return
     */
    @RequestMapping(value = "/standard/updateStandardDetl", method = RequestMethod.POST)
    public Message updateStandardDetl(@RequestBody StandardDetlRequestDTO standardDetlRequestDTO) {
        Message message = new Message();
        try {
            if (null != standardDetlRequestDTO && null != standardDetlRequestDTO.getId()) {
                message = standardDetlService.updateStandardDetl(standardDetlRequestDTO);
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
     * 批量删除方案细节(单个删除也走批量的流程)
     *
     * @param standardDetlRequestDTO
     * @return
     */
    @RequestMapping(value = "/standard/batchDeleteStandardDetl", method = RequestMethod.POST)
    public Message batchDeleteStandardDetl(@RequestBody StandardDetlRequestDTO standardDetlRequestDTO) {
        Message message = new Message();
        try {
            if (null != standardDetlRequestDTO && null != standardDetlRequestDTO.getIds()) {
                message = standardDetlService.batchDeleteStandardDetl(standardDetlRequestDTO);
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
     * 根据方案id删除方案及其方案细节
     *
     * @param standardDataRequestDTO
     * @return
     */
    @RequestMapping(value = "/standard/deletStandardDateAndDetl", method = RequestMethod.POST)
    public Message deleteStandardData(@RequestBody StandardDataRequestDTO standardDataRequestDTO) {
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
     * 新增保存设备信息
     *
     * @Param [deviceDataRequestDTO]
     * @Return com.zero.egg.tool.Message
     **/
    @RequestMapping(value = "/device/saveDeviceData", method = RequestMethod.POST)
    public Message saveDeviceData(@RequestBody DeviceDataRequestDTO deviceDataRequestDTO) {
        Message message = new Message();
        try {
            if (null != deviceDataRequestDTO && null != deviceDataRequestDTO.getStrDeviceName() && null != deviceDataRequestDTO.getStrSerialNumber() && null != deviceDataRequestDTO.getStrBindingSequence()) {
                message = dataService.saveDeviceData(deviceDataRequestDTO);
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
     * 批量删除设备信息(单个删除也走该流程)
     *
     * @Param [deviceDataRequestDTO]
     * @Return com.zero.egg.tool.Message
     **/
    @RequestMapping(value = "/device/batchDeleteDeviceData", method = RequestMethod.POST)
    public Message batchDeleteDeviceData(@RequestBody DeviceDataRequestDTO deviceDataRequestDTO) {
        Message message = new Message();
        try {
            if (null != deviceDataRequestDTO && null != deviceDataRequestDTO.getIds()) {
                message = dataService.deleteDeviceData(deviceDataRequestDTO);
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
     * 根据id更新设备信息
     *
     * @Param [deviceDataRequestDTO]
     * @Return com.zero.egg.tool.Message
     **/
    @RequestMapping(value = "/device/updateDeviceData", method = RequestMethod.POST)
    public Message updateDeviceData(@RequestBody DeviceDataRequestDTO deviceDataRequestDTO) {
        Message message = new Message();
        try {
            if (null != deviceDataRequestDTO && null != deviceDataRequestDTO.getId() && null != deviceDataRequestDTO.getStrDeviceName()
                    && null != deviceDataRequestDTO.getStrBindingSequence() && null != deviceDataRequestDTO.getStrSerialNumber()) {
                message = dataService.updateDeviceDataById(deviceDataRequestDTO);
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
     * 设备信息列表(包含根据设备名称模糊搜索)
     *
     * @Param [deviceDataRequestDTO]
     * @Return com.zero.egg.tool.Message
     **/
    @RequestMapping(value = "/device/listDeviceData", method = RequestMethod.POST)
    public Message listDeviceData(@RequestBody DeviceDataRequestDTO deviceDataRequestDTO) {
        Message message = new Message();
        try {
            if (null != deviceDataRequestDTO) {
                message = dataService.listDeviceData(deviceDataRequestDTO);
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
