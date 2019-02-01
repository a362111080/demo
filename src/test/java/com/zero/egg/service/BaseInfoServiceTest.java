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
    /**
     * 新增品种
     */
    public void testAddEggType() {
        EggTypeRequestDTO saveEggTypeDTO = new EggTypeRequestDTO();
        saveEggTypeDTO.setStrEggtypeName("测试品种");
        saveEggTypeDTO.setEnterpriseId("1");
        saveEggTypeDTO.setShopId("1");
        saveEggTypeDTO.setStrCreateuser("老王");

        Message message = eggTypeService.saveEggType(saveEggTypeDTO);

        assertEquals(1, message.getState());
    }

    @Test
    /**
     * 修改品种
     */
    public void testModifyEggType() {
        EggTypeRequestDTO modifyEggTypeDTO = new EggTypeRequestDTO();
        modifyEggTypeDTO.setId("ce22c28f6a9146dab7ba26ed3dff9995");
        modifyEggTypeDTO.setStrEggtypeName("测试品种2");
        modifyEggTypeDTO.setEnterpriseId("1");
        modifyEggTypeDTO.setShopId("1");
        modifyEggTypeDTO.setStrCreateuser("老王222");

        Message message = eggTypeService.modifyEggType(modifyEggTypeDTO);
        assertEquals(1, message.getState());

    }
}
