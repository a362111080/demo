package com.zero.egg.responseDTO;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CategorySum {

    //品种id
    private  String goodsCategoryId;

    //品种
    private  String goodsCategoryName;

    //数量
    private BigDecimal CountNum=BigDecimal.ZERO;
}
