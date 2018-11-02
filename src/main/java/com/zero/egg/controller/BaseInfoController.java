package com.zero.egg.controller;

import com.zero.egg.model.EggType;
import com.zero.egg.service.BaseInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @RequestMapping(value = "/variety/add")
    public String saveEggType(@RequestBody EggType eggType) {
        //坐等大佬兵写自定义Json返回对象,暂时写String
        String result = "sucess";
        //应该是从session里面获取,暂时写死
        eggType.setStrCreateUser("老王");
        eggType.setStrEggTypeCode("TP001");
        try {
            baseInfoService.saveEggType(eggType);
            return result;
        } catch (Exception e) {
            result = "failed";
            return result;
        }
    }


}
