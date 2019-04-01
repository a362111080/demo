package com.zero.egg.service.impl;

import com.zero.egg.model.Company;
import com.zero.egg.dao.CompanyMapper;
import com.zero.egg.enums.CompanyEnums;
import com.zero.egg.service.ICompanyService;
import com.zero.egg.tool.UuidUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Hhaifeng
 * @since 2019-03-09
 */
@Service
public class CompanyServiceImpl extends ServiceImpl<CompanyMapper, Company> implements ICompanyService {

	@Override
	public boolean save(Company company) {
		company.setId(UuidUtil.get32UUID());
		company.setCreatetime(LocalDateTime.now());
		company.setModifytime(LocalDateTime.now());
		company.setStatus(CompanyEnums.Status.Normal.index().toString());
		company.setDr(false);
		return super.save(company);
	}
}
