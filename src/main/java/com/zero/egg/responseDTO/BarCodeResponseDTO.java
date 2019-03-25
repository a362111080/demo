package com.zero.egg.responseDTO;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class BarCodeResponseDTO implements Serializable {

    private static final long serialVersionUID = -6504014074826436838L;

    private String id;

    private String shopId;

    private String shopName;

    private String companyId;

    private String companyName;

    private String code;

    private String supplierId;

    private String supplierName;

    private String categoryId;

    private String categoryName;

    private String currentCode;

    private String creator;

    private LocalDateTime createtime;

    private String modifier;

    private LocalDateTime modifytime;

    private Boolean dr;

    private String status;
}
