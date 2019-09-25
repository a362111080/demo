package com.zero.egg.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zero.egg.dao.CompanyUserMapper;
import com.zero.egg.enums.CompanyUserEnums;
import com.zero.egg.model.CompanyUser;
import com.zero.egg.requestDTO.CompanyUserRequest;
import com.zero.egg.responseDTO.CompanyinfoResponseDto;
import com.zero.egg.service.ICompanyUserService;
import com.zero.egg.tool.UuidUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Hhaifeng
 * @since 2019-03-11
 */
@Service
public class CompanyUserServiceImpl extends ServiceImpl<CompanyUserMapper, CompanyUser> implements ICompanyUserService {

	@Override
	public boolean save(CompanyUser entity) {
		entity.setId(UuidUtil.get32UUID());
		entity.setCreatetime(new Date());
		entity.setModifytime(new Date());
		entity.setStatus(CompanyUserEnums.Status.Normal.index().toString());
		entity.setDr(false);
		if (StringUtils.isNotBlank(entity.getPassword())) {
			try {
				entity.setPassword(entity.getPassword());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return super.save(entity);
	}

	@Autowired
	private CompanyUserMapper mapper;

	@Override
	public List<CompanyinfoResponseDto> getCompanyinfolist(CompanyUserRequest model) {
		return mapper.getCompanyinfolist(model);
	}
}
