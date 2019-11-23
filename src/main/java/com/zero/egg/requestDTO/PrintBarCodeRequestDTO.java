package com.zero.egg.requestDTO;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName PrintBarCodeRequestDTO
 * @Description TODO
 * @Author lyming
 * @Date 2019/5/5 11:13 PM
 **/
@Data
public class PrintBarCodeRequestDTO implements Serializable {

    private static final long serialVersionUID = 3956942354261637732L;

    /**
     * 母二维码地址
     */
    private String matrixAddr;

    /**
     * 需要打印的数量
     */
    private Integer printNum;
}
