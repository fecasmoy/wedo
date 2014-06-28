package com.wedo.businessserver.cache;

import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;

import com.wedo.businessserver.common.dao.EntityDao;
import com.wedo.businessserver.common.dao.support.DAOUtil;
import com.wedo.businessserver.common.exception.BusinessException;
import com.wedo.businessserver.common.exception.DataNotFoundException;
import com.wedo.businessserver.common.util.LanguageUtil;
import com.wedo.businessserver.css3.domain.WSupportNode;

/**
 * 获取本机CSP记录单例
 * 
 * @author l00100468
 */
public class SupportNode
{
    //WSupportNode 实例
    private static WSupportNode instance = null;
    
    // 记录日志的时候用到的日志工具类
    private static final Log logger = LogFactory.getLog(SupportNode.class);
    
    // 本地日志国际化
    private static ResourceBundle msg = LanguageUtil.getMessage();
    
    /**
     * 私有构造函数
     */
    private SupportNode()
    {
        
    }
    
    /**
     * get entity dao
     * 
     * @return return value dao
     */
    public static EntityDao<WSupportNode> getWSupportNodeEntityDao()
    {
        EntityDao<WSupportNode> entityDao =
            DAOUtil.getEntityDao(WSupportNode.class);
        return entityDao;
    }
    
    /**
     * 查询获得CSP单例
     * 
     * @author l00100468
     * @return node
     * @throws BusinessException Business Exception
     */
    public static synchronized WSupportNode getInstance()
        throws BusinessException
    {
        if (instance == null)
        {
            try
            {
                String ip = com.wedo.businessserver.storage.jcr.Constants.LOCALIP;
                logger.info(msg.getString("msg.csp.get") + "  ip  :" + ip); // 获取CSP单例
                String hql =
                    "from WSupportNode c where c.localIp=? "
                        + "and c.hangsCarriesPath is not null"; // 获得当前可写入得cspsql
                // 返回单例
                instance =
                    getWSupportNodeEntityDao().getByhql(hql, new Object[] {ip});
            }
            catch (DataAccessException e)
            {
                logger.info(msg.getString("ERROR.00050"), e);
                throw new DataNotFoundException("ERROR.00050");
            }
            catch (Exception e)
            {
                logger.info("ERROR.00222", e); // CSP查询失败
                throw new BusinessException("ERROR.00222");
            }
        }
        return instance;
    }
    
    /**
     * 更新CSP单例
     * 
     * @author l00100468
     */
    public static void updateInstance()
    {
        if (instance != null)
        {
            instance = null;
        }
    }
}
