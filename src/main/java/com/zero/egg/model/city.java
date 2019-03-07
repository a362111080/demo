package com.zero.egg.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "bd_citys")
public class city {
    /**
     * @ClassName 省市区类
     * @Description 省市区类
     * @Author CQ
     * @Date 2019/03/06
     **/

    /**
     * 省市区id
     */
    private  String id;
    /**
     * 省市区父级id
     */
    private  String parentId;
    /**
     * 省市区名称
     */
    private  String name;
    /**
     * 省市区分类层级
     */
    private  int LevelType;
}
