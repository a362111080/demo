package com.zero.egg.requestDTO;

import com.zero.egg.tool.PageDTO;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 鸡蛋类型RequestDTO
 *
 * @ClassName saveEggTypeRequestDTO
 * @Author lyming
 * @Date 2018/11/6 16:25
 **/
@Data
public class EggTypeRequestDTO extends PageDTO implements Serializable {


    private static final long serialVersionUID = -1867113411973002512L;

    /**
     * 主键id
     */
    private String id;

    /**
     * 鸡蛋类别编码
     */
    private String strEggTypeCode;

    /**
     * 鸡蛋类别名称
     */
    private String strEggTypeName;

    /**
     * 类别创建时间
     */
    private Date strTypeCreateTime;

    /**
     * 创建人
     */
    private String strCreateUser;

    /**
     * 状态 0:停用 1:启用(默认)
     */
    private Integer LngState = 1;

    /**
     * 前端传来的id数组(批量操作)
     */
    private List<String> ids;
}
