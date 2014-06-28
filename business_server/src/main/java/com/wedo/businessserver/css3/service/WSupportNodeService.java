package com.wedo.businessserver.css3.service;

import java.util.List;

import com.wedo.businessserver.common.exception.BusinessException;
import com.wedo.businessserver.css3.domain.WSupportNode;
import com.wedo.businessserver.css3.ws.model.SupportNode;

/**
 * CSP操作接口类
 * 
 * @author c90003207
 * 
 */
public interface WSupportNodeService
{
    /**
     * 批量增加CSP
     * 
     * @param listSupportNode csp列表
     * @throws BusinessException BusinessException
     */
    public void batchCreateCsp(List<SupportNode> listSupportNode)
        throws BusinessException;
    
    /**
     * 更新CSP
     * 
     * @param oldIp csp旧ip
     * @param newIp 新Ip
     *            /////param mountPath 挂载路径
     * @throws BusinessException BusinessException
     */
    public void updateCsp(String oldIp, String newIp)
        throws BusinessException;
    
    /**
     * 批量更新
     * 
     * @param listSupportNode csp列表
     * @throws BusinessException BusinessException
     */
    public void batchUpdateCsp(List<SupportNode> listSupportNode)
        throws BusinessException;
    
    /**
     * 根据IP查询列表
     * 
     * @param ip ip地址
     * @return WSupportNode List
     * @throws BusinessException BusinessException
     */
    public List<WSupportNode> getWSupportByIp(String ip)
        throws BusinessException;
    
    /**
     * 更新CSP记录
     * 
     * @param wSupportNode wSupportNode
     * @throws BusinessException Business Exception
     */
    public void updateCsp(WSupportNode wSupportNode)
        throws BusinessException;
    
    /**
     * CSP节点的挂载变化软链接组播处理（处理条件：linux系统csp节点）
     * 
     * @param listSupportNode support node
     * @throws BusinessException BusinessException
     */
    public void updateCspSym(List<SupportNode> listSupportNode)
        throws BusinessException;
    
}
