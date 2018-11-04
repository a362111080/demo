package com.zero.egg.controller;

import com.zero.egg.model.EggType;
import com.zero.egg.service.BaseInfoService;
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
    private BaseInfoService baseInfoService;

    /**
     * @param eggType
     * @return
     * @Description 添加鸡蛋类型
     */
    @RequestMapping(value = "/variety/add", method = RequestMethod.POST)
    public String saveEggType(@RequestBody EggType eggType) {
        //坐等大佬兵写自定义Json返回对象,暂时写String
        String result = "sucess";
        try {
            if (null != eggType) {
                //应该是从session里面获取,暂时写死
                eggType.setStrCreateUser("老王");
                baseInfoService.saveEggType(eggType);
            } else {
                result = "failed";
            }
            return result;
        } catch (Exception e) {
            result = "failed";
            return result;
        }
    }

    /**
     * @param eggType
     * @return
     * @Description 删除鸡蛋类型（单个删除）
     */
    @RequestMapping(value = "/variety/delete", method = RequestMethod.POST)
    public String deleteEggTypeById(@RequestBody EggType eggType) {
        String result = "sucess";
        try {
            if (null != eggType.getId()) {
                baseInfoService.deleteEggTypeById(eggType);
            } else {
                result = "failed";
            }
            return result;
        } catch (Exception e) {
            result = "failed";
            return result;
        }
    }

    /**
     * @Description 批量删除鸡蛋类型
     * @Param [eggType]
     * @Return java.lang.String
     **/
    @RequestMapping(value = "/variety/batchDelete", method = RequestMethod.POST)
    public String batchDeleteEggType(@RequestBody EggType eggType) {
        String result = "success";
        try {
            if (null != eggType.getIds()) {
                baseInfoService.batchDeleteEggType(eggType);
            } else {
                result = "请选择至少一种类型";
            }
            return result;
        } catch (Exception e) {
            result = "failed";
            return result;
        }
    }


}
