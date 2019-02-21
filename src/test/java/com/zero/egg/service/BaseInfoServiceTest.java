package com.zero.egg.service;

import com.Apptest;
import com.zero.egg.requestDTO.EggTypeRequestDTO;
import com.zero.egg.requestDTO.StandardDataRequestDTO;
import com.zero.egg.requestDTO.StandardDetlRequestDTO;
import com.zero.egg.tool.Message;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @ClassName BaseInfoServiceTest
 * @Description 基础模块测试类
 * @Author lyming
 * @Date 2019/1/30 10:12 PM
 **/
public class BaseInfoServiceTest extends Apptest {

    @Autowired
    private EggTypeService eggTypeService;

    @Autowired
    private StandardDataService standardDataService;

    @Autowired
    private StandardDetlService standardDetlService;

    @Test
    /**
     * 新增品种
     */
    public void testAddEggType() {
        EggTypeRequestDTO saveEggTypeDTO = new EggTypeRequestDTO();
        saveEggTypeDTO.setName("测试品种3");
        saveEggTypeDTO.setCompanyId("1");
        saveEggTypeDTO.setShopId("1");
        saveEggTypeDTO.setCreator("老王");
        saveEggTypeDTO.setModifier("老李");

        Message message = eggTypeService.saveEggType(saveEggTypeDTO);

        assertEquals(1, message.getState());
    }

    @Test
    /**
     * 修改品种
     */
    public void testModifyEggType() {
        EggTypeRequestDTO modifyEggTypeDTO = new EggTypeRequestDTO();
        modifyEggTypeDTO.setId("738a6d1bb3ef4604ac426fb53353b1b4");
        modifyEggTypeDTO.setName("测试品种2");
        modifyEggTypeDTO.setCompanyId("1");
        modifyEggTypeDTO.setShopId("1");
        modifyEggTypeDTO.setModifier("laowang2");
        modifyEggTypeDTO.setModifytime(new Date());

        Message message = eggTypeService.modifyEggType(modifyEggTypeDTO);
        assertEquals(1, message.getState());

    }

    @Test
    /**
     * 删除单个品种
     */
    public void testDeleteById() {
        EggTypeRequestDTO deleteEggTypeDTO = new EggTypeRequestDTO();
        deleteEggTypeDTO.setId("782909ad94264e8cab1aa1e5548b4766");
        deleteEggTypeDTO.setShopId("1");
        deleteEggTypeDTO.setCompanyId("1");
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
        batchDeleteDTO.setCompanyId("1");
        eggTypeService.batchDeleteEggType(batchDeleteDTO);
    }

    /**
     * 按主键查询品种接口
     */
    @Test
    public void testSelectEggTypeById() {
        EggTypeRequestDTO eggTypeRequestDTO = new EggTypeRequestDTO();
        eggTypeRequestDTO.setId("3d02e7c100ec4129823738e36a44f679");
        eggTypeRequestDTO.setShopId("1");
        eggTypeRequestDTO.setCompanyId("1");
        Message message = eggTypeService.selectEggTypeById(eggTypeRequestDTO);
        System.out.println(message.getData());
    }

    @Test
    /**
     * 品种列表分页模糊查询
     */
    public void testListEggType() {
        EggTypeRequestDTO eggTypeRequestDTO = new EggTypeRequestDTO();
        eggTypeRequestDTO.setShopId("1");
        eggTypeRequestDTO.setCompanyId("1");
        eggTypeRequestDTO.setName("1");
        eggTypeRequestDTO.setCurrent(3L);
        eggTypeRequestDTO.setSize(2L);
        Message message = eggTypeService.listEggType(eggTypeRequestDTO);
        System.out.println(message.getState());
    }

    @Test
    /**
     * 新增方案
     */
    public void testAddStandard() {
        StandardDataRequestDTO standardDataRequestDTO = new StandardDataRequestDTO();
        standardDataRequestDTO.setName("测试方案");
        standardDataRequestDTO.setCategoryId("3d02e7c100ec4129823738e36a44f679");
        standardDataRequestDTO.setShopId("1");
        standardDataRequestDTO.setCompanyId("1");
        standardDataRequestDTO.setCreator("laowang");
        standardDataRequestDTO.setModifier("laowang");
        standardDataService.addStandardData(standardDataRequestDTO);
    }

    @Test
    /**
     * 新增方案细节
     */
    public void testAddStandardDetl() {
        StandardDetlRequestDTO standardDetlRequestDTO = new StandardDetlRequestDTO();
        standardDetlRequestDTO.setCompanyId("1");
        standardDetlRequestDTO.setShopId("1");
        standardDetlRequestDTO.setCreator("laowang");
        standardDetlRequestDTO.setModifier("laowang");
        standardDetlRequestDTO.setProgramId("3ede7a23dadb4fba928c56fbf7cfdff8");
        standardDetlRequestDTO.setWeightMin(new BigDecimal("10"));
        standardDetlRequestDTO.setWeightMax(new BigDecimal("20"));
        standardDetlRequestDTO.setMarker("&*^&*123");
        standardDetlRequestDTO.setMode(2);
        standardDetlRequestDTO.setWarn(0);
        standardDetlService.addStandardDetl(standardDetlRequestDTO);
    }

    @Test
    /**
     * 更新方案细节
     */
    public void testUpdateStandardDetl() {
        StandardDetlRequestDTO standardDetlRequestDTO = new StandardDetlRequestDTO();
        standardDetlRequestDTO.setCompanyId("1");
        standardDetlRequestDTO.setShopId("1");
        standardDetlRequestDTO.setModifier("laowang2");
        standardDetlRequestDTO.setModifytime(new Date());
        standardDetlRequestDTO.setProgramId("3ede7a23dadb4fba928c56fbf7cfdff8");
        standardDetlRequestDTO.setId("0528c54a58ab40d1a615b5c281e9e7b0");
        standardDetlRequestDTO.setWarn(1);
        standardDetlRequestDTO.setMarker("a9sud9812");
        standardDetlRequestDTO.setWeightMin(new BigDecimal(90));
        standardDetlRequestDTO.setWeightMax(new BigDecimal(440));
        standardDetlService.updateStandardDetl(standardDetlRequestDTO);
    }

}
