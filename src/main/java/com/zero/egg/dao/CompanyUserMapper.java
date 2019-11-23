package com.zero.egg.dao;

import com.zero.egg.model.CompanyUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zero.egg.requestDTO.CompanyUserRequest;
import com.zero.egg.responseDTO.CompanyinfoResponseDto;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Hhaifeng
 * @since 2019-03-11
 */
public interface CompanyUserMapper extends BaseMapper<CompanyUser> {

    List<CompanyinfoResponseDto> getCompanyinfolist(CompanyUserRequest model);
}
