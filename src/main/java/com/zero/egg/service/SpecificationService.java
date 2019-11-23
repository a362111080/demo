package com.zero.egg.service;

import com.zero.egg.model.Specification;
import com.zero.egg.requestDTO.SpecificationRequestDTO;
import com.zero.egg.tool.Message;

import java.util.List;

/**
 * 方案细节Service
 *
 * @Author lyming
 * @Date 2018/11/9 17:01
 **/
public interface SpecificationService {

    /**
     * 新增一条方案细节
     *
     * @Param [specificationRequestDTO]
     * @Return com.zero.egg.tool.Message
     **/
    public Message addStandardDetl(SpecificationRequestDTO specificationRequestDTO);

    /**
     * 修改方案细节
     *
     * @Param [specificationRequestDTO]
     * @Return com.zero.egg.tool.Message
     **/
    public Message updateStandardDetl(SpecificationRequestDTO specificationRequestDTO);

    /**
     * 批量删除方案细节
     *
     * @Param [specificationRequestDTO]
     * @Return com.zero.egg.tool.Message
     **/
    public Message batchDeleteStandardDetl(SpecificationRequestDTO specificationRequestDTO);

    /**
     * 根据program_id列出所有方案细节
     *
     * @Param [specificationRequestDTO]
     * @Return com.zero.egg.tool.Message
     **/
    public Message listStandardDetlByProgramId(SpecificationRequestDTO specificationRequestDTO);

    /**
     * 根据program_id列出所有方案细节id
     *
     * @return
     */
    public List<String> listStandardDetlIDsByProgramId(String programId, String companyId, String shopId);
    
    public Specification getById(Specification specification);

    /**
     * 根据id集合批量删除方案细节
     *
     * @param ids
     */
    void batchDeleteStandardDetlByIds(List<String> ids);
}
