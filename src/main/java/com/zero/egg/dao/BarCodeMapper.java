package com.zero.egg.dao;

import com.zero.egg.model.BarCode;
import com.zero.egg.requestDTO.BarCodeRequestDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface BarCodeMapper {
    int AddBarCode(BarCodeRequestDTO req);
    int DeleteBarCode(List<String> ids);

    List<BarCode> GetBarCodeList(BarCode model);

}
