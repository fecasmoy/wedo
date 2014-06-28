package com.wedo.businessserver.css3.ws.service;

import java.util.HashMap;
import java.util.ResourceBundle;

import javax.jws.WebService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wedo.businessserver.common.util.BaseStaticContextLoader;
import com.wedo.businessserver.common.util.LanguageUtil;
import com.wedo.businessserver.css3.service.WAppService;
import com.wedo.businessserver.css3.ws.model.App;
import com.wedo.businessserver.css3.ws.model.Result;

/**
 * 应用申请对外webservice暴露
 * 
 * @author c90003207
 * 
 */
@WebService
public class AppService
{
    
    /**
     * 记录日志的时候用到的日志工具类
     */
    private static final Log logger = LogFactory.getLog(AppService.class);
    
    /**
     * 本地化处理
     */
    private ResourceBundle msg = LanguageUtil.getMessage();
    
    private WAppService wAppService =
        (WAppService) BaseStaticContextLoader.getApplicationContext().getBean(
            "wAppService");
    
    /**
     * get value
     */
    public void appSetProperties()
    {
        
    }
    
    /**
     * 创建应用
     * 
     * @param app app
     * @return return {@link App}
     */
    public App createApp(App app)
    {
        try
        {
            logger.info(msg.getString("msg.app.create"));
            wAppService.createApp(app);
            app.setMessage(Constants.SUCCESS);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            app.setMessage(e.getMessage());
        }
        return app;
    }
    
    /**
     * 更新应用
     * 
     * @param app app
     * @return return {@link Result}
     */
    public Result updateApp(App app)
    {
        Result result = new Result();
        try
        {
            logger.info(msg.getString("msg.app.update"));
            wAppService.updateApp(app);
            result.setResult(true);
            result.setMessage(Constants.SUCCESS);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            result.setResult(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }
    
    /**
     * 获取总空间，已用空间,可用空间
     * 
     * @param appguid 应用guid
     * @return 总空间，已用空间,可用空间
     */
    public HashMap<String, String> getAppSpace(String appguid)
    {
        HashMap<String, String> map = new HashMap<String, String>();
        try
        {
            logger.info(msg.getString("msg.app.space"));
            map = wAppService.getAppSpace(appguid);
            map.put("message", Constants.SUCCESS);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            map.put("message", e.getMessage());
        }
        return map;
    }
    
    /**
     * 为应用创建仓库
     * 
     * @param appguid 应用guid
     * @param name 应用名称
     * @return 仓库uuid
     */
    public String createRepository(String appguid, String name)
    {
        try
        {
            logger.info(msg.getString("msg.rep.create"));
            return wAppService.createRepository(appguid, name);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            return e.getMessage();
        }
    }
    
}
