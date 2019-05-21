package com.zero.egg.requestDTO;

import com.zero.egg.responseDTO.BlankBillGoodsDetail;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName BlankBillGoodsRequestDTO
 * @Author lyming
 * @Date 2019/5/18 15:31
 **/
@Data
public class BlankBillGoodsRequestDTO implements Serializable {

    private static final long serialVersionUID = -2196896469807780928L;

    private String categoryId;

    private String categoryName;

    List<BlankBillGoodsDetail> blankBillGoodsDetailList;
}
