package com.zero.egg.requestDTO;

import com.zero.egg.responseDTO.GoodsResponse;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author lym
 */
@Data
public class RemShipmentGoodsRequestDTO implements Serializable {

    private static final long serialVersionUID = 8796787814767220641L;

    /**
     * 需要移除的出货商品集合
     */
    private List<GoodsResponse> goodsResponseList;

    /**
     * 任务id
     */
    private String taskId;

    /**
     * 合作商id
     */
    private String customerId;
}
