package com.zero.egg.controller;

import com.zero.egg.annotation.LoginToken;
import com.zero.egg.api.ApiConstants;
import com.zero.egg.requestDTO.CategoryRequestDTO;
import com.zero.egg.requestDTO.DeviceDataRequestDTO;
import com.zero.egg.requestDTO.LoginUser;
import com.zero.egg.requestDTO.SaveSpecificationRequestDTO;
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
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

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

    @Autowired
    private HttpServletRequest request;

    /**
     * @param saveCategoryRequestDTO
     * @return
     * @Description 添加鸡蛋类型
     */
    @ApiOperation(value = "添加鸡蛋类型", notes = "{\"name\":\"红鸡蛋\"}")
    @RequestMapping(value = "/variety/add", method = RequestMethod.POST)
    @LoginToken
    public Message saveEggType(@RequestBody @ApiParam(required = true, name = "saveCategoryRequestDTO",
            value = "1.鸡蛋类别名称") CategoryRequestDTO saveCategoryRequestDTO) {
        Message message = null;
        LoginUser user = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
        try {
            /**
             * 后期如果加验证方法,这里可以省略
             */
            if (null != saveCategoryRequestDTO && null != saveCategoryRequestDTO.getName()
                    && checkShopAndCompanyExist(user, saveCategoryRequestDTO)) {
                saveCategoryRequestDTO.setCreator(user.getName());
                saveCategoryRequestDTO.setCreatetime(new Date());
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

    @ApiOperation(value = "修改品种信息", notes = "{\"id\":\"\",\"name\":\"\"}")
    @RequestMapping(value = "/variety/modify", method = RequestMethod.POST)
    @LoginToken
    public Message modifyEggType(@RequestBody @ApiParam(required = true,
            name = "modifyCategoryRequestDTO",
            value = "1.鸡蛋类别名称 4.主键") CategoryRequestDTO modifyCategoryRequestDTO) {
        Message message = null;
        LoginUser user = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
        try {
            /**
             * 后期如果加验证方法,这里可以省略
             */
            if (null != modifyCategoryRequestDTO && null != modifyCategoryRequestDTO.getName()
                    && checkShopAndCompanyExist(user, modifyCategoryRequestDTO)) {
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
    @ApiOperation(value = "单个删除品种", notes = "{\"id\":\"\"}")
    @RequestMapping(value = "/variety/delete", method = RequestMethod.POST)
    @LoginToken
    public Message deleteEggTypeById(@RequestBody @ApiParam(required = true,
            name = "deleteCategoryRequestDTO",
            value = "1.主键") CategoryRequestDTO deleteCategoryRequestDTO) {
        Message message = new Message();
        LoginUser user = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
        try {
            if (null != deleteCategoryRequestDTO.getId() && checkShopAndCompanyExist(user, deleteCategoryRequestDTO)) {
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
    @ApiOperation(value = "批量删除鸡蛋类型", notes = "{\"ids\":[\"\"]}")
    @RequestMapping(value = "/variety/batchdelete", method = RequestMethod.POST)
    @LoginToken
    public Message batchDeleteEggType(@RequestBody @ApiParam(required = true,
            name = "categoryRequestDTO",
            value = "1.前端传来的类别id数组(批量操作)") CategoryRequestDTO categoryRequestDTO) {
        Message message = new Message();
        LoginUser user = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
        try {
            if (null != categoryRequestDTO.getIds() && checkShopAndCompanyExist(user, categoryRequestDTO)) {
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

    @ApiOperation(value = "按主键查询品种接口", notes = "{\"id\":\"\"}")
    @RequestMapping(value = "/variety/queryeggtypebyid", method = RequestMethod.POST)
    @LoginToken
    public Message queryEggTypeById(@RequestBody @ApiParam(required = true,
            name = "categoryRequestDTO",
            value = "1.主键") CategoryRequestDTO categoryRequestDTO) {
        Message message = null;
        LoginUser user = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
        try {
            if (null != categoryRequestDTO.getId() && checkShopAndCompanyExist(user, categoryRequestDTO)) {
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
    @ApiOperation(value = "查询鸡蛋列表(带分页模糊查询)",
            notes = "{\"name\":\"null\",\"current\":\"1\",\"size\":\"10\"}")
    @RequestMapping(value = "/variety/listeggtype", method = RequestMethod.POST)
    @LoginToken
    public Message SelectEggTypeList(@RequestBody @ApiParam(required = true,
            name = "categoryRequestDTO",
            value = " 1.鸡蛋类别名称(可选,默认null) 2.当前页(默认1) 3.每页数量(默认10)") CategoryRequestDTO categoryRequestDTO) {
        Message message = null;
        LoginUser user = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
        try {
            if (checkShopAndCompanyExist(user, categoryRequestDTO)) {
                message = categoryService.listEggType(categoryRequestDTO);
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
     * 新增方案
     *
     * @Param [specificationProgramRequestDTO]
     * @Return com.zero.egg.tool.Message
     **/
    @ApiOperation(value = "新增方案", notes = "{\"name\":\"\",\"categoryId\":\"\"}")
    @RequestMapping(value = "/standard/addstandarddata", method = RequestMethod.POST)
    @LoginToken
    public Message addStandard(@RequestBody @ApiParam(required = true,
            name = "specificationProgramRequestDTO",
            value = " 1.品种id 2.方案名称") SpecificationProgramRequestDTO specificationProgramRequestDTO) {
        Message message = null;
        LoginUser user = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
        try {
            if (null != specificationProgramRequestDTO && null != specificationProgramRequestDTO.getName()
                    && null != specificationProgramRequestDTO.getCategoryId()
                    && checkShopAndCompanyExist(user, specificationProgramRequestDTO)) {
                specificationProgramRequestDTO.setCreator(user.getName());
                specificationProgramRequestDTO.setCreatetime(new Date());
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
    @LoginToken
    public Message updateStandard(@RequestBody @ApiParam(required = true,
            name = "specificationProgramRequestDTO",
            value = " 1.品种id 2.方案名称") SpecificationProgramRequestDTO specificationProgramRequestDTO) {
        Message message = null;
        LoginUser user = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
        try {
            if (null != specificationProgramRequestDTO
                    && null != specificationProgramRequestDTO.getName()
                    && null != specificationProgramRequestDTO.getCategoryId()
                    && checkShopAndCompanyExist(user, specificationProgramRequestDTO)) {
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
    @ApiOperation(value = "根据品种id列出所有方案及其细节", notes = "{\"categoryId\":\"\"}")
    @RequestMapping(value = "/standard/liststandardddataanddetl", method = RequestMethod.POST)
    @LoginToken
    public Message listStandard(@RequestBody @ApiParam(required = true,
            name = "categoryRequestDTO",
            value = "1.品种id") SpecificationProgramRequestDTO specificationProgramRequestDTO) {
        Message message = new Message();
        LoginUser user = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
        try {
            if (null != specificationProgramRequestDTO && null != specificationProgramRequestDTO.getCategoryId()
                    && checkShopAndCompanyExist(user, specificationProgramRequestDTO)) {
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
     * 根据品种id列出所有方案(下拉列表用)
     *
     * @Param [specificationProgramRequestDTO]
     * @Return com.zero.egg.tool.Message
     **/
    @ApiOperation(value = "根据品种id列出所有方案(下拉列表用)", notes = "{\"categoryId\":\"\"}")
    @RequestMapping(value = "/standard/liststandard", method = RequestMethod.POST)
    @LoginToken
    public Message listStandardForTables(@RequestBody @ApiParam(required = true,
            name = "categoryRequestDTO",
            value = "1.品种id") SpecificationProgramRequestDTO specificationProgramRequestDTO) {
        Message message = new Message();
        LoginUser user = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
        try {
            if (null != specificationProgramRequestDTO && null != specificationProgramRequestDTO.getCategoryId()
                    && checkShopAndCompanyExist(user, specificationProgramRequestDTO)) {
                message = specificationProgramService.listData(specificationProgramRequestDTO);
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
    @ApiOperation(value = "添加一条方案细节", notes = "{\"programId\":\"\",\"weightMin\":0,\"weightMax\":0," +
            "\"marker\":\"\",\"mode\":0,\"numerical\":0,\"warn\":0,\"dr\":\"0\"}")
    @RequestMapping(value = "/standard/addstandarddetl", method = RequestMethod.POST)
    @LoginToken
    public Message addStandardDetl(@RequestBody @ApiParam(required = true,
            name = "specificationRequestDTO",
            value = " 1.方案id " +
                    "2.判定最小称重 3.判定最大称重 4.标记 5.是否预警(0:否,1:是) " +
                    "6.计重方式(1:去皮 2:包) 7(非必传).删除标识 0:未删除(默认) 1:已删除 " +
                    "8(非必传).数值（去皮对应的数值,计重方式为包是不传）") SpecificationRequestDTO specificationRequestDTO) {
        Message message = new Message();
        LoginUser user = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
        try {
            if (null != specificationRequestDTO && null != specificationRequestDTO.getProgramId()
                    && checkShopAndCompanyExist(user, specificationRequestDTO)) {
                specificationRequestDTO.setCreator(user.getName());
                specificationRequestDTO.setModifytime(new Date());
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
    @ApiOperation(value = "根据方案id列出其下方案细节", notes = "{\"programId\":\"\"}")
    @RequestMapping(value = "/standard/liststandarddetel", method = RequestMethod.POST)
    @LoginToken
    public Message listStandardDetel(@RequestBody @ApiParam(required = true,
            name = "specificationRequestDTO",
            value = "1.方案id") SpecificationRequestDTO specificationRequestDTO) {
        Message message = new Message();
        LoginUser user = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
        try {
            if (null != specificationRequestDTO && null != specificationRequestDTO.getProgramId()
                    && checkShopAndCompanyExist(user, specificationRequestDTO)) {
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
    @ApiOperation(value = "更新方案细节", notes = "{\"id\":\"\",\"programId\":\"\",\"weightMin\":0,\"weightMax\":0,\"ma" +
            "rker\":\"\",\"mode\":0,\"numerical\":\"\",\"warn\":0,\"dr\":\"\",\"shopId\":\"\",\"companyId\":\"\"}")
    @RequestMapping(value = "/standard/updatestandarddetl", method = RequestMethod.POST)
    @LoginToken
    public Message updateStandardDetl(@RequestBody @ApiParam(required = true,
            name = "specificationRequestDTO",
            value = "1.所属店铺id 2.店铺所属企业id 3.方案id " +
                    "4.判定最小称重 5.判定最大称重 6.标记 7.是否预警(0:否,1:是) " +
                    "8.计重方式(1:去皮 2:包) 9.主键 10(非必传).删除标识 0:未删除(默认) 1:已删除 " +
                    "11(非必传).数值（去皮对应的数值,计重方式为包是不传）") SpecificationRequestDTO specificationRequestDTO) {
        Message message = new Message();
        LoginUser user = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
        try {
            if (null != specificationRequestDTO && null != specificationRequestDTO.getId()
                    && checkShopAndCompanyExist(user, specificationRequestDTO)) {
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
    @ApiOperation(value = "批量删除方案细节(单个删除也走批量的流程)", notes = "{\"ids\":[\"\"]}")
    @RequestMapping(value = "/standard/batchdeletestandarddetl", method = RequestMethod.POST)
    @LoginToken
    public Message batchDeleteStandardDetl(@RequestBody @ApiParam(required = true,
            name = "specificationRequestDTO",
            value = "1.前端传来的方案细节id数组(批量操作)") SpecificationRequestDTO specificationRequestDTO) {
        Message message = new Message();
        LoginUser user = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
        try {
            if (null != specificationRequestDTO && null != specificationRequestDTO.getIds()
                    && checkShopAndCompanyExist(user, specificationRequestDTO)) {
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
    @ApiOperation(value = "根据方案id删除方案及其方案细节", notes = "{\"id\":\"\"}")
    @RequestMapping(value = "/standard/deletstandarddateanddetl", method = RequestMethod.POST)
    @LoginToken
    public Message deleteStandardData(@RequestBody @ApiParam(required = true,
            name = "specificationProgramRequestDTO",
            value = "1.主键") SpecificationProgramRequestDTO specificationProgramRequestDTO) {
        Message message = new Message();
        LoginUser user = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
        try {
            if (null != specificationProgramRequestDTO && null != specificationProgramRequestDTO.getId()
                    && checkShopAndCompanyExist(user, specificationProgramRequestDTO)) {
                message = specificationProgramService.deleteStandardDataById(specificationProgramRequestDTO);
            } else {
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
     * 保存方案细节(包括新增和修改)
     *
     * @return
     */
    @RequestMapping(value = "/standard/savestandarddetl", method = RequestMethod.POST)
    @LoginToken
    @ApiOperation(value = "保存方案细节(包括新增和修改)", notes = "{\"specificationRequestDTOS\":[{\"id\":" +
            "\"6d68494a6235753e635b58f005d594d2\",\"programId\":\"e61177ee779e4548b0c305d577232d57\",\"weightMin\":10," +
            "\"weightMax\":20,\"marker\":\"4811111\",\"mode\":2,\"numerical\":null,\"warn\":1},{\"programId\":" +
            "\"e61177ee779e4548b0c305d577232d57\",\"weightMin\":10,\"weightMax\":20,\"marker\":\"4811111\",\"mode\":2,\"" +
            "numerical\":null,\"warn\":1}]}")
    public Message saveSpecification(@RequestBody @ApiParam(required = true,
            name = "saveSpecificationRequestDTO",
            value = "1.方案id " +
                    "2.判定最小称重 3.判定最大称重 4.标记 5.是否预警(0:否,1:是) " +
                    "6.计重方式(1:去皮 2:包) 7.主键(非必传,传了走修改,不传走新增) 8.数值（去皮对应的数值,计重方式为包是不传）")
                                             SaveSpecificationRequestDTO saveSpecificationRequestDTO) {
        Message message = new Message();
        LoginUser user = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
        try {
            //空值判断
            if (null != saveSpecificationRequestDTO && null != saveSpecificationRequestDTO.getSpecificationRequestDTOS()
                    && !saveSpecificationRequestDTO.getSpecificationRequestDTOS().isEmpty()
                    && null != user.getCompanyId() && null != user.getShopId()) {
                String name = user.getName();
                /* 迭代方案细节列表,将店铺id和企业id赋予给方案细节,并判断方案细节的id是否为空,为空走新增流程,否则走编辑 */
                List<SpecificationRequestDTO> dtoList = saveSpecificationRequestDTO.getSpecificationRequestDTOS();
                for (SpecificationRequestDTO requestDTO : dtoList) {
                    requestDTO.setCompanyId(user.getCompanyId());
                    requestDTO.setShopId(user.getShopId());
                    requestDTO.setModifier(name);
                    if (null != requestDTO.getId() && !"".equals(requestDTO.getId())) {
                        //编辑
                        specificationService.updateStandardDetl(requestDTO);
                    } else {
                        requestDTO.setCreator(name);
                        specificationService.addStandardDetl(requestDTO);
                    }
                }
                message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
                message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
            } else {
                message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
                message.setMessage(UtilConstants.ResponseMsg.PARAM_MISSING);
            }
            return message;
        } catch (Exception e) {
            message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
            message.setMessage(e.toString());
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
    @LoginToken
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
    @LoginToken
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
    @LoginToken
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
    @LoginToken
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
     * 从当前登录用户信息中检查shopId和companyId是否为空
     *
     * @param specificationProgramRequestDTO 方案信息Request
     * @return
     */
    private boolean checkShopAndCompanyExist(LoginUser user, SpecificationProgramRequestDTO specificationProgramRequestDTO) {
        boolean flag = (null != user.getShopId() && null != user.getCompanyId()) ? true : false;
        specificationProgramRequestDTO.setCompanyId(user.getCompanyId());
        specificationProgramRequestDTO.setShopId(user.getShopId());
        specificationProgramRequestDTO.setModifier(user.getName());
        specificationProgramRequestDTO.setModifytime(new Date());
        return flag;
    }

    /**
     * 从当前登录用户信息中检查shopId和companyId是否为空
     *
     * @param specificationRequestDTO 方案细节信息Request
     * @return
     */
    private boolean checkShopAndCompanyExist(LoginUser user, SpecificationRequestDTO specificationRequestDTO) {
        boolean flag = (null != user.getShopId() && null != user.getCompanyId()) ? true : false;
        specificationRequestDTO.setCompanyId(user.getCompanyId());
        specificationRequestDTO.setShopId(user.getShopId());
        specificationRequestDTO.setModifier(user.getName());
        specificationRequestDTO.setModifytime(new Date());
        return flag;
    }

    /**
     * 从当前登录用户信息中检查shopId和companyId是否为空
     *
     * @param user
     * @return
     */
    private boolean checkShopAndCompanyExist(LoginUser user, CategoryRequestDTO categoryRequestDTO) {
        boolean flag = (null != user.getShopId() && null != user.getCompanyId()) ? true : false;
        categoryRequestDTO.setShopId(user.getShopId());
        categoryRequestDTO.setCompanyId(user.getCompanyId());
        categoryRequestDTO.setModifier(user.getName());
        categoryRequestDTO.setModifytime(new Date());
        return flag;
    }

}
