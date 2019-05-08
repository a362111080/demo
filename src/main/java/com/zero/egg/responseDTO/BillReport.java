package com.zero.egg.responseDTO;

import io.swagger.annotations.ApiModelProperty;

import java.util.Dictionary;
import java.util.List;

public class BillReport {

    @ApiModelProperty(value = "总收入")
    private String InCount;

    @ApiModelProperty(value = "总支出")
    private String OutCount;

    @ApiModelProperty(value = "总进货数量按品种")
    private List<CategorySum> InCategorySum;

    @ApiModelProperty(value = "总出货数量按品种")
    private List<CategorySum> OutCategorySum;


    public  class   CategorySum
    {
        //品种
        private  String goodsCategoryId;

        //数量
        private  Integer CountNum;
    }

    @ApiModelProperty(value = "已结清数量")
    private int CompleteCount;

    @ApiModelProperty(value = "未结清数量")
    private int UnCompleteCount;
}


