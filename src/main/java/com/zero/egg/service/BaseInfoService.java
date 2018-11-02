package com.zero.egg.service;

import com.zero.egg.model.EggType;

/**
 * @Description 基础模块Service
 * @Author lyming
 * @Date 2018/11/1 19:25
 **/
public interface BaseInfoService {

    /**
     * @Description 新增鸡蛋类型
     * @Param [eggType]
     * @Return void
     **/
    public void saveEggType(EggType eggType);
}
