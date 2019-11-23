package com.zero.egg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zero.egg.config.FileUploadProperteis;
import com.zero.egg.dao.BarCodeMapper;
import com.zero.egg.dao.CategoryMapper;
import com.zero.egg.dao.ShopMapper;
import com.zero.egg.dao.SupplierMapper;
import com.zero.egg.model.BarCode;
import com.zero.egg.model.BarCodeInfoDTO;
import com.zero.egg.model.Category;
import com.zero.egg.model.Shop;
import com.zero.egg.model.Supplier;
import com.zero.egg.requestDTO.BarCodeListRequestDTO;
import com.zero.egg.requestDTO.BarCodeRequestDTO;
import com.zero.egg.responseDTO.BarCodeListResponseDTO;
import com.zero.egg.responseDTO.PrintBarCodeResponseDTO;
import com.zero.egg.responseDTO.SinglePrintBarCodeDTO;
import com.zero.egg.service.BarCodeService;
import com.zero.egg.tool.MatrixToImageWriterUtil;
import com.zero.egg.tool.Message;
import com.zero.egg.tool.ServiceException;
import com.zero.egg.tool.TransferUtil;
import com.zero.egg.tool.UtilConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@Slf4j
public class BarCodeServiceImpl implements BarCodeService {

    @Autowired
    private BarCodeMapper mapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private SupplierMapper supplierMapper;

    @Autowired
    private ShopMapper shopMapper;

    @Override
    @Transactional
    public Message AddBarCode(BarCodeRequestDTO barCodeRequestDTO) {
        BarCode barCode = new BarCode();
        Message message = new Message();
        Map<String, String> hash;
        try {
            TransferUtil.copyProperties(barCode, barCodeRequestDTO);
            /**根据供应商id和category_id生成二维码,并把相对路径复制给barCode实体类进行新增操作*/
            String targetAddr = FileUploadProperteis.getMatrixImagePath(barCode.getCompanyId(),
                    barCode.getShopId(), barCode.getSupplierId(), barCode.getCategoryId());
            /**
             * 查重:同一个店铺同一品种只能生成一张母二维码
             */
            int count = mapper.selectCount(new QueryWrapper<BarCode>()
                    .eq("company_id", barCode.getCompanyId())
                    .eq("shop_id", barCode.getShopId())
                    .eq("supplier_id", barCode.getSupplierId())
                    .eq("category_id", barCode.getCategoryId())
                    .eq("dr", 0));
            if (count > 0) {
                message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
                message.setMessage(UtilConstants.ResponseMsg.DUPLACTED_DATA);
                return message;
            }
//            BarCodeInfoDTO infoDTO = compactBarInfo(barCode);
            String categoryName = categoryMapper.selectOne(new QueryWrapper<Category>()
                    .select("name")
                    .eq("id", barCode.getCategoryId()))
                    .getName();
            String supplierName = supplierMapper.selectOne(new QueryWrapper<Supplier>()
                    .select("name")
                    .eq("id", barCode.getSupplierId()))
                    .getName();
            barCode.setCategoryName(categoryName);
            barCode.setSupplierName(supplierName);
            mapper.insert(barCode);
            /**
             * 二维码信息包含二维码id
             */
            String text = barCode.getId();
            String shopName = shopMapper.selectOne(new QueryWrapper<Shop>()
                    .select("name")
                    .eq("id", barCode.getShopId()))
                    .getName();
            //生成二维码,返回二维码地址
            String matrixAddr = MatrixToImageWriterUtil.writeToFile(targetAddr, text, "BaseMatrix", shopName, categoryName, null);
            barCode.setMatrixAddr(matrixAddr);
            //将二维码地址更新到数据库
            int effectNum = mapper.updateById(barCode);
            if (effectNum > 0) {
                hash = new HashMap<>();
                hash.put("matrixAddr", matrixAddr);
                message.setData(hash);
                message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
                message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
            } else {
                message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
                message.setMessage(UtilConstants.ResponseMsg.FAILED);
            }
            return message;
        } catch (Exception e) {
            log.error("AddBarCode error:" + e);
            throw new ServiceException("AddBarCode error");
        }
    }


    @Override
    public void DeleteBarCode(BarCodeRequestDTO model) {
        BarCode barCode = new BarCode();
        try {
            barCode.setDr(true);
            mapper.update(barCode, new UpdateWrapper<BarCode>()
                    .in("id", model.getIds())
                    .eq("shop_id", model.getShopId())
                    .eq("company_id", model.getCompanyId()));
        } catch (Exception e) {
            log.error("DeleteBarCode Error!", e);
            throw new ServiceException("DeleteBarCode Error!");
        }
    }

