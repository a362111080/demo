package com.zero.egg.controller;

import com.zero.egg.model.EggType;
import com.zero.egg.service.BaseInfoService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public String saveEggType(EggType eggType) {
        baseInfoService.saveEggType(eggType);
        return "success";
    }


}
