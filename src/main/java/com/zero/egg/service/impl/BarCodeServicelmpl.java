package com.zero.egg.service.impl;

import com.zero.egg.dao.BarCodeMapper;
import com.zero.egg.model.BarCode;
import com.zero.egg.requestDTO.BarCodeRequestDTO;
import com.zero.egg.service.BarCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class BarCodeServicelmpl implements BarCodeService {

    @Autowired
    private BarCodeMapper mapper;

    @Override
    public int AddBarCode(BarCodeRequestDTO req) {
        return  mapper.AddBarCode(req);
    }

    @Override
    public int DeleteBarCode(BarCodeRequestDTO model) {
        return mapper.DeleteBarCode(model.getIds());
    }

    @Override
    public List<BarCode> GetBarCodeList(BarCode model) {
        return  mapper.GetBarCodeList(model);
    }
}
