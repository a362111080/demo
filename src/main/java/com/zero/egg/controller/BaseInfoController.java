package com.zero.egg.controller;

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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@RequestMapping("/baseinfo")
@Api(value = "基础设置")
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
    @ApiOperation(value = "添加鸡蛋类型")
    @RequestMapping(value = "/variety/add", method = RequestMethod.POST)
    public Message saveEggType(@RequestBody EggTypeRequestDTO saveEggTypeRequestDTO) {
        Message message = null;
        try {
            /**
             * 后期如果加验证方法,这里可以省略
             */
            if (null != saveEggTypeRequestDTO && null != saveEggTypeRequestDTO.getName()
                    && checkShopAndEnterpriseExist(saveEggTypeRequestDTO)) {
                //应该是从session里面获取,暂时从依赖前端
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

    @ApiOperation(value = "修改鸡蛋类型")
    @RequestMapping(value = "/variety/modify", method = RequestMethod.POST)
    public Message modifyEggType(@RequestBody EggTypeRequestDTO modifyEggTypeRequestDTO) {
        Message message = null;
        try {
            /**
             * 后期如果加验证方法,这里可以省略
             */
            if (null != modifyEggTypeRequestDTO && null != modifyEggTypeRequestDTO.getName()
                    && checkShopAndEnterpriseExist(modifyEggTypeRequestDTO)) {
                //应该是从session里面获取,暂时从依赖前端
                message = eggTypeService.modifyEggType(modifyEggTypeRequestDTO);
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
    @ApiOperation(value = "删除鸡蛋类型（单个删除）")
    @RequestMapping(value = "/variety/delete", method = RequestMethod.POST)
    public Message deleteEggTypeById(@RequestBody EggTypeRequestDTO deleteEggTypeRequestDTO) {
        Message message = new Message();
        /** 从session中获取shopId和enterpriseId,暂时写死 */
        deleteEggTypeRequestDTO.setShopId("1");
        deleteEggTypeRequestDTO.setCompanyId("1");
        try {
            if (null != deleteEggTypeRequestDTO.getId() && checkShopAndEnterpriseExist(deleteEggTypeRequestDTO)) {
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
    @ApiOperation(value = "批量删除鸡蛋类型")
    @RequestMapping(value = "/variety/batchdelete", method = RequestMethod.POST)
    public Message batchDeleteEggType(@RequestBody EggTypeRequestDTO eggTypeRequestDTO) {
        Message message = new Message();
        /** 从session中获取shopId和enterpriseId,暂时写死 */
        eggTypeRequestDTO.setShopId("1");
        eggTypeRequestDTO.setCompanyId("1");
        try {
            if (null != eggTypeRequestDTO.getIds() && checkShopAndEnterpriseExist(eggTypeRequestDTO)) {
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

    @ApiOperation(value = "按主键查询品种接口")
    @RequestMapping(value = "/variety/queryeggtypebyid", method = RequestMethod.POST)
    public Message queryEggTypeById(@RequestBody EggTypeRequestDTO eggTypeRequestDTO) {
        Message message = null;
        /** 从session中获取shopId和enterpriseId,暂时写死 */
        eggTypeRequestDTO.setShopId("1");
        eggTypeRequestDTO.setCompanyId("1");
        try {
            if (null != eggTypeRequestDTO.getId() && checkShopAndEnterpriseExist(eggTypeRequestDTO)) {
                message = eggTypeService.selectEggTypeById(eggTypeRequestDTO);
            } else {
                message = new Message();
                message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
                message.setMessage(UtilConstants.ResponseMsg.FAILED);
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
     * 查询鸡蛋列表(带分页模糊查询)
     *
     * @Param [eggType]
     * @Return com.zero.egg.tool.Message
     **/
    @ApiOperation(value = "查询鸡蛋列表(带分页模糊查询)")
    @RequestMapping(value = "/variety/listeggtype", method = RequestMethod.POST)
    public Message SelectEggTypeList(@RequestBody EggTypeRequestDTO eggTypeRequestDTO) {
        Message message = null;
        /** 从session中获取shopId和enterpriseId,暂时写死 */
        eggTypeRequestDTO.setShopId("1");
        eggTypeRequestDTO.setCompanyId("1");
        try {
            message = eggTypeService.listEggType(eggTypeRequestDTO);
            return message;
        } catch (Exception e) {
            message = new Message();
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
    @ApiOperation(value = "新增方案")
    @RequestMapping(value = "/standard/addstandarddata", method = RequestMethod.POST)
    public Message addStandard(@RequestBody StandardDataRequestDTO standardDataRequestDTO) {
        Message message = null;
        /** 从session中获取shopId和enterpriseId,暂时写死 */
        standardDataRequestDTO.setShopId("1");
        standardDataRequestDTO.setEnterpriseId("1");
        try {
            if (null != standardDataRequestDTO && null != standardDataRequestDTO.getStrStandName()
                    && checkShopAndEnterpriseExist(standardDataRequestDTO)) {
                message = standardDataService.addStandardData(standardDataRequestDTO);
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
     * 修改方案名
     *
     * @param standardDataRequestDTO
     * @return
     */
    @ApiOperation(value = "修改方案名")
    @RequestMapping(value = "/standard/updatestandard", method = RequestMethod.POST)
    public Message updateStandard(@RequestBody StandardDataRequestDTO standardDataRequestDTO) {
        Message message = null;

        return message;
    }

    /**
     * 根据品种id列出所有方案及其细节
     *
     * @Param [standardDataRequestDTO]
     * @Return com.zero.egg.tool.Message
     **/
    @ApiOperation(value = "根据品种id列出所有方案及其细节")
    @RequestMapping(value = "/standard/liststandardddataanddetl", method = RequestMethod.POST)
    public Message listStandard(@RequestBody StandardDataRequestDTO standardDataRequestDTO) {
        Message message = new Message();
        try {
            if (null != standardDataRequestDTO) {
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
    @ApiOperation(value = "添加一条方案细节")
    @RequestMapping(value = "/standard/addstandarddetl", method = RequestMethod.POST)
    public Message addStandardDetl(@RequestBody StandardDetlRequestDTO standardDetlRequestDTO) {
        Message message = new Message();
        try {
            if (null != standardDetlRequestDTO && null != standardDetlRequestDTO.getStrStandId()) {
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
     * 根据方案id列出其下方案细节
     *
     * @param standardDetlRequestDTO
     * @return
     */
    @ApiOperation(value = "根据方案id列出其下方案细节")
    @RequestMapping(value = "/standard/liststandarddetel", method = RequestMethod.POST)
    public Message listStandardDetel(@RequestBody StandardDetlRequestDTO standardDetlRequestDTO) {
        Message message = new Message();
        try {
            if (null != standardDetlRequestDTO && null != standardDetlRequestDTO.getStrStandId()) {
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
    @ApiOperation(value = "更新方案细节")
    @RequestMapping(value = "/standard/updatestandarddetl", method = RequestMethod.POST)
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
    @ApiOperation(value = "批量删除方案细节(单个删除也走批量的流程)")
    @RequestMapping(value = "/standard/batchdeletestandarddetl", method = RequestMethod.POST)
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
    @ApiOperation(value = "根据方案id删除方案及其方案细节")
    @RequestMapping(value = "/standard/deletstandarddateanddetl", method = RequestMethod.POST)
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
    @ApiOperation(value = "新增保存设备信息")
    @RequestMapping(value = "/device/savedevicedata", method = RequestMethod.POST)
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
    @ApiOperation(value = "批量删除设备信息(单个删除也走该流程)")
    @RequestMapping(value = "/device/batchdeletedevicedata", method = RequestMethod.POST)
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
    @ApiOperation(value = "根据id更新设备信息")
    @RequestMapping(value = "/device/updatedevicedata", method = RequestMethod.POST)
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
    @ApiOperation(value = "设备信息列表(包含根据设备名称模糊搜索)")
    @RequestMapping(value = "/device/listdevicedata", method = RequestMethod.POST)
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

    /**
     * 检验shopId和enterpriseId是否为空
     *
     * @param eggTypeRequestDTO 鸡蛋类型Request
     * @return
     */
    private boolean checkShopAndEnterpriseExist(EggTypeRequestDTO eggTypeRequestDTO) {
        return (null != eggTypeRequestDTO.getShopId() && null != eggTypeRequestDTO.getCompanyId()) ? true : false;
    }

    /**
     * 检验shopId和enterpriseId是否为空
     *
     * @param standardDataRequestDTO 方案信息Request
     * @return
     */
    private boolean checkShopAndEnterpriseExist(StandardDataRequestDTO standardDataRequestDTO) {
        return (null != standardDataRequestDTO.getShopId() && null != standardDataRequestDTO.getEnterpriseId()) ? true : false;
    }

}
