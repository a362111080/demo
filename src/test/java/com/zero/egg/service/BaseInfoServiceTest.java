package com.zero.egg.service;

import com.Apptest;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zero.egg.cache.JedisUtil;
import com.zero.egg.dao.CategoryMapper;
import com.zero.egg.dao.OrderBillMapper;
import com.zero.egg.dao.ShipmentGoodsMapper;
import com.zero.egg.model.Category;
import com.zero.egg.requestDTO.CategoryRequestDTO;
import com.zero.egg.requestDTO.OrderBillDetailsRequestDTO;
import com.zero.egg.requestDTO.OrderBillListReqeustDTO;
import com.zero.egg.requestDTO.SpecificationProgramRequestDTO;
import com.zero.egg.requestDTO.SpecificationRequestDTO;
import com.zero.egg.responseDTO.OrderBillDetailResponseDTO;
import com.zero.egg.tool.Message;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;

/**
 * @ClassName BaseInfoServiceTest
 * @Description 基础模块测试类
 * @Author lyming
 * @Date 2019/1/30 10:12 PM
 **/
public class BaseInfoServiceTest extends Apptest {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SpecificationProgramService specificationProgramService;

    @Autowired
    private SpecificationService specificationService;

    @Autowired
    private WechatAuthService wechatAuthService;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private JedisUtil.Sets sets;

    @Autowired
    private JedisUtil.SortSets sortSets;

    @Autowired
    private ShipmentGoodsMapper shipmentGoodsMapper;

    @Autowired
    private OrderBillService orderBillService;

    @Autowired
    private OrderBillMapper orderBillMapper;


    @Test
    public void test() {
        OrderBillDetailsRequestDTO orderBillDetailsRequestDTO = new OrderBillDetailsRequestDTO();
        orderBillDetailsRequestDTO.setUserId("95959f5fed6d239f6ce52efc8b20c349");
        orderBillDetailsRequestDTO.setOrderId("4d5c7d9e928aad770f0d0e23112dfd0c");
        OrderBillDetailResponseDTO orderBillDetailResponseDTO;
        orderBillDetailResponseDTO = orderBillMapper.getOrderBillDetail(orderBillDetailsRequestDTO.getUserId(),orderBillDetailsRequestDTO.getOrderId());
        System.out.println(orderBillDetailResponseDTO);
    }

    @Test
    public void testBillList(){
        OrderBillListReqeustDTO orderBillListReqeustDTO = new OrderBillListReqeustDTO();
        orderBillListReqeustDTO.setUserId("8");
        orderBillListReqeustDTO.setStatus(1);
        orderBillService.listOrderBill(orderBillListReqeustDTO);
    }


    @Test
    public void testZadd(){
//        ShipmentGoods shipmentGoods1 = shipmentGoodsMapper.selectById("01b399a8efcef63bfcb961c4a58c17e7");
//        ShipmentGoods shipmentGoods2 = shipmentGoodsMapper.selectById("063be559414e84c2dabd6c028ba100ae");
//        ShipmentGoods shipmentGoods3 = shipmentGoodsMapper.selectById("063c52025a2e36ac8bb69cc55da3ed0a");
//        ShipmentGoods shipmentGoods4 = shipmentGoodsMapper.selectById("06f12b7b0bbb6e6b1726ce3dfcaa1d9d");
//        ShipmentGoods shipmentGoods5 = shipmentGoodsMapper.selectById("077fe856e97eedbdc96ec08addaf75f0");
//        long effectiveNum = sortSets.zadd("test",Double.valueOf(12345) , JsonUtils.objectToJson(shipmentGoods1));
//        sortSets.zadd("test",Double.valueOf(new Date().getTime()) , JsonUtils.objectToJson(shipmentGoods2));
//        sortSets.zadd("test",Double.valueOf(new Date().getTime()) , JsonUtils.objectToJson(shipmentGoods3));
//        sortSets.zadd("test",Double.valueOf(new Date().getTime()) , JsonUtils.objectToJson(shipmentGoods4));
//        sortSets.zadd("test",Double.valueOf(new Date().getTime()) , JsonUtils.objectToJson(shipmentGoods5));
//        List<ShipmentGoods> list = sortSets.
        Set<String> set =  sortSets.zrevrange("test",0,-1);
////        JsonUtils.jsonToList()
        System.out.println(set.size());
//        String jsonString = JsonUtils.objectToJson(set);
//        System.out.println(jsonString);
//        List<ShipmentGoods> list = JsonUtils.jsonToList(jsonString, ShipmentGoods.class);
//        System.out.println(list);
//        System.out.println(effectiveNum);
//        System.out.println(9/10);
    }


