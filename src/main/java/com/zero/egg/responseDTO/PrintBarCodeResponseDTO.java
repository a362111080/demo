package com.zero.egg.responseDTO;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName PrintBarCodeResponseDTO
 * @Author lyming
 * @Date 2019/5/5 11:41 PM
 **/
@Data
public class PrintBarCodeResponseDTO implements Serializable {

    private static final long serialVersionUID = -1642081156744318166L;

    private List<SinglePrintBarCodeDTO> printBarCodeDTOS ;

}
