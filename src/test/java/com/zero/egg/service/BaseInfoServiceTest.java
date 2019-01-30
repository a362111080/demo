package com.zero.egg.service;

import com.Apptest;
import com.zero.egg.requestDTO.EggTypeRequestDTO;
import com.zero.egg.tool.Message;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;

/**
 * @ClassName BaseInfoServiceTest
 * @Description TODO
 * @Author lyming
 * @Date 2019/1/30 10:12 PM
 **/
public class BaseInfoServiceTest extends Apptest {

    @Autowired
    private EggTypeService eggTypeService;

    @Test
    public void testAddEggType() {
        EggTypeRequestDTO saveEggTypeDTO = new EggTypeRequestDTO();
        saveEggTypeDTO.setStrEggtypeName("测试品种");
        saveEggTypeDTO.setEnterpriseId("1");
        saveEggTypeDTO.setShopId("1");
        saveEggTypeDTO.setStrCreateuser("老王");

        Message message = eggTypeService.saveEggType(saveEggTypeDTO);

        assertEquals(1, message.getState());
    }
}
