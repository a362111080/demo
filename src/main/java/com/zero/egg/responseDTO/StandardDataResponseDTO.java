package com.zero.egg.responseDTO;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 方案ResponseDTO
 *
 * @ClassName StandardDataResponseDTO
 * @Author lyming
 * @Date 2018/11/10 4:27
 **/
@Data
public class StandardDataResponseDTO implements Serializable {

    private static final long serialVersionUID = -7358584118213590121L;

    /**
     * 方案列表
     */
    private List<StandardDataListResponseDTO> standardDataList;
}
