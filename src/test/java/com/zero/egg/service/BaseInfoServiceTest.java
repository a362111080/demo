package com.zero.egg.service;

import com.Apptest;
import com.zero.egg.requestDTO.EggTypeRequestDTO;
import com.zero.egg.tool.Message;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

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
        saveEggTypeDTO.setStrEggtypeName("测试品种2");
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

    @Test
    /**
     * 删除单个品种
     */
    public void testDeleteById() {
        EggTypeRequestDTO deleteEggTypeDTO = new EggTypeRequestDTO();
        deleteEggTypeDTO.setId("ce22c28f6a9146dab7ba26ed3dff9995");
        deleteEggTypeDTO.setShopId("1");
        deleteEggTypeDTO.setEnterpriseId("1");
        eggTypeService.deleteEggTypeById(deleteEggTypeDTO);
    }

    @Test
    /**
     * 批量删除品种
     */
    public void testBatchDeleteByIds() {
        EggTypeRequestDTO batchDeleteDTO = new EggTypeRequestDTO();
        List<String> ids = new ArrayList<>();
        ids.add("2978dd4e3f00449fa4caaa23dff05cf9");
        ids.add("0d4813042eb54475bd7cf525fd5c80de");
        batchDeleteDTO.setIds(ids);
        batchDeleteDTO.setShopId("1");
        batchDeleteDTO.setEnterpriseId("1");
        eggTypeService.batchDeleteEggType(batchDeleteDTO);
    }

    /**
     * 按主键查询品种接口
     */
    @Test
    public void testSelectEggTypeById() {
        EggTypeRequestDTO eggTypeRequestDTO = new EggTypeRequestDTO();
        eggTypeRequestDTO.setId("0a23ecdfa4594197a3ff53144d7d5a5b");
        eggTypeRequestDTO.setShopId("1");
        eggTypeRequestDTO.setEnterpriseId("1");
        Message message = eggTypeService.selectEggTypeById(eggTypeRequestDTO);
        System.out.println(message.getData());
    }
}
