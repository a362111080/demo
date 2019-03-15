package com.zero.egg.service;


import com.zero.egg.model.BarCode;
import com.zero.egg.requestDTO.BarCodeRequestDTO;
import com.zero.egg.responseDTO.BarCodeResponseDTO;

import java.util.List;

public interface BarCodeService {
    int AddBarCode(BarCodeRequestDTO req);

    int DeleteBarCode(BarCodeRequestDTO model);

    List<BarCodeResponseDTO> GetBarCodeList(BarCode model);
}
