package com.zero.egg.responseDTO;

import com.zero.egg.model.BarCode;
import com.zero.egg.tool.PageDTO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class BarCodeListResponseDTO extends PageDTO implements Serializable {

    private static final long serialVersionUID = -6504014074826436838L;

    private List<BarCode> barCodeList;
}
