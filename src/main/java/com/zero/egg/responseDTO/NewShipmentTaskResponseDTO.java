package com.zero.egg.responseDTO;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName NewShipmentTaskResponseDTO
 * @Author lyming
 * @Date 2019/5/18 14:56
 **/
@Data
public class NewShipmentTaskResponseDTO implements Serializable {

    private static final long serialVersionUID = -3485428239616670771L;

    //空账单id
    private String billId;

    //相关联出货任务id
    private String taskId;

}
