package com.zero.egg.service;


import com.zero.egg.model.BarCodeInfoDTO;
import com.zero.egg.requestDTO.BarCodeListRequestDTO;
import com.zero.egg.requestDTO.BarCodeRequestDTO;
import com.zero.egg.tool.Message;

public interface BarCodeService {
    Message AddBarCode(BarCodeRequestDTO req);

    int DeleteBarCode(BarCodeRequestDTO model);

    Message GetBarCodeList(BarCodeListRequestDTO listRequestDTO);

    Message PrintBarCode(BarCodeRequestDTO model, BarCodeInfoDTO infoDTO, int pageNum);
}
