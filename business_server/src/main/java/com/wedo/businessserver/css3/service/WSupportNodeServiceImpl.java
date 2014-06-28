package com.wedo.businessserver.css3.service;

import java.io.Serializable;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.wedo.businessserver.common.exception.BusinessException;
import com.wedo.businessserver.common.exception.DataNotFoundException;
import com.wedo.businessserver.common.exception.GeneralException;
import com.wedo.businessserver.common.exception.ParameterException;
import com.wedo.businessserver.common.util.BaseStaticContextLoader;
import com.wedo.businessserver.css3.domain.WSupportNode;
import com.wedo.businessserver.css3.ws.model.SupportNode;
import com.wedo.businessserver.event.Publisher;
import com.wedo.businessserver.heartbeat.ClientFactory;

/**
 * CSP操作实现类
 * 
 * changed log : 20091205 by liuguoxia :1.新增CSP节点更新软链接符号方法updateCsp(WSupportNode
 * supportNode)，2.batchCreateCsp方法中，增加软链接处理。
 * 
 * @author c90003207
 * 
 */
@Service("wSupportNodeService")
public class WSupportNodeServiceImpl
    extends WOnlineAbstractServiceImpl
    implements WSupportNodeService, Serializable
{
    
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -8988418708504345494L;
    
    /**
     * 记录日志的时候用到的日志工具类
     */
    private static final Log logger =
        LogFactory.getLog(WSupportNodeServiceImpl.class);
    
    /**
     * 批量增加CSP
     * 
     * @param listSupportNode csp列表
     * @throws BusinessException Business Exception
     */
    public void batchCreateCsp(List<SupportNode> listSupportNode)
        throws BusinessException
    {
        try
        {
            // logger.info("进入新增节点处理");
            WSupportNode tempsupportNode = new WSupportNode();
            
            if (listSupportNode != null && listSupportNode.size() > 0)
            {
                String[] sqls = new String[listSupportNode.size()];
                // 批量组装sql
                for (int i = 0; i < listSupportNode.size(); i++)
                {
                    SupportNode supportNode = listSupportNode.get(i);
                    tempsupportNode.setHangsCarriesPath(supportNode
                        .getHangsCarriesPath());
                    sqls[i] =
                        "insert into ci_csp"
                            + "(guid,localIp,mountPath,mountguid,mountTime,invocation,hangsCarriesPath,csnguid)"
                            + "values(";
                    sqls[i] += "'" + supportNode.getGuid() + "',";// guid
                    sqls[i] += "'" + supportNode.getLocalIp() + "',";// localIp
                    sqls[i] += "'" + supportNode.getMountPath() + "',";// mountPath
                    sqls[i] += "'" + supportNode.getMountguid() + "',";// mountguid
                    sqls[i] += "'" + supportNode.getMountTime() + "',";// mountTime
                    sqls[i] += "'" + supportNode.getInvocation() + "',";// invocation
                    sqls[i] += "'" + supportNode.getHangsCarriesPath() + "',";// hangsCarriesPath
                    sqls[i] += "'" + supportNode.getCsnguid() + "')";// csnguid
                }
                if (sqls != null && sqls.length > 0)
                {
                    getJdbcTemplate().batchUpdate(sqls);// 批量新增CSP
                }
            }
            else
            {
                throw new ParameterException("ERROR.00201");
            }
        }
        catch (DataAccessException e)
        {
            logger.info(MSG.getString("ERROR.00050"));
            throw new DataNotFoundException("ERROR.00050", e);
        }
        catch (BusinessException e)
        {
            throw e;
        }
        catch (GeneralException e)
        {
            throw new BusinessException(e.getMessage());
        }
        catch (Exception e)
        {
            logger.info(MSG.getString("ERROR.00220"));
            throw new BusinessException("ERROR.00220", e);
        }
    }
    
    /**
     * 更新CSP
     * 
     * @param oldIp csp旧ip
     * @param newIp 新Ip
     *            ///param mountPath 挂载路径
     * @throws BusinessException Business Exception
     */
    public void updateCsp(String oldIp, String newIp)
        throws BusinessException
    {
        try
        {
            if (oldIp.equalsIgnoreCase(newIp))
            {
                throw new ParameterException("ERROR.00202");
            }
            logger.info("oldIp=" + oldIp);
            logger.info("newIp=" + newIp);
            String sql =
                "update ci_csp c set c.localIp='" + newIp
                    + "' where c.localIp='" + oldIp + "'";
            getJdbcTemplate().execute(sql);
        }
        catch (DataAccessException e)
        {
            logger.info(MSG.getString("ERROR.00050"));
            throw new DataNotFoundException("ERROR.00050", e);
        }
        catch (BusinessException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            logger.info(MSG.getString("ERROR.00221"));
            throw new BusinessException("ERROR.00221", e);
        }
    }
    
    /**
     * 批量更新
     * 
     * @param listSupportNode csp列表
     * @throws BusinessException Business Exception
     */
    public void batchUpdateCsp(List<SupportNode> listSupportNode)
        throws BusinessException
    {
        try
        {
            // logger.info("进入批量更新节点处理,总计节点数：" + listSupportNode.size());
            if (listSupportNode != null && listSupportNode.size() > 0)
            {
                String[] insertsqls = new String[listSupportNode.size()];
                String[] updatesqls = new String[listSupportNode.size()];
                // 批量组装sql
                for (int i = 0; i < listSupportNode.size(); i++)
                {
                    SupportNode supportNode = listSupportNode.get(i);
                    updatesqls[i] =
                        "update ci_csp c set c.hangsCarriesPath=null where c.localIp='"
                            + supportNode.getLocalIp() + "'";
                    insertsqls[i] =
                        "insert into "
                            + "ci_csp(guid,localIp,mountPath,mountguid,mountTime,"
                            + "invocation,hangsCarriesPath,csnguid)"
                            + "values(";
                    insertsqls[i] += "'" + supportNode.getGuid() + "',";// guid
                    insertsqls[i] += "'" + supportNode.getLocalIp() + "',";// localIp
                    insertsqls[i] += "'" + supportNode.getMountPath() + "',";// mountPath
                    insertsqls[i] += "'" + supportNode.getMountguid() + "',";// mountguid
                    insertsqls[i] += "'" + supportNode.getMountTime() + "',";// mountTime
                    insertsqls[i] += "'" + supportNode.getInvocation() + "',";// invocation
                    insertsqls[i] +=
                        "'" + supportNode.getHangsCarriesPath() + "',";// hangsCarriesPath
                    insertsqls[i] += "'" + supportNode.getCsnguid() + "')";// csnguid
                }
                if (updatesqls.length > 0)
                {
                    getJdbcTemplate().batchUpdate(updatesqls);// 批量更新
                }
                if (insertsqls.length > 0)
                {
                    getJdbcTemplate().batchUpdate(insertsqls);// 批量新增
                }
            }
            else
            {
                throw new ParameterException("ERROR.00201");
            }
        }
        catch (DataAccessException e)
        {
            logger.info(MSG.getString("ERROR.00050"));
            throw new DataNotFoundException("ERROR.00050", e);
        }
        catch (BusinessException e)
        {
            throw e;
        }
        catch (GeneralException e)
        {
            throw new BusinessException(e.getMessage());
        }
        catch (Exception e)
        {
            logger.info(MSG.getString("ERROR.00221"));
            throw new BusinessException("ERROR.00221", e);
        }
    }
    
    /**
     * 根据IP更新节点符号名（处理条件：linux系统）
     * 
     * @param supportNode CSP节点
     * @throws BusinessException Business Exception
     */
    public void updateCsp(WSupportNode supportNode)
        throws BusinessException
    {
        try
        {
            String sql = "update ci_csp c set c.symName=? where c.localIp=?";
            getJdbcTemplate().update(
                sql,
                new Object[] {supportNode.getSymName(),
                    supportNode.getLocalIp()});
        }
        catch (DataAccessException e)
        {
            logger.info(MSG.getString("ERROR.00050"));
            throw new DataNotFoundException("ERROR.00050", e);
        }
        catch (Exception e)
        {
            logger.info(MSG.getString("ERROR.00221"));
            throw new BusinessException("ERROR.00221", e);
        }
        
    }
    
    /**
     * 处理CSP节点的挂载
     * 
     * @param listSupportNode Support Nodes list
     * @throws BusinessException Business Exception
     */
    public void updateCspSym(List<SupportNode> listSupportNode)
        throws BusinessException
    {
        try
        {
            Properties props = System.getProperties();
            String osName = props.getProperty("os.name");
            if (osName.indexOf("Windows") != -1)
            {
                // TODO:windows暂不处理
                logger.info("windows暂不处理");
            }
            else
            {
                WSupportNode tempsupportNode = new WSupportNode();
                if (listSupportNode != null && listSupportNode.size() > 0)
                {
                    tempsupportNode.setHangsCarriesPath(listSupportNode.get(0)
                        .getHangsCarriesPath());
                    tempsupportNode.setCsnguid(listSupportNode.get(0)
                        .getCsnguid());
                    Publisher pub =
                        (Publisher) BaseStaticContextLoader
                            .getApplicationContext().getBean("publisher");
                    pub.publish(tempsupportNode);
                    String hangCarriesPath =
                        listSupportNode.get(0).getHangsCarriesPath();
                    String csnGuid = listSupportNode.get(0).getCsnguid();
                    
                    ClientFactory.run(
                        com.wedo.businessserver.heartbeat.Constants.CSP_UPDATE,
                        hangCarriesPath + "|" + csnGuid);// 组播发送地址给其他服务器
                }
            }
        }
        catch (Exception e)
        {
            logger.error("csp写入路径挂载，CSP节点更新错误");// csp写入路径挂载，CSP节点更新错误。
            throw new BusinessException("ERROR.00221", e);
        }
    }
    
}
