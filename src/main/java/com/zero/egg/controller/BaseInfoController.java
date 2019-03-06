package com.zero.egg.controller;

import com.zero.egg.requestDTO.CategoryRequestDTO;
import com.zero.egg.requestDTO.DeviceDataRequestDTO;
import com.zero.egg.requestDTO.SpecificationProgramRequestDTO;
import com.zero.egg.requestDTO.SpecificationRequestDTO;
import com.zero.egg.service.CategoryService;
import com.zero.egg.service.DeviceDataService;
import com.zero.egg.service.SpecificationProgramService;
import com.zero.egg.service.SpecificationService;
import com.zero.egg.tool.Message;
import com.zero.egg.tool.UtilConstants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

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
    private CategoryService categoryService;

    @Autowired
    private SpecificationProgramService specificationProgramService;

    @Autowired
    private SpecificationService specificationService;

    @Autowired
    private DeviceDataService dataService;

    /**
     * @param saveCategoryRequestDTO
     * @return
     * @Description 添加鸡蛋类型
     */
    @ApiOperation(value = "添加鸡蛋类型")
    @RequestMapping(value = "/variety/add", method = RequestMethod.POST)
    public Message saveEggType(@RequestBody CategoryRequestDTO saveCategoryRequestDTO) {
        Message message = null;
        try {
            /**
             * 后期如果加验证方法,这里可以省略
             */
            if (null != saveCategoryRequestDTO && null != saveCategoryRequestDTO.getName()
                    && checkShopAndCompanyExist(saveCategoryRequestDTO)) {
                //应该是从session里面获取,暂时从依赖前端
                saveCategoryRequestDTO.setCreator("laowang");
                saveCategoryRequestDTO.setModifier("laowang");
                message = categoryService.saveEggType(saveCategoryRequestDTO);
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
    public Message modifyEggType(@RequestBody CategoryRequestDTO modifyCategoryRequestDTO) {
        Message message = null;
        try {
            /**
             * 后期如果加验证方法,这里可以省略
             */
            if (null != modifyCategoryRequestDTO && null != modifyCategoryRequestDTO.getName()
                    && checkShopAndCompanyExist(modifyCategoryRequestDTO)) {
                //应该是从session里面获取,暂时从依赖前端
                modifyCategoryRequestDTO.setModifier("laoli");
                modifyCategoryRequestDTO.setModifytime(new Date());
                message = categoryService.modifyEggType(modifyCategoryRequestDTO);
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
     * @param deleteCategoryRequestDTO
     * @return
     * @Description 删除鸡蛋类型（单个删除）
     */
    @ApiOperation(value = "删除鸡蛋类型（单个删除）")
    @RequestMapping(value = "/variety/delete", method = RequestMethod.POST)
    public Message deleteEggTypeById(@RequestBody CategoryRequestDTO deleteCategoryRequestDTO) {
        Message message = new Message();
        /** 从session中获取shopId和enterpriseId,暂时写死 */
        deleteCategoryRequestDTO.setShopId("1");
        deleteCategoryRequestDTO.setCompanyId("1");
        try {
            if (null != deleteCategoryRequestDTO.getId() && checkShopAndCompanyExist(deleteCategoryRequestDTO)) {
                categoryService.deleteEggTypeById(deleteCategoryRequestDTO);
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
     * @Param [categoryRequestDTO]
     * @Return java.lang.String
     **/
    @ApiOperation(value = "批量删除鸡蛋类型")
    @RequestMapping(value = "/variety/batchdelete", method = RequestMethod.POST)
    public Message batchDeleteEggType(@RequestBody CategoryRequestDTO categoryRequestDTO) {
        Message message = new Message();
        /** 从session中获取shopId和enterpriseId,暂时写死 */
        categoryRequestDTO.setShopId("1");
        categoryRequestDTO.setCompanyId("1");
        try {
            if (null != categoryRequestDTO.getIds() && checkShopAndCompanyExist(categoryRequestDTO)) {
                categoryService.batchDeleteEggType(categoryRequestDTO);
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
    public Message queryEggTypeById(@RequestBody CategoryRequestDTO categoryRequestDTO) {
        Message message = null;
        /** 从session中获取shopId和enterpriseId,暂时写死 */
        categoryRequestDTO.setShopId("1");
        categoryRequestDTO.setCompanyId("1");
        try {
            if (null != categoryRequestDTO.getId() && checkShopAndCompanyExist(categoryRequestDTO)) {
                message = categoryService.selectEggTypeById(categoryRequestDTO);
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
    public Message SelectEggTypeList(@RequestBody CategoryRequestDTO categoryRequestDTO) {
        Message message = null;
        /** 从session中获取shopId和enterpriseId,暂时写死 */
        categoryRequestDTO.setShopId("1");
        categoryRequestDTO.setCompanyId("1");
        try {
            message = categoryService.listEggType(categoryRequestDTO);
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
     * @Param [specificationProgramRequestDTO]
     * @Return com.zero.egg.tool.Message
     **/
    @ApiOperation(value = "新增方案")
    @RequestMapping(value = "/standard/addstandarddata", method = RequestMethod.POST)
    public Message addStandard(@RequestBody SpecificationProgramRequestDTO specificationProgramRequestDTO) {
        Message message = null;
        /** 从session中获取shopId和companyId,暂时写死 */
        specificationProgramRequestDTO.setShopId("1");
        specificationProgramRequestDTO.setCompanyId("1");
        specificationProgramRequestDTO.setCreator("laowang");
        specificationProgramRequestDTO.setModifier("laowang");
        try {
            if (null != specificationProgramRequestDTO && null != specificationProgramRequestDTO.getName()
                    && checkShopAndCompanyExist(specificationProgramRequestDTO)) {
                message = specificationProgramService.addStandardData(specificationProgramRequestDTO);
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
     * @param specificationProgramRequestDTO
     * @return
     */
    @ApiOperation(value = "修改方案名")
    @RequestMapping(value = "/standard/updatespecificationprogram", method = RequestMethod.POST)
    public Message updateStandard(@RequestBody SpecificationProgramRequestDTO specificationProgramRequestDTO) {
        Message message = null;
        /** 从session中获取shopId和companyId,暂时写死 */
        specificationProgramRequestDTO.setShopId("1");
        specificationProgramRequestDTO.setCompanyId("1");
        specificationProgramRequestDTO.setModifier("laowang3333");
        specificationProgramRequestDTO.setModifytime(new Date());
        try {
            if (null != specificationProgramRequestDTO) {
                message = specificationProgramService.updateSpecificationProgram(specificationProgramRequestDTO);
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
     * 根据品种id列出所有方案及其细节
     *
     * @Param [specificationProgramRequestDTO]
     * @Return com.zero.egg.tool.Message
     **/
    @ApiOperation(value = "根据品种id列出所有方案及其细节")
    @RequestMapping(value = "/standard/liststandardddataanddetl", method = RequestMethod.POST)
    public Message listStandard(@RequestBody SpecificationProgramRequestDTO specificationProgramRequestDTO) {
        Message message = new Message();
        try {
            if (null != specificationProgramRequestDTO) {
                message = specificationProgramService.listDataAndDetl(specificationProgramRequestDTO);
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
     * @Param [specificationRequestDTO]
     * @Return com.zero.egg.tool.Message
     **/
    @ApiOperation(value = "添加一条方案细节")
    @RequestMapping(value = "/standard/addstandarddetl", method = RequestMethod.POST)
    public Message addStandardDetl(@RequestBody SpecificationRequestDTO specificationRequestDTO) {
        Message message = new Message();
        /** 从session中获取shopId和companyId,暂时写死 */
        specificationRequestDTO.setShopId("1");
        specificationRequestDTO.setCompanyId("1");
        specificationRequestDTO.setCreator("laowang");
        specificationRequestDTO.setModifier("laowang");
        try {
            if (null != specificationRequestDTO && null != specificationRequestDTO.getProgramId()) {
                message = specificationService.addStandardDetl(specificationRequestDTO);
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
     * @param specificationRequestDTO
     * @return
     */
    @ApiOperation(value = "根据方案id列出其下方案细节")
    @RequestMapping(value = "/standard/liststandarddetel", method = RequestMethod.POST)
    public Message listStandardDetel(@RequestBody SpecificationRequestDTO specificationRequestDTO) {
        Message message = new Message();
        try {
            if (null != specificationRequestDTO && null != specificationRequestDTO.getProgramId()) {
                message = specificationService.listStandardDetlByProgramId(specificationRequestDTO);
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
     * @param specificationRequestDTO
     * @return
     */
    @ApiOperation(value = "更新方案细节")
    @RequestMapping(value = "/standard/updatestandarddetl", method = RequestMethod.POST)
    public Message updateStandardDetl(@RequestBody SpecificationRequestDTO specificationRequestDTO) {
        Message message = new Message();
        /** 从session中获取shopId和companyId,暂时写死 */
        specificationRequestDTO.setShopId("1");
        specificationRequestDTO.setCompanyId("1");
        specificationRequestDTO.setModifier("laowang222");
        specificationRequestDTO.setModifytime(new Date());
        try {
            if (null != specificationRequestDTO && null != specificationRequestDTO.getId()) {
                message = specificationService.updateStandardDetl(specificationRequestDTO);
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
     * @param specificationRequestDTO
     * @return
     */
    @ApiOperation(value = "批量删除方案细节(单个删除也走批量的流程)")
    @RequestMapping(value = "/standard/batchdeletestandarddetl", method = RequestMethod.POST)
    public Message batchDeleteStandardDetl(@RequestBody SpecificationRequestDTO specificationRequestDTO) {
        Message message = new Message();
        try {
            if (null != specificationRequestDTO && null != specificationRequestDTO.getIds()) {
                message = specificationService.batchDeleteStandardDetl(specificationRequestDTO);
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
     * @param specificationProgramRequestDTO
     * @return
     */
    @ApiOperation(value = "根据方案id删除方案及其方案细节")
    @RequestMapping(value = "/standard/deletstandarddateanddetl", method = RequestMethod.POST)
    public Message deleteStandardData(@RequestBody SpecificationProgramRequestDTO specificationProgramRequestDTO) {
        Message message = new Message();
        try {
            if (null != specificationProgramRequestDTO && null != specificationProgramRequestDTO.getId()) {
                message = specificationProgramService.deleteStandardDataById(specificationProgramRequestDTO);
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
     * 检验shopId和companyId是否为空
     *
     * @param categoryRequestDTO 鸡蛋类型Request
     * @return
     */
    private boolean checkShopAndCompanyExist(CategoryRequestDTO categoryRequestDTO) {
        return (null != categoryRequestDTO.getShopId() && null != categoryRequestDTO.getCompanyId()) ? true : false;
    }

    /**
     * 检验shopId和companyId是否为空
     *
     * @param specificationProgramRequestDTO 方案信息Request
     * @return
     */
    private boolean checkShopAndCompanyExist(SpecificationProgramRequestDTO specificationProgramRequestDTO) {
        return (null != specificationProgramRequestDTO.getShopId() && null != specificationProgramRequestDTO.getCompanyId()) ? true : false;
    }

}
