package com.zero.egg.service;


import com.zero.egg.model.BarCode;
import com.zero.egg.model.BarCodeInfoDTO;
import com.zero.egg.requestDTO.BarCodeRequestDTO;
import com.zero.egg.responseDTO.BarCodeResponseDTO;
import com.zero.egg.tool.Message;

import java.util.List;

public interface BarCodeService {
    Message AddBarCode(BarCodeRequestDTO req);

    int DeleteBarCode(BarCodeRequestDTO model);

    List<BarCodeResponseDTO> GetBarCodeList(BarCode model);

    Message PrintBarCode(BarCodeRequestDTO model, BarCodeInfoDTO infoDTO);
}
