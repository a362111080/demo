package com.zero.egg.responseDTO;

import lombok.Data;

@Data
public class UnLoadCountResponseDto {
    private static final long serialVersionUID = 6957017767264523082L;

    //返回标识
    private String marker;
    //净重去皮数值，没有则为0
    private int Count;
}
