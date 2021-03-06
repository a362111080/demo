package com.zero.egg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zero.egg.dao.BarCodeMapper;
import com.zero.egg.dao.CategoryMapper;
import com.zero.egg.dao.GoodsMapper;
import com.zero.egg.dao.SpecificationMapper;
import com.zero.egg.dao.SpecificationProgramMapper;
import com.zero.egg.dao.StockMapper;
import com.zero.egg.model.BarCode;
import com.zero.egg.model.Category;
import com.zero.egg.model.Goods;
import com.zero.egg.model.SpecificationProgram;
import com.zero.egg.model.Stock;
import com.zero.egg.requestDTO.CategoryRequestDTO;
import com.zero.egg.requestDTO.SpecificationProgramRequestDTO;
import com.zero.egg.responseDTO.CategoryListResponseDTO;
import com.zero.egg.service.CategoryService;
import com.zero.egg.service.SpecificationProgramService;
import com.zero.egg.tool.Message;
import com.zero.egg.tool.ServiceException;
import com.zero.egg.tool.TransferUtil;
import com.zero.egg.tool.UtilConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @ClassName BaseInfoServiceImpl
 * @Description 鸡蛋类型模块ServiceImpl
 * @Author lyming
 * @Date 2018/11/1 19:36
 **/
@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {


    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private SpecificationProgramMapper specificationProgramMapper;

    @Autowired
    private SpecificationMapper specificationMapper;

    @Autowired
    private SpecificationProgramService programService;

    @Autowired
    private BarCodeMapper barCodeMapper;

    @Autowired
    private StockMapper stockMapper;

    @Autowired
    private GoodsMapper goodsMapper;

    @Override
    @Transactional
    public Message saveEggType(CategoryRequestDTO saveCategoryRequestDTO) {
        Category category = new Category();
        Message message = new Message();
        /** 查重结果 */
        List<Category> resultList = null;
        try {
            TransferUtil.copyProperties(category, saveCategoryRequestDTO);
            /** 插入数据前做查重操作
             *  name不能重复
             */
            if (checkByName(resultList, category) > 0) {
                message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
                message.setMessage(UtilConstants.ResponseMsg.DUPLACTED_DATA);
            } else {
                categoryMapper.insert(category);
                message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
                message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
            }
            return message;
        } catch (Exception e) {
            log.error("saveEggType Error!", e);
            throw new ServiceException("saveEggType Error!:" + e.toString());
        }
    }

    @Override
    @Transactional
    public void deleteEggTypeById(CategoryRequestDTO deleteCategoryRequestDTO) {
        Category category;
        BarCode barCode;
        SpecificationProgramRequestDTO specificationProgramRequestDTO = new SpecificationProgramRequestDTO();
        specificationProgramRequestDTO.setShopId(deleteCategoryRequestDTO.getShopId());
        specificationProgramRequestDTO.setCompanyId(deleteCategoryRequestDTO.getCompanyId());
        try {
            /**
             * 想根据Id删除所属的所有方案以及方案细节
             */
            List<SpecificationProgram> specificationProgramList = specificationProgramMapper.selectList(new QueryWrapper<SpecificationProgram>()
                    .eq("category_id", deleteCategoryRequestDTO.getId())
                    .eq("dr", 0)
                    .eq("shop_id", deleteCategoryRequestDTO.getShopId())
                    .eq("company_id", deleteCategoryRequestDTO.getCompanyId()));
            for (SpecificationProgram specificationProgram : specificationProgramList) {
                specificationProgramRequestDTO.setId(specificationProgram.getId());
                programService.deleteStandardDataById(specificationProgramRequestDTO);
            }
            //对应品种的二维码也作逻辑删除,只对母二维码作操作,以免影响到出货操作
            barCode = new BarCode();
            barCode.setDr(true);
            barCodeMapper.update(barCode, new UpdateWrapper<BarCode>()
                    .eq("shop_id", deleteCategoryRequestDTO.getShopId())
                    .eq("company_id", deleteCategoryRequestDTO.getCompanyId())
                    .eq("category_id", deleteCategoryRequestDTO.getId()));
            /**
             * 只做逻辑删除
             */
            category = new Category();
            category.setDr(1);
            categoryMapper.update(category, new UpdateWrapper<Category>()
                    .eq("id", deleteCategoryRequestDTO.getId())
                    .eq("shop_id", deleteCategoryRequestDTO.getShopId())
                    .eq("company_id", deleteCategoryRequestDTO.getCompanyId()));
            //要对仓库里面做对应品种的验证,如果仓库中还有对应品种的货物,则不许删除
            Integer effectNum = goodsMapper.selectCount(new QueryWrapper<Goods>()
                    .eq("goods_category_id", deleteCategoryRequestDTO.getId())
                    .eq("shop_id", deleteCategoryRequestDTO.getShopId())
                    .eq("company_id", deleteCategoryRequestDTO.getCompanyId())
                    .eq("dr", false));
            //如果库存中有货,则不允许删品种
            if (effectNum > 0) {
                throw new ServiceException("库存中还有对应品种的货物,请勿删除");
            } else {
                List<Goods> needToDelete = goodsMapper.selectList(new QueryWrapper<Goods>()
                        .select("specification_id")
                        .eq("goods_category_id", deleteCategoryRequestDTO.getId())
                        .eq("shop_id", deleteCategoryRequestDTO.getShopId())
                        .eq("company_id", deleteCategoryRequestDTO.getCompanyId()));
                Set<String> specificationSet = new HashSet<>();
                for (Goods goods : needToDelete) {
                    specificationSet.add(goods.getSpecificationId());
                }
                if (specificationSet.size() > 0) {
                    stockMapper.update(new Stock().setDr(true), new UpdateWrapper<Stock>()
                            .eq("shop_id", deleteCategoryRequestDTO.getShopId())
                            .eq("company_id", deleteCategoryRequestDTO.getCompanyId())
                            .in("specification_id", specificationSet));
                }
            }
        } catch (ServiceException se) {
            log.error(se.getMessage());
            throw se;
        } catch (Exception e) {
            log.error("deleteEggTypeById Error!", e);
            throw new ServiceException("deleteEggTypeById Error!");
        }
    }

    @Override
    @Transactional
    public void batchDeleteEggType(CategoryRequestDTO batchDeleteCategoryRequestDTO) {
        Category category = new Category();
        BarCode barCode;
        SpecificationProgramRequestDTO specificationProgramRequestDTO = new SpecificationProgramRequestDTO();
        specificationProgramRequestDTO.setShopId(batchDeleteCategoryRequestDTO.getShopId());
        specificationProgramRequestDTO.setCompanyId(batchDeleteCategoryRequestDTO.getCompanyId());
        try {
            List<SpecificationProgram> specificationProgramList = specificationProgramMapper.selectList(new QueryWrapper<SpecificationProgram>()
                    .in("category_id", batchDeleteCategoryRequestDTO.getId())
                    .eq("dr", 0)
                    .eq("shop_id", batchDeleteCategoryRequestDTO.getShopId())
                    .eq("company_id", batchDeleteCategoryRequestDTO.getCompanyId()));
            for (SpecificationProgram specificationProgram : specificationProgramList) {
                specificationProgramRequestDTO.setId(specificationProgram.getId());
                programService.deleteStandardDataById(specificationProgramRequestDTO);
            }
            barCode = new BarCode();
            barCode.setDr(true);
            barCodeMapper.update(barCode, new UpdateWrapper<BarCode>()
                    .eq("shop_id", batchDeleteCategoryRequestDTO.getShopId())
                    .eq("company_id", batchDeleteCategoryRequestDTO.getCompanyId())
                    .in("category_id", batchDeleteCategoryRequestDTO.getIds()));
            category.setDr(1);
            categoryMapper.update(category, new UpdateWrapper<Category>()
                    .in("id", batchDeleteCategoryRequestDTO.getIds())
                    .eq("shop_id", batchDeleteCategoryRequestDTO.getShopId())
                    .eq("company_id", batchDeleteCategoryRequestDTO.getCompanyId()));
            //要对仓库里面做对应品种的验证,如果仓库中还有对应品种的货物,则不许删除
            Integer effectNum = goodsMapper.selectCount(new QueryWrapper<Goods>()
                    .in("goods_category_id", batchDeleteCategoryRequestDTO.getIds())
                    .eq("shop_id", batchDeleteCategoryRequestDTO.getShopId())
                    .eq("company_id", batchDeleteCategoryRequestDTO.getCompanyId())
                    .eq("dr", false));
            //如果库存中有货,则不允许删品种
            if (effectNum > 0) {
                throw new ServiceException("库存中还有对应品种的货物,请勿删除");
            } else {
                List<Goods> needToDelete = goodsMapper.selectList(new QueryWrapper<Goods>()
                        .select("specification_id")
                        .in("goods_category_id", batchDeleteCategoryRequestDTO.getIds())
                        .eq("shop_id", batchDeleteCategoryRequestDTO.getShopId())
                        .eq("company_id", batchDeleteCategoryRequestDTO.getCompanyId()));
                Set<String> specificationSet = new HashSet<>();
                for (Goods goods : needToDelete) {
                    specificationSet.add(goods.getSpecificationId());
                }
                if (specificationSet.size() > 0) {
                    stockMapper.update(new Stock().setDr(true), new UpdateWrapper<Stock>()
                            .eq("shop_id", batchDeleteCategoryRequestDTO.getShopId())
                            .eq("company_id", batchDeleteCategoryRequestDTO.getCompanyId())
                            .in("specification_id", specificationSet));
                }
            }
        } catch (ServiceException se) {
            log.error(se.getMessage());
            throw se;
        } catch (Exception e) {
            log.error("batchDeleteEggType Error!", e);
            throw new ServiceException("batchDeleteEggType Error!");
        }
    }


    @Override
    public Message listEggType(CategoryRequestDTO categoryRequestDTO) {
        /**
         * 如果前端传来了strEggTypeName,则认为是条件模糊查询
         * 否则默认列出全部鸡蛋类型
         */
        Message message = new Message();
        Page<Category> page = new Page<>();
        CategoryListResponseDTO categoryListResponseDTO = new CategoryListResponseDTO();
        try {
            page.setCurrent(categoryRequestDTO.getCurrent());
            page.setSize(categoryRequestDTO.getSize());
            page = (Page<Category>) categoryMapper.selectPage(page, new QueryWrapper<Category>()
                    .eq("shop_id", categoryRequestDTO.getShopId())
                    .eq("company_id", categoryRequestDTO.getCompanyId())
                    .like(null != categoryRequestDTO.getName() && !"".equals(categoryRequestDTO.getName())
                            , "name", categoryRequestDTO.getName())
                    .eq("dr", 0));
            categoryListResponseDTO.setCategoryList(page.getRecords());
            categoryListResponseDTO.setCurrent(page.getCurrent());
            categoryListResponseDTO.setPages(page.getPages());
            categoryListResponseDTO.setTotal(page.getTotal());
            categoryListResponseDTO.setSize(page.getSize());

            message.setData(categoryListResponseDTO);
            message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
            message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
        } catch (Exception e) {
            log.error("listEggType Error!", e);
            throw new ServiceException("listEggType Error!");
        }
        return message;
    }

    @Override
    @Transactional
    public Message modifyEggType(CategoryRequestDTO modifyCategoryRequestDTO) {
        Message message = new Message();
        Category category = new Category();
        /** 查重结果 */
        List<Category> resultList = null;
        try {
            TransferUtil.copyProperties(category, modifyCategoryRequestDTO);
            /** str_eggtype_name 不能已经存在 */
            if (checkByName(resultList, category) > 0) {
                message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
                message.setMessage(UtilConstants.ResponseMsg.DUPLACTED_DATA);
            } else {
                categoryMapper.update(category, new UpdateWrapper<Category>()
                        .eq("id", category.getId())
                        .eq("shop_id", category.getShopId())
                        .eq("company_id", category.getCompanyId()));
                message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
                message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
            }
            return message;
        } catch (Exception e) {
            log.error("modifyEggType Error:" + e.toString());
            throw new ServiceException("modifyEggType Error");
        }
    }

    @Override
    public Message selectEggTypeById(CategoryRequestDTO categoryRequestDTO) throws ServiceException {
        Message message = new Message();
        Category category = null;
        try {
            category = categoryMapper.selectOne(new QueryWrapper<Category>()
                    .eq("id", categoryRequestDTO.getId())
                    .eq("shop_id", categoryRequestDTO.getShopId())
                    .eq("company_id", categoryRequestDTO.getCompanyId())
                    .eq("dr", 0));
            message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
            message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
            message.setData(category);
        } catch (Exception e) {
            log.error("selectEggTypeById Error!:" + e.toString());
            throw new ServiceException("selectEggTypeById Error!");
        }
        return message;
    }

    /**
     * 同个企业同个店铺只能存在独一无二的类型名
     *
     * @param resultList
     * @param category
     * @return 查重得到的集合大小
     */
    private int checkByName(List<Category> resultList, Category category) {
        resultList = categoryMapper.selectList(new QueryWrapper<Category>()
                .eq("name", category.getName())
                .eq("shop_id", category.getShopId())
                .eq("company_id", category.getCompanyId()
                ).eq("dr", 0));
        int count;
        if (null != resultList) {
            count = resultList.size();
        } else {
            count = 0;
        }
        return count;
    }
}
