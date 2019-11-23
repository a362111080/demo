package com.zero.egg.responseDTO;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName MarkerListResponseDTO
 * @Author lyming
 * @Date 2019/5/18 12:02
 **/
@Data
public class MarkerListResponseDTO implements Serializable {

    private static final long serialVersionUID = -1375165198226376178L;

    /**
     * 标记id
     */
    private String markerId;

    /**
     * 标记名
     */
    private String markerName;
}
