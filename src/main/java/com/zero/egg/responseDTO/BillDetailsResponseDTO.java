package com.zero.egg.responseDTO;

import com.zero.egg.model.BillDetails;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @ClassName BillDetailsResponseDTO
 * @Description TODO
 * @Author lyming
 * @Date 2019-07-19 02:41
 **/
@Data
public class BillDetailsResponseDTO implements Serializable {

    private static final long serialVersionUID = 5549779329245608867L;

    private List<BillDetails> billDetailsList;

    /**
     * 实收金额
     */
    private BigDecimal realAmount;

    /**
     * 应收金额
     */
    private BigDecimal amount;
}
