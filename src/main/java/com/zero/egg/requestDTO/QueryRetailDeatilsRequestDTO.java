package com.zero.egg.requestDTO;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @ClassName QueryRetailDeatilsRequestDTO
 * @Description TODO
 * @Author lyming
 * @Date 2019-07-31 18:26
 **/
@Data
public class QueryRetailDeatilsRequestDTO implements Serializable {

    private static final long serialVersionUID = -9198496298546936628L;

    @NotNull(message = "账单id不能为空")
    private String billId;

    @NotNull(message ="合作商id不能为空")
    private String customerId;

    private String companyId;

    private String shopId;


}
