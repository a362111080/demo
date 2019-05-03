package com.zero.egg.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zero.egg.model.BarCode;
import com.zero.egg.requestDTO.BarCodeRequestDTO;
import com.zero.egg.responseDTO.BarCodeResponseDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface BarCodeMapper extends BaseMapper<BarCode> {
    int AddBarCode(BarCodeRequestDTO req);
    int DeleteBarCode(List<String> ids);

    List<BarCodeResponseDTO> GetBarCodeList(BarCode model);

    int PrintBarCode(BarCodeRequestDTO model);
}
