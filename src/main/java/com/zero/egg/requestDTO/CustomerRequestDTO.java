package com.zero.egg.requestDTO;

import com.zero.egg.tool.PageDTO;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 客户RequestDTO
 *
 * @ClassName CustomerRequestDTO
 * @Author CQ
 * @Date 2018/11/6 16:25
 **/
@Data
public class CustomerRequestDTO extends PageDTO implements Serializable {


    private static final long serialVersionUID = 9090211475733425568L;

    /**
     * 主键id
     */
    private String id;

    /**
     * 修改人
     */
    private String modifier;

    /**
     * 修改时间
     */
    private Date modifytime;


    /**
     * 所属店铺id
     */
    private String shopId;

    /**
     * 店铺所属企业id
     */
    private String companyId;

    /**
     * 删除标识 0:停用 1:启用(默认)
     */
    private Integer dr = 0;

    /**
     * 前端传来的id数组(批量操作)
     */
    private List<String> ids;
}
