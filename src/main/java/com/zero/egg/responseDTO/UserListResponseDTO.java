package com.zero.egg.responseDTO;

import com.zero.egg.model.User;
import lombok.Data;

/**
 * @ClassName UserListResponseDTO
 * @Description TODO
 * @Author lyming
 * @Date 2019-08-02 02:01
 **/
@Data
public class UserListResponseDTO extends User {

    private String shopName;
}
