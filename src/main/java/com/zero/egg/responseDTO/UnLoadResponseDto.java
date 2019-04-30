package com.zero.egg.responseDTO;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class UnLoadResponseDto implements Serializable {

    private static final long serialVersionUID = -1406006311170704356L;

    //返回标识
    private String marker;
    //净重去皮数值，没有则为0
    private BigDecimal numerical;
}