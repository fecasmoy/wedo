package com.wedo.businessserver.css3.service;

import java.util.Map;

import com.wedo.businessserver.common.exception.BusinessException;
import com.wedo.businessserver.css3.domain.WRepository;

/**
 * 仓库操作接口类
 * 
 * @author c90003207
 * 
 */
public interface WRepositoryService
{
    /**
     * 根据查询条件获得仓库
     * 
     * @param map map
     * @return value
     * @throws BusinessException Business Exception
     */
    public WRepository getCabinet(Map<String, String> map)
        throws BusinessException;
}
