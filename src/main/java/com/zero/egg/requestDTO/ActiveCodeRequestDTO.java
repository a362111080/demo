package com.zero.egg.requestDTO;

import lombok.Data;

@Data
public class ActiveCodeRequestDTO{
    // 激活员工ID
    private String active_usercode;
    // 激活员工ID
    private String active_stroecode;
    // 激活码
    private String certificatecode;
}
