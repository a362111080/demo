package com.zero.egg.responseDTO;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName BlankBillGoodsResponseDTO
 * @Author lyming
 * @Date 2019/5/18 15:31
 **/
@Data
public class BlankBillGoodsResponseDTO implements Serializable {

    private static final long serialVersionUID = 5753293101138246345L;

    private String categoryId;

    private String categoryName;

    List<BlankBillGoodsDetail> blankBillGoodsDetailList;
}
