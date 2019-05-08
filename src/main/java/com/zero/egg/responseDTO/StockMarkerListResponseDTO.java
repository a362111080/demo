package com.zero.egg.responseDTO;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 库存所有标记列表
 *
 * @ClassName StockMarkerListResponseDTO
 * @Author lyming
 * @Date 2019/5/7 13:03
 **/
@Data
public class StockMarkerListResponseDTO implements Serializable {

    private static final long serialVersionUID = -4460233471917870628L;

    private List<String> markerList;
}
