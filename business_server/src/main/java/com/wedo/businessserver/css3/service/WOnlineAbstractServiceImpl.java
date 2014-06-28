package com.wedo.businessserver.css3.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import com.wedo.businessserver.common.dao.EntityDao;
import com.wedo.businessserver.common.dao.support.DAOUtil;
import com.wedo.businessserver.common.exception.BusinessException;
import com.wedo.businessserver.common.exception.DataNotFoundException;
import com.wedo.businessserver.common.exception.ParameterException;
import com.wedo.businessserver.common.util.BaseStaticContextLoader;
import com.wedo.businessserver.common.util.LanguageUtil;
import com.wedo.businessserver.css3.domain.WFile;
import com.wedo.businessserver.css3.domain.WFileHistory;
import com.wedo.businessserver.css3.domain.WFolder;
import com.wedo.businessserver.css3.domain.WRepository;
import com.wedo.businessserver.css3.domain.WSupportNode;
import com.wedo.businessserver.css3.domain.Wapp;
import com.wedo.businessserver.storage.jcr.service.JCRService;

/**
 * 在线存储公共基类
 * 
 * @author c90003207
 * 
 */
public abstract class WOnlineAbstractServiceImpl
{
    
    /** 文件夹分割串 */
    protected static final String FOLDERSPLIT = "|";
    
    /**
     * logger
     */
    protected static final Log logger =
        LogFactory.getLog(WOnlineAbstractServiceImpl.class);
    
    /**
     * message
     */
    protected static final ResourceBundle MSG = LanguageUtil.getMessage();
    
    /**
     * jcr service
     */
    private JCRService jCRService;
    
    /**
     * jdbc template
     */
    private JdbcTemplate jdbcTemplate;
    
    /**
     * getter of jcr service
     * 
     * @return return value
     */
    public JCRService getJCRService()
    {
        if (jCRService == null)
        {
            jCRService =
                (JCRService) BaseStaticContextLoader.getApplicationContext()
                    .getBean("jCRService");
        }
        return jCRService;
    }
    
    /**
     * set jcr service
     * 
     * @param service service
     */
    public void setJCRService(JCRService service)
    {
        jCRService = service;
    }
    
    /**
     * getter
     * 
     * @return return value
     */
    public JdbcTemplate getJdbcTemplate()
    {
        if (jdbcTemplate == null)
        {
            jdbcTemplate =
                (JdbcTemplate) BaseStaticContextLoader.getApplicationContext()
                    .getBean("jdbcTemplate");
        }
        return jdbcTemplate;
    }
    