    @Test
    public void testRegisterWechatAuth() {
        System.out.println(wechatAuthService.getCountByOpenId("12y3912uirbhdqwebn"));
    }


    @Test
    public void testSelectSql() {
        Category category = categoryMapper.selectOne(new QueryWrapper<Category>().select("name").eq("id", "3a3d2d61e15c14c619ced247990fc08f"));
        System.out.println(category);
    }

    @Test
    /**
     * 新增品种
     */
    public void testAddEggType() {
        CategoryRequestDTO saveEggTypeDTO = new CategoryRequestDTO();
        saveEggTypeDTO.setName("测试品种44444");
        saveEggTypeDTO.setCompanyId("1");
        saveEggTypeDTO.setShopId("1");
        saveEggTypeDTO.setCreator("老王");
        saveEggTypeDTO.setModifier("老李");

        Message message = categoryService.saveEggType(saveEggTypeDTO);

        assertEquals(1, message.getState());
    }

    @Test
    /**
     * 修改品种
     */
    public void testModifyEggType() {
        CategoryRequestDTO modifyEggTypeDTO = new CategoryRequestDTO();
        modifyEggTypeDTO.setId("738a6d1bb3ef4604ac426fb53353b1b4");
        modifyEggTypeDTO.setName("测试品种2");
        modifyEggTypeDTO.setCompanyId("1");
        modifyEggTypeDTO.setShopId("1");
        modifyEggTypeDTO.setModifier("laowang2");
        modifyEggTypeDTO.setModifytime(new Date());

        Message message = categoryService.modifyEggType(modifyEggTypeDTO);
        assertEquals(1, message.getState());

    }

    @Test
    /**
     * 删除单个品种
     */
    public void testDeleteById() {
        CategoryRequestDTO deleteEggTypeDTO = new CategoryRequestDTO();
        deleteEggTypeDTO.setId("491fb285e47248fab419add60d534103");
        deleteEggTypeDTO.setShopId("1");
        deleteEggTypeDTO.setCompanyId("1");
        categoryService.deleteEggTypeById(deleteEggTypeDTO);
    }

    @Test
    /**
     * 批量删除品种
     */
    public void testBatchDeleteByIds() {
        CategoryRequestDTO batchDeleteDTO = new CategoryRequestDTO();
        List<String> ids = new ArrayList<>();
        ids.add("491fb285e47248fab419add60d534103");
        ids.add("0d4813042eb54475bd7cf525fd5c80de");
        batchDeleteDTO.setIds(ids);
        batchDeleteDTO.setShopId("1");
        batchDeleteDTO.setCompanyId("1");
        categoryService.batchDeleteEggType(batchDeleteDTO);
    }

    /**
     * 按主键查询品种接口
     */
    @Test
    public void testSelectEggTypeById() {
        CategoryRequestDTO categoryRequestDTO = new CategoryRequestDTO();
        categoryRequestDTO.setId("3d02e7c100ec4129823738e36a44f679");
        categoryRequestDTO.setShopId("1");
        categoryRequestDTO.setCompanyId("1");
        Message message = categoryService.selectEggTypeById(categoryRequestDTO);
        System.out.println(message.getData());
    }

    @Test
    /**
     * 品种列表分页模糊查询
     */
    public void testListEggType() {
        CategoryRequestDTO categoryRequestDTO = new CategoryRequestDTO();
        categoryRequestDTO.setShopId("1");
        categoryRequestDTO.setCompanyId("1");
        categoryRequestDTO.setName("1");
        categoryRequestDTO.setCurrent(3L);
        categoryRequestDTO.setSize(2L);
        Message message = categoryService.listEggType(categoryRequestDTO);
        System.out.println(message.getState());
    }

    @Test
    /**
     * 新增方案
     */
    public void testAddStandard() {
        SpecificationProgramRequestDTO specificationProgramRequestDTO = new SpecificationProgramRequestDTO();
        specificationProgramRequestDTO.setName("测试方案");
        specificationProgramRequestDTO.setCategoryId("3d02e7c100ec4129823738e36a44f679");
        specificationProgramRequestDTO.setShopId("1");
        specificationProgramRequestDTO.setCompanyId("1");
        specificationProgramRequestDTO.setCreator("laowang");
        specificationProgramRequestDTO.setModifier("laowang");
        specificationProgramService.addStandardData(specificationProgramRequestDTO);
    }

