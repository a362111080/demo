package com.zero.egg.service;


import com.zero.egg.model.BarCode;
import com.zero.egg.requestDTO.BarCodeRequestDTO;

import java.util.List;

public interface BarCodeService {
    int AddBarCode(BarCodeRequestDTO req);

    int DeleteBarCode(BarCodeRequestDTO model);

    List<BarCode> GetBarCodeList(BarCode model);
}
