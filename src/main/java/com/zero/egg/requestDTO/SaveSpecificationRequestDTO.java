package com.zero.egg.requestDTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName SaveSpecificationRequestDTO
 * @Description TODO
 * @Author lyming
 * @Date 2019/4/27 11:14 PM
 **/
@Data
public class SaveSpecificationRequestDTO implements Serializable {


    private static final long serialVersionUID = 3241556269525935473L;

    /**
     * 需要作保存操作的方案细节列表(新增和编辑)
     */
    @ApiModelProperty(value = "需要作保存操作的方案细节列表(新增和编辑)",required=false)
    List<SpecificationRequestDTO> specificationRequestDTOS;

    /**
     * 所属店铺id
     */
    @ApiModelProperty(value = "所属店铺id",required=false)
    private String shopId;

    /**
     * 店铺所属企业id
     */
    @ApiModelProperty(value = "店铺所属企业id",required=false)
    private String companyId;
}