    @Test
    /**
     * 新增方案细节
     */
    public void testAddStandardDetl() {
        SpecificationRequestDTO specificationRequestDTO = new SpecificationRequestDTO();
        specificationRequestDTO.setCompanyId("1");
        specificationRequestDTO.setShopId("1");
        specificationRequestDTO.setCreator("laowang");
        specificationRequestDTO.setModifier("laowang");
        specificationRequestDTO.setProgramId("3ede7a23dadb4fba928c56fbf7cfdff8");
        specificationRequestDTO.setWeightMin(new BigDecimal("10"));
        specificationRequestDTO.setWeightMax(new BigDecimal("20"));
        specificationRequestDTO.setMarker("&*^&*123");
        specificationRequestDTO.setMode(2);
        specificationRequestDTO.setWarn(0);
        specificationService.addStandardDetl(specificationRequestDTO);
    }

    @Test
    /**
     * 更新方案细节
     */
    public void testUpdateStandardDetl() {
        SpecificationRequestDTO specificationRequestDTO = new SpecificationRequestDTO();
        specificationRequestDTO.setCompanyId("1");
        specificationRequestDTO.setShopId("1");
        specificationRequestDTO.setModifier("laowang2");
        specificationRequestDTO.setModifytime(new Date());
        specificationRequestDTO.setProgramId("3ede7a23dadb4fba928c56fbf7cfdff8");
        specificationRequestDTO.setId("0528c54a58ab40d1a615b5c281e9e7b0");
        specificationRequestDTO.setWarn(1);
        specificationRequestDTO.setMode(2);
        specificationRequestDTO.setMarker("a9sud9812");
        specificationRequestDTO.setWeightMin(new BigDecimal(90));
        specificationRequestDTO.setWeightMax(new BigDecimal(440));
        specificationService.updateStandardDetl(specificationRequestDTO);
    }

    @Test
    /**
     * 根据方案id列出其下方案细节
     */
    public void testListstandarddetel(){
        SpecificationRequestDTO specificationRequestDTO = new SpecificationRequestDTO();
        specificationRequestDTO.setProgramId("e61177ee779e4548b0c305d577232d57");
        Message message = specificationService.listStandardDetlByProgramId(specificationRequestDTO);
        System.out.println(message.getState());
    }

    @Test
    /**
     * 更新方案(方案名)
     */
    public void testUpdateSpecificationProgram() {
        SpecificationProgramRequestDTO specificationProgramRequestDTO = new SpecificationProgramRequestDTO();
        specificationProgramRequestDTO.setCompanyId("1");
        specificationProgramRequestDTO.setShopId("1");
        specificationProgramRequestDTO.setCategoryId("a50c111d11f443cdb7866e995537c186");
        specificationProgramRequestDTO.setModifier("laowanggggg");
        specificationProgramRequestDTO.setModifytime(new Date());
        specificationProgramRequestDTO.setId("e61177ee779e4548b0c305d577232d57");
        specificationProgramRequestDTO.setName("测试方案333");
        Message message = specificationProgramService.updateSpecificationProgram(specificationProgramRequestDTO);
        System.out.println(message.getMessage());
    }

    @Test
    /**
     * 列出选中品种下的所有方案和方案细节
     */
    public void testListDataAndDetl(){
        SpecificationProgramRequestDTO specificationProgramRequestDTO = new SpecificationProgramRequestDTO();
        specificationProgramRequestDTO.setCategoryId("a50c111d11f443cdb7866e995537c186");
        specificationProgramRequestDTO.setShopId("1");
        specificationProgramRequestDTO.setCompanyId("1");
        Message message = specificationProgramService.listDataAndDetl(specificationProgramRequestDTO);
        System.out.println(message.getMessage());
    }

    @Test
    /**
     * 删除方案(逻辑删除)
     */
    public void testDeleteSpecificationProgram() {
        SpecificationProgramRequestDTO specificationProgramRequestDTO = new SpecificationProgramRequestDTO();
        specificationProgramRequestDTO.setShopId("1");
        specificationProgramRequestDTO.setCompanyId("1");
        specificationProgramRequestDTO.setId("e61177ee779e4548b0c305d577232d57");
        Message message = specificationProgramService.deleteStandardDataById(specificationProgramRequestDTO);
        System.out.println(message.getMessage());
    }
}
