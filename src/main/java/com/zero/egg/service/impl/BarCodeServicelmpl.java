package com.zero.egg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
import com.zero.egg.requestDTO.BarCodeRequestDTO;
import com.zero.egg.responseDTO.BarCodeResponseDTO;
import com.zero.egg.service.BarCodeService;
import com.zero.egg.tool.JsonUtils;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@Slf4j
public class BarCodeServicelmpl implements BarCodeService {

    @Autowired
    private BarCodeMapper mapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private SupplierMapper supplierMapper;

    @Autowired
    private ShopMapper shopMapper;

    @Override
    public Message AddBarCode(BarCodeRequestDTO barCodeRequestDTO) {
        BarCode barCode = new BarCode();
        Message message = new Message();
        Map<String, String> hash;
        try {
            TransferUtil.copyProperties(barCode, barCodeRequestDTO);
            /**根据供应商id和category_id生成二维码,并把相对路径复制给barCode实体类进行新增操作*/
            String targetAddr = FileUploadProperteis.getMatrixImagePath(barCode.getCompanyId(),
                    barCode.getShopId(), barCode.getSupplierId(), barCode.getCategoryId());
            //二维码信息包含供应商id,code,name  产品(鸡蛋)类别 id,name  店铺id  名称  企业 id
            //TODO 1.二维码信息需要加密 2.查重:同一个店铺同一品种只能生成一张母二维码
            BarCodeInfoDTO infoDTO = compactBarInfo(barCode);
            String text = JsonUtils.objectToJson(infoDTO);
            String matrixAddr = MatrixToImageWriterUtil.writeToFile(targetAddr, text, "BaseMatrix");
            barCode.setMatrixAddr(matrixAddr);
            int effectNum = mapper.insert(barCode);
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
    public int DeleteBarCode(BarCodeRequestDTO model) {
        return mapper.DeleteBarCode(model.getIds());
    }

    @Override
    public List<BarCodeResponseDTO> GetBarCodeList(BarCode model) {
        return mapper.GetBarCodeList(model);
    }

    @Override
    public Message PrintBarCode(BarCodeRequestDTO model, BarCodeInfoDTO infoDTO) {
        BarCode barCode = new BarCode();
        Message message = new Message();
        int num = model.getPrintNum();
        List<String> matrixAddrList = new ArrayList<>();
        try {
            TransferUtil.copyProperties(barCode, model);
            /**8位编码前面补0格式*/
            DecimalFormat g1 = new DecimalFormat("00000000");
            String currentCode = null;
            for (int i = 0; i < num; i++) {
                barCode.setId(null);
                //查询同一企业下同一店铺下同一供应商下同一鸡蛋类型的数量,初始应该为1(母二维码)
                int count = mapper.selectCount(new QueryWrapper<BarCode>()
                        .eq("shop_id", barCode.getShopId())
                        .eq("company_id", barCode.getCompanyId())
                        .eq("supplier_id", barCode.getSupplierId())
                        .eq("category_id", barCode.getCategoryId()));
                currentCode = barCode.getCode() + g1.format(count);
                infoDTO.setCurrentCode(currentCode);
                barCode.setCurrentCode(currentCode);
                String targetAddr = FileUploadProperteis.getMatrixImagePath(barCode.getCompanyId(),
                        barCode.getShopId(), barCode.getSupplierId(), barCode.getCategoryId());
                String text = JsonUtils.objectToJson(infoDTO);
                String matrixAddr = MatrixToImageWriterUtil.writeToFile(targetAddr, text, currentCode);
                barCode.setMatrixAddr(matrixAddr);
                mapper.insert(barCode);
                matrixAddrList.add(matrixAddr);
            }
            message.setData(matrixAddrList);
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
