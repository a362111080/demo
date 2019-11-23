package com.zero.egg.requestDTO;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

/**
 * @ClassName RemAddressRequestDTO
 * @Description 删除收货地址RequestDTO
 * @Author lyming
 * @Date 2019/8/26 3:54 下午
 **/
@Data
public class RemAddressRequestDTO implements Serializable {

    private static final long serialVersionUID = -3111990760670183365L;

    @NotEmpty(message ="至少需要选择一个待删除地址")
    private List<String> ids;
}
