package com.zero.egg.responseDTO;

import com.zero.egg.model.Specification;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 方案细节ResponseDTO
 *
 * @ClassName SpecificationResponseDTO
 * @Author lyming
 * @Date 2018/11/10 4:08
 **/
@Data
public class SpecificationResponseDTO implements Serializable {

    private static final long serialVersionUID = -384371864812207524L;

    /**
     * 方案细节列表
     */
    private List<Specification> specificationList;
}