    @Override
    public Message GetBarCodeList(BarCodeListRequestDTO listRequestDTO) {
        Message message = new Message();
        Page<BarCode> page = new Page<>();
        BarCodeListResponseDTO barCodeListResponseDTO = new BarCodeListResponseDTO();
        try {
            page.setCurrent(listRequestDTO.getCurrent());
            page.setSize(listRequestDTO.getSize());
            page = (Page<BarCode>) mapper.selectPage(page, new QueryWrapper<BarCode>()
                    .eq("shop_id", listRequestDTO.getShopId())
                    .eq("company_id", listRequestDTO.getCompanyId())
                    .eq("dr", 0)
                    .eq("current_code", "")
                    .nested(null != listRequestDTO.getSupplierName() && !"".equals(listRequestDTO.getSupplierName())
                            ,i -> i.like("supplier_name", listRequestDTO.getSupplierName())
                            .or()
                            .like("code", listRequestDTO.getSupplierName()))
            );
            barCodeListResponseDTO.setBarCodeList(page.getRecords());
            barCodeListResponseDTO.setCurrent(page.getCurrent());
            barCodeListResponseDTO.setPages(page.getPages());
            barCodeListResponseDTO.setTotal(page.getTotal());
            barCodeListResponseDTO.setSize(page.getSize());
            message.setData(barCodeListResponseDTO);
            message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
            message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
        } catch (Exception e) {
            log.error("GetBarCodeList!", e);
            throw new ServiceException("GetBarCodeList");
        }


        return message;
    }

    @Override
    @Transactional
    public Message PrintBarCode(String barCodeId, int printNum, String loginUserId) {
        //母二维码信息
        BarCode barCode;
        //子二维码信息
        BarCode newBarCode = new BarCode();
        Message message = new Message();
        PrintBarCodeResponseDTO barCodeResponseDTO = new PrintBarCodeResponseDTO();
        List<SinglePrintBarCodeDTO> singlePrintBarCodeDTOList = new ArrayList<>();
        SinglePrintBarCodeDTO barCodeDTO = null;
        try {
            /**
             * 先根据二维码主键id获取二维码对象
             */
            barCode = mapper.selectById(barCodeId);
            //二维码服务器上相对路径
            String targetAddr = FileUploadProperteis.getMatrixImagePath(barCode.getCompanyId(),
                    barCode.getShopId(), barCode.getSupplierId(), barCode.getCategoryId());
            /**8位编码前面补0格式*/
            DecimalFormat g1 = new DecimalFormat("00000000");
            String currentCode = null;
            String categoryName = categoryMapper.selectOne(new QueryWrapper<Category>()
                    .select("name")
                    .eq("id", barCode.getCategoryId()))
                    .getName();
            String shopName = shopMapper.selectOne(new QueryWrapper<Shop>()
                    .select("name")
                    .eq("id", barCode.getShopId()))
                    .getName();
            for (int i = 0; i < printNum; i++) {
                newBarCode = new BarCode();
                //查询同一企业下同一店铺下同一供应商下的数量,初始应该为1(母二维码)
                int count = mapper.selectCount(new QueryWrapper<BarCode>()
                        .eq("shop_id", barCode.getShopId())
                        .eq("company_id", barCode.getCompanyId())
                        .eq("supplier_id", barCode.getSupplierId())
                );
                TransferUtil.copyProperties(newBarCode, barCode);
                currentCode = barCode.getCode() + g1.format(count);
                newBarCode.setId(null);
                //覆盖母二维码的创建人,创建时间
                newBarCode.setCreatetime(new Date());
                newBarCode.setModifytime(new Date());
                newBarCode.setCreator(loginUserId);
                newBarCode.setModifier(loginUserId);
                newBarCode.setCurrentCode(currentCode);
                mapper.insert(newBarCode);
                String text = newBarCode.getId();
                String matrixAddr = MatrixToImageWriterUtil.writeToFile(targetAddr, text, currentCode, shopName, categoryName, currentCode);
                newBarCode.setMatrixAddr(matrixAddr);
                mapper.updateById(newBarCode);
                barCodeDTO = new SinglePrintBarCodeDTO();
                barCodeDTO.setCategoryName(barCode.getCategoryName());
                barCodeDTO.setCurrentCode(currentCode);
                barCodeDTO.setMatrixAddr(matrixAddr);
                barCodeDTO.setShopName(shopName);
                singlePrintBarCodeDTOList.add(barCodeDTO);
                newBarCode = null;
            }
            barCodeResponseDTO.setPrintBarCodeDTOS(singlePrintBarCodeDTOList);
            message.setData(barCodeResponseDTO);
            return message;
        } catch (Exception e) {
            log.error("PrintBarCode error:" + e);
            throw new ServiceException("PrintBarCode error");
        }
    }

    /**
     * 封装母二维码信息(不包含current_code和matrix_addr)
     *
     * @param barCode
     * @return
     */
    private BarCodeInfoDTO compactBarInfo(BarCode barCode) {
        BarCodeInfoDTO infoDTO = new BarCodeInfoDTO();
        Supplier supplier = supplierMapper.selectOne(new QueryWrapper<Supplier>().select("name").eq("id", barCode.getSupplierId()));
        Category category = categoryMapper.selectOne(new QueryWrapper<Category>().select("name").eq("id", barCode.getCategoryId()));
        Shop shop = shopMapper.selectOne(new QueryWrapper<Shop>().select("name").eq("id", barCode.getShopId()));
        barCode.setCategoryName(category.getName());
        barCode.setSupplierName(supplier.getName());
        infoDTO.setCategoryId(barCode.getCategoryId());
        infoDTO.setCategoryName(category.getName());
        infoDTO.setCompanyId(barCode.getCompanyId());
        infoDTO.setSupplierId(barCode.getSupplierId());
        infoDTO.setSupplierName(supplier.getName());
        infoDTO.setCode(barCode.getCode());
        infoDTO.setShopId(barCode.getShopId());
        infoDTO.setShopName(shop.getName());
        return infoDTO;
    }
}
