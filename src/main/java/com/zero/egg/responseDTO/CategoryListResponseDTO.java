package com.zero.egg.responseDTO;

import com.zero.egg.model.Category;
import com.zero.egg.tool.PageDTO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName CategoryListResponseDTO
 * @Author lyming
 * @Date 2018/11/6 22:49
 **/
@Data
public class CategoryListResponseDTO extends PageDTO implements Serializable {

    private static final long serialVersionUID = -2102658029082505709L;

    /**
     * 鸡蛋类型列表
     */
    private List<Category> categoryList;
}
