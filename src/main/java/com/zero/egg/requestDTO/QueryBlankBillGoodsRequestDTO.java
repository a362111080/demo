package com.zero.egg.requestDTO;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName QueryBlankBillGoodsRequestDTO
 * @Author lyming
 * @Date 2019/5/18 15:01
 **/
@Data
public class QueryBlankBillGoodsRequestDTO implements Serializable {

    private static final long serialVersionUID = -7403854797655831582L;

    //出货任务id
    private String taskId;

    private String companyId;

    private String shopId;

}
