package com.zero.egg.responseDTO;

import com.zero.egg.model.StandardDetl;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 方案细节ResponseDTO
 *
 * @ClassName StandardDetlResponseDTO
 * @Author lyming
 * @Date 2018/11/10 4:08
 **/
@Data
public class StandardDetlResponseDTO implements Serializable {

    private static final long serialVersionUID = -384371864812207524L;

    /**
     * 方案细节列表
     */
    private List<StandardDetl> standardDetlList;
}
