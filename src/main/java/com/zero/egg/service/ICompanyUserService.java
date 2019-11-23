package com.zero.egg.service;

import com.zero.egg.model.CompanyUser;
import com.zero.egg.requestDTO.CompanyUserRequest;
import com.zero.egg.requestDTO.LoginUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zero.egg.responseDTO.CompanyinfoResponseDto;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Hhaifeng
 * @since 2019-03-11
 */
public interface ICompanyUserService extends IService<CompanyUser> {

    List<CompanyinfoResponseDto> getCompanyinfolist(CompanyUserRequest model);
}
