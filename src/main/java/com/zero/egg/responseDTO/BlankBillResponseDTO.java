package com.zero.egg.responseDTO;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName BlankBillResponseDTO
 * @Author lyming
 * @Date 2019/5/20 20:39
 **/
@Data
public class BlankBillResponseDTO implements Serializable {

    private static final long serialVersionUID = 3420993517274939392L;

    List<BlankBillGoodsResponseDTO> billGoodsResponseDTOS;
}
