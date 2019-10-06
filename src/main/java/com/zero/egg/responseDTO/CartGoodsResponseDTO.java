package com.zero.egg.responseDTO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.zero.egg.model.OrderGoodsSpecification;
import com.zero.egg.tool.PageDTO;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @ClassName CartGoodsResponseDTO
 * @Description 购物车列表
 * @Author lyming
 * @Date 2019/10/4 6:58 下午
 **/
public class CartGoodsResponseDTO extends PageDTO implements Serializable {

    private static final long serialVersionUID = -7091983960267354574L;

    @TableId(value = "id", type = IdType.UUID)
    private String id;

    /**
     * 店铺id
     */
    private String shopId;

    /**
     * 用户表的用户ID
     */
    private String userId;

    /**
     * 该商品已经选择的规格id
     */
    private String goodSpecificationId;

    /**
     * 合作商计重习惯(1:去皮->按斤收费 2:包->按箱收费)
     * 如果按箱出货,所有货物默认按箱计算小计,如果按去皮出货,去皮卸的货按斤计算小计,包卸的货,默认还是按箱
     * 包卸的货,出货的时候,如果要转换成按斤计算小计,如要额外输入去皮值
     */
    private String weightMode;

    /**
     * 商品表的商品ID
     */
    private String goodsId;

    /**
     * 商品编号
     */
    private String goodsSn;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品货品的价格
     */
    private String price;

    /**
     * 商品货品的数量
     */
    private Integer number;

    /**
     * 购物车中商品是否选择状态
     */
    private Boolean checked;

    /**
     * 商品图片或者商品货品图片
     */
    private String picUrl;

    /**
     * 创建时间
     */
    private Date createtime;

    /**
     * 更新时间
     */
    private Date modifytime;

    /**
     * 逻辑删除
     */
    private Boolean dr;

    @ApiModelProperty(value = "商品规格列表",hidden=true)
    private List<OrderGoodsSpecification> sepcificationList;
}
