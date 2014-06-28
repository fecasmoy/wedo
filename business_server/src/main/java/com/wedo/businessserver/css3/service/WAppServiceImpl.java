package com.wedo.businessserver.css3.service;

import java.math.BigDecimal;
import java.util.HashMap;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.wedo.businessserver.common.exception.BusinessException;
import com.wedo.businessserver.common.exception.DataNotFoundException;
import com.wedo.businessserver.common.exception.ParameterException;
import com.wedo.businessserver.common.util.RandomGUID;
import com.wedo.businessserver.css3.domain.WRepository;
import com.wedo.businessserver.css3.domain.Wapp;
import com.wedo.businessserver.css3.service.util.DataServiceUtil;
import com.wedo.businessserver.css3.ws.model.App;

/**
 * 配置应用相关实现类
 * 
 * @author c90003207
 * 
 */
@Service("wAppService")
public class WAppServiceImpl
    extends WOnlineAbstractServiceImpl
    implements WAppService
{
    /**
     * SPACE SIZE 1073741824
     */
    private static final int SPACE_SIZE_1073741824 = 1073741824;
    
    /**
     * 创建应用
     * 
     * @param app 应用对像
     * @return app {@link App}
     * @throws BusinessException 业务异常
     */
    public App createApp(App app)
        throws BusinessException
    {
        try
        {
            Wapp wapp = new Wapp();
            BeanUtils.copyProperties(wapp, app);
            wapp.setGuid(app.getGuid());
            wapp.setUsespace(new BigDecimal(0));// 设置已用空间
            wapp.setToalspace(app.getToalspace().multiply(
                new BigDecimal(SPACE_SIZE_1073741824)));// 设置总空间
            getWappEntityDao().save(wapp);// 更新数据库
            app.setAccesskeyUrl(wapp.getKeyUrl());
            DataServiceUtil.defaultProperties(app.getGuid());
            return app;
        }
        catch (DataAccessException e)
        {
            logger.info(MSG.getString("ERROR.00050"));
            throw new DataNotFoundException("ERROR.00050", e);
        }
        catch (Exception e)
        {
            logger.info(MSG.getString("ERROR.00320"));
            throw new BusinessException("ERROR.00320", e);
        }
    }
    
    /**
     * 创建仓库
     * 
     * @param appguid
     *            应用guid
     * @param name
     *            应用名称
     * @return String
     * @throws BusinessException 业务异常
     */
    public String createRepository(String appguid, String name)
        throws BusinessException
    {
        
        try
        {
            logger.info("name =   " + name);
            logger.info("appguid= " + appguid);
            if (StringUtils.isBlank(name))
            {
                throw new ParameterException("ERROR.00301");
            }
            getApp(appguid);
            WRepository wRepository = new WRepository();
            RandomGUID randomGUID = new RandomGUID();
            wRepository
                .setGuid(randomGUID
                    .getValueAfterMD5());
            wRepository
                .setAppGuid(appguid);
            wRepository
                .setName(name);
            String repuuid = getJCRService()
                .createAppRep(appguid);// 底层jcr创建仓库
            wRepository
                .setRepuuid(repuuid);
            getRepositoryEntityDao().save(wRepository);
            // DataServiceUtil.defaultProperties(appguid);
            return repuuid;
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
            logger.info(MSG.getString("ERROR.00321"));
            throw new BusinessException("ERROR.00321", e);
        }
    }
    
    /**
     * 得到应用使用空间
     * 
     * @param appguid 应用guid
     * @return hash map {@link HashMap}
     * @throws BusinessException 业务异常
     */
    public HashMap<String, String> getAppSpace(String appguid)
        throws BusinessException
    {
        try
        {
            Wapp wapp = getApp(appguid);
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("toalspace", wapp.getToalspace().toString());// 总空间
            map.put("usespace", wapp.getUsespace().toString());// 已用空间
            map.put("hasspace", wapp.getToalspace()// 剩余空间
                .subtract(wapp.getUsespace()).toString());
            return map;
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
            logger.info(MSG.getString("ERROR.00311"));
            throw new BusinessException("ERROR.00311", e);
        }
    }
    
    /**
     * 更新应用
     * 
     * @param app
     *            应用对象
     * @throws BusinessException 业务异常
     */
    public void updateApp(App app)
        throws BusinessException
    {
        try
        {
            Wapp wapp = getApp(app.getGuid());
            BeanUtils.copyProperties(wapp, app);
            wapp.setUsespace(new BigDecimal(0));
            wapp.setToalspace(app.getToalspace().multiply(
                new BigDecimal(SPACE_SIZE_1073741824)));// 设置总空间大小
            getWappEntityDao().update(wapp);// 更新数据库
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
            logger.info(MSG.getString("ERROR.00322"));
            throw new BusinessException("ERROR.00322", e);
        }
    }
}
