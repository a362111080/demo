package com.zero.egg.service;


import com.zero.egg.requestDTO.BarCodeListRequestDTO;
import com.zero.egg.requestDTO.BarCodeRequestDTO;
import com.zero.egg.tool.Message;

public interface BarCodeService {
    Message AddBarCode(BarCodeRequestDTO req);

    void DeleteBarCode(BarCodeRequestDTO model);

    Message GetBarCodeList(BarCodeListRequestDTO listRequestDTO);

    Message PrintBarCode(String barCodeId, int pageNum, String loginUserId);
}