    /**
     * setter
     * 
     * @param jdbcTemplate jdbcTemplate
     */
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate)
    {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    /**
     * getter
     * 
     * @return return value
     */
    public static EntityDao<WSupportNode> getWSupportNodeEntityDao()
    {
        EntityDao<WSupportNode> entityDao =
            DAOUtil.getEntityDao(WSupportNode.class);
        return entityDao;
    }
    
    /**
     * getter
     * 
     * @return return value
     */
    public static EntityDao<WFolder> getFolderEntityDao()
    {
        EntityDao<WFolder> entityDao = DAOUtil.getEntityDao(WFolder.class);
        return entityDao;
    }
    
    /**
     * getter
     * 
     * @return return value
     */
    public static EntityDao<WFile> getFileEntityDao()
    {
        EntityDao<WFile> entityDao = DAOUtil.getEntityDao(WFile.class);
        return entityDao;
    }
    
    /**
     * getter
     * 
     * @return return value
     */
    public static EntityDao<WFileHistory> getFileHistoryEntityDao()
    {
        EntityDao<WFileHistory> entityDao =
            DAOUtil.getEntityDao(WFileHistory.class);
        return entityDao;
    }
    
    /**
     * getter
     * 
     * @return return value
     */
    public static EntityDao<WRepository> getRepositoryEntityDao()
    {
        EntityDao<WRepository> entityDao =
            DAOUtil.getEntityDao(WRepository.class);
        return entityDao;
    }
    
    /**
     * getter
     * 
     * @return return value
     */
    public static EntityDao<Wapp> getWappEntityDao()
    {
        EntityDao<Wapp> entityDao = DAOUtil.getEntityDao(Wapp.class);
        return entityDao;
    }
    
    /**
     * 仓库获取
     * 
     * @param map appId、repuuid
     * @return return value
     * @throws BusinessException business exception
     */
    public WRepository getCabinet(Map<String, String> map)
        throws BusinessException
    {
        String appId = map
            .get("appId");
        String repuuid = map
            .get("repuuid");
        try
        {
            EntityDao<WRepository> entityDao = getRepositoryEntityDao();
            Criteria criteria = entityDao
                .getEntityCriteria();
            if (StringUtils
                .isNotBlank(appId))
            {
                criteria
                    .add(Restrictions
                        .eq("appGuid", appId));// 应用appid
            }
            if (StringUtils
                .isNotBlank(repuuid))
            {
                criteria
                    .add(Restrictions
                        .eq("repuuid", repuuid));// 仓库uuid
            }
            WRepository repository = (WRepository) entityDao
                .getByQbc(criteria);
            return repository;
        }
        catch (Exception e)
        {
            logger
                .error("Error appid =" + appId);
            logger
                .error("Error repuuid =" + repuuid);
            throw new BusinessException("ERROR.00312", e);
        }
    }
    
    /**
     * 文件夹获取
     * 
     * @param map map
     * @return return value
     * @throws BusinessException Exception
     */
    public WFolder getFolder(Map<String, String> map)
        throws BusinessException
    {
        String avaible = map.get("avaible");
        String folderuuid = map.get("folderuuid");
        String appguid = map.get("appguid");
        String parentGuid = map.get("parentGuid");
        String guid = map.get("guid");
        try
        {
            EntityDao<WFolder> entityDao = getFolderEntityDao();
            Criteria criteria = entityDao.getEntityCriteria();
            if (StringUtils.isNotBlank(avaible))
            {
                criteria.add(Restrictions.eq("avaible", avaible));// 是否可用
            }
            if (StringUtils.isNotBlank(folderuuid))
            {
                criteria.add(Restrictions.eq("folderUuid", folderuuid));// 文件夹uuid
            }
            if (StringUtils.isNotBlank(appguid))
            {
                criteria.add(Restrictions.eq("appGuid", appguid));// 应用appid
            }
            if (StringUtils.isNotBlank(parentGuid))
            {
                criteria.add(Restrictions.eq("paguid", parentGuid));// 父文件夹guid
            }
            if (StringUtils.isNotBlank(guid))
            {
                criteria.add(Restrictions.eq("guid", guid));// guid
            }
            return (WFolder) entityDao.getByQbc(criteria);
        }
        catch (DataAccessException e)
        {
            logger.info(MSG.getString("ERROR.00410"));
            logger.error("appguid =" + appguid);
            logger.error("parentGuid =" + parentGuid);
            logger.error("folderguid =" + guid);
            logger.error("folderuuid =" + folderuuid);
            logger.error("avaible =" + avaible);
            throw new BusinessException("ERROR.00410", e);
        }
    }
    
    /**
     * 得到当前最新文件
     * 
     * @param map map
     * @return return value
     * @throws BusinessException Exception
     */
    public WFile getWFile(Map<String, String> map)
        throws BusinessException
    {
        try
        {
            // logger.info("开始查找最新文件");
            EntityDao<WFile> entityDao = getFileEntityDao();
            Criteria criteria = entityDao.getEntityCriteria();
            String appguid = map.get("appguid");
            if (StringUtils.isNotBlank(appguid))
            {
                criteria.add(Restrictions.eq("appGuid", appguid));// 应用appid
            }
            String avaible = map.get("avaible");
            if (StringUtils
                .isNotBlank(avaible))
            {
                criteria.add(Restrictions.eq("avaible", avaible));// 是否可用
            }
            String fileuuid = map.get("fileuuid");
            if (StringUtils.isNotBlank(fileuuid))
            {
                criteria.add(Restrictions.eq("fileuuid", fileuuid));// 文件uuid
            }
            WFile wfile = entityDao.getByQbc(criteria);
            return wfile;
        }
        catch (Exception e)
        {
            logger.info(MSG.getString("ERROR.00510"));
            throw new BusinessException("ERROR.00510", e);
        }
    }
    
    /**
     * 得到历史文件
     * 
     * @param map map
     * @return return value
     * @throws BusinessException Exception
     */
    public WFileHistory getWFileHistory(Map<String, String> map)
        throws BusinessException
    {
        try
        {
            // logger.info("开始找查历史文件");
            Criteria criteria = getFileHistoryEntityDao().getEntityCriteria();
            String appguid = map.get("appguid");
            if (StringUtils.isNotBlank(appguid))
            {
                criteria.add(Restrictions.eq("appGuid", appguid));// 应用appid
            }
            String avaible = map.get("avaible");
            if (StringUtils.isNotBlank(avaible))
            {
                criteria.add(Restrictions.eq("avaible", avaible));// 是否可用
            }
            String fileuuid = map.get("fileuuid");
            if (StringUtils.isNotBlank(fileuuid))
            {
                criteria.add(Restrictions.eq("fileuuid", fileuuid));// 文件uuid
            }
            String version = map.get("version");
            if (StringUtils.isNotBlank(version))
            {
                criteria.add(Restrictions.eq("version", version));// 文件版本
            }
            WFileHistory wFileHistory =
                getFileHistoryEntityDao().getByQbc(criteria);
            return wFileHistory;
        }
        catch (Exception e)
        {
            logger.info(MSG.getString("ERROR.00510"));
            throw new BusinessException("ERROR.00510", e);
        }
    }
    
    /**
     * 根据应用AppGuid获得应用
     * 
     * @param appId appId
     * @return return value
     * @throws BusinessException Business Exception
     */
    public Wapp getApp(String appId)
        throws BusinessException
    {
        try
        {
            if (StringUtils.isBlank(appId))
            {
                throw new ParameterException("ERROR.00301");
            }
            EntityDao<Wapp> entityDao = getWappEntityDao();
            Criteria criteria = entityDao.getEntityCriteria();
            criteria.add(Restrictions.eq("guid", appId));// 应用appid
            Wapp wapp = entityDao.getByQbc(criteria);
            return wapp;
        }
        catch (Exception e)
        {
            logger.error(MSG.getString("ERROR.00310"));
            logger.error("Error appid =" + appId);
            throw new BusinessException("ERROR.00310", e);
        }
    }
    
    /**
     * 上传校验文件是否重名
     * 
     * @param appId appId
     * @param cabinetuuid cabinetuuid
     * @param folderuuid folderuuid
     * @param filename filename
     * @return return value
     * @throws BusinessException Business Exception
     */
    public HashMap<String, String> uploadvlidateName(String appId,
        String cabinetuuid, String folderuuid, String filename)
        throws BusinessException
    {
        // logger.info("验证soap上传开始");
        logger.debug("appId=" + appId);
        logger.debug("cabinetuuid=" + cabinetuuid);
        logger.debug("folderuuid=" + folderuuid);
        logger.debug("filename=" + filename);
        String folderGuid = null;
        String flag = "false";
        String fileuuid = null;
        HashMap<String, String> map = new HashMap<String, String>();
        try
        {
            Map<String, String> cabinetMap = new HashMap<String, String>();
            cabinetMap.put("appId", appId);// 应用appid
            cabinetMap.put("repuuid", cabinetuuid);// 仓库uuid
            WRepository wrepository = getCabinet(cabinetMap);
            map.put("wrepositoryguid", wrepository.getGuid());
            if (StringUtils.isNotBlank(folderuuid))
            {
                folderGuid =
                    (String) getJdbcTemplate()
                        .queryForObject(
                            "select c.guid from ci_folder c "
                                + "where c.avaible=? and c.folderuuid=? and c.appguid=?",
                            new Object[] {SystemParameter.FILE_FOLDER_AVIABLES,
                                folderuuid, appId}, String.class);// 如果父文件夹不为空，获得父文件夹guid
            }
            else
            {
                folderGuid = wrepository.getGuid();// 如果父文件夹为空,则取仓库guid
            }
            map.put("folderGuid", folderGuid);
            try
            {
                fileuuid =
                    (String) getJdbcTemplate()
                        .queryForObject(
                            "select c.fileuuid from ci_file c "
                                + "where  c.avaible=?  and c.folderguid=? and  c.fName=?",
                            new Object[] {SystemParameter.FILE_FOLDER_AVIABLES,
                                folderGuid, filename}, String.class);
                map.put("fileuuid", fileuuid);
                flag = "true";// 如果重名，则标志位为true
            }
            catch (DataAccessException e)
            {
                logger.info("Data Access Exception ocur.");
            }
            map.put("flag", flag);// 文件不重名
            return map;
        }
        catch (DataAccessException e)
        {
            logger.info(MSG.getString("ERROR.00050"));
            throw new BusinessException("ERROR.00050", e);
        }
        catch (BusinessException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            logger.info(MSG.getString("ERROR.00528_2"));
            throw new BusinessException("ERROR.00528", e);
        }
    }
    
    /**
     * 判断文件是否重名
     * 
     *  //param fileuuid fileuuid
     * @param folderguid folderguid
     * @param filename filename
     * @throws BusinessException Business Exception
     */
    public void validFileName(String folderguid, String filename)
        throws BusinessException
    {
        try
        {
            String sql =
                "select c.fName from ci_file c "
                    + "where c.folderguid=? and c.fName=? and c.avaible=?";// 根据文件名以及父目录检测文件是否重复
            String name =
                (String) this.getJdbcTemplate().queryForObject(
                    sql,
                    new Object[] {folderguid, filename,
                        SystemParameter.FILE_FOLDER_AVIABLES}, String.class);
            if (StringUtils.isNotBlank(name))
            {
                throw new BusinessException("ERROR.00511");
            }
        }
        catch (EmptyResultDataAccessException e)
        {
            logger.info(MSG.getString("msg.file.nonexistent"));
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
            logger.info(MSG.getString("ERROR.00528"));
            throw new BusinessException("ERROR.00528", e);
        }
    }
    
    /**
     * 根据IP查询列表
     * 
     * @param ip ip地址
     * @return return value
     * @throws BusinessException Business Exception
     */
    public List<WSupportNode> getWSupportByIp(String ip)
        throws BusinessException
    {
        try
        {
            logger.debug("IP" + ip);
            String hql =
                "from WSupportNode c where c.localIp=? order by c.hangsCarriesPath desc";// 降序排列
            return getWSupportNodeEntityDao().find(hql, new Object[] {ip});
        }
        catch (DataAccessException e)
        {
            logger.info(MSG.getString("ERROR.00050"));
            throw new DataNotFoundException("ERROR.00050", e);
        }
        catch (Exception e)
        {
            logger.info(MSG.getString("ERROR.00222"));
            throw new BusinessException("ERROR.00222", e);
        }
    }
    
}
