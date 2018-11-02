package com.zero.egg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zero.egg.dao.EggTypeMapper;
import com.zero.egg.model.EggType;
import com.zero.egg.service.BaseInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @ClassName BaseInfoServiceImpl
 * @Description 基础信息模块ServiceImpl
 * @Author lyming
 * @Date 2018/11/1 19:36
 **/
@Service
@Slf4j
public class BaseInfoServiceImpl implements BaseInfoService {


    @Autowired
    private EggTypeMapper eggTypeMapper;

    @Override
    @Transactional
    public void saveEggType(EggType eggType) {
        try {
            eggTypeMapper.insert(eggType);
        } catch (Exception e) {
            /** 坐等大佬斌写自定义异常 */
            log.error("saveEggType Error!", e);
        }
    }
}
