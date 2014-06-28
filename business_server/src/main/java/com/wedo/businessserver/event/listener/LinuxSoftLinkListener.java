package com.wedo.businessserver.event.listener;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.Random;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

import com.wedo.businessserver.cache.SupportNode;
import com.wedo.businessserver.common.exception.BusinessException;
import com.wedo.businessserver.common.exception.GeneralException;
import com.wedo.businessserver.common.util.BaseStaticContextLoader;
import com.wedo.businessserver.common.util.LanguageUtil;
import com.wedo.businessserver.css3.domain.WSupportNode;
import com.wedo.businessserver.css3.service.WSupportNodeService;
import com.wedo.businessserver.event.MessageEvent;
import com.wedo.businessserver.storage.jcr.Constants;

/**
 * linux系统软链接事件监听处理方法
 * 
 * @author l00100468
 */
public class LinuxSoftLinkListener
    implements ApplicationListener
{
    
    /**
     * 记录日志的时候用到的日志工具类
     */
    private static final Log logger =
        LogFactory.getLog(LinuxSoftLinkListener.class);
    
    /**
     * model
     */
    private static final int MODEL_100 = 100;
    
    /**
     * message
     */
    private ResourceBundle msg = LanguageUtil.getMessage();
    
    /**
     * getter
     * 
     * @return WSupportNodeService
     */
    public WSupportNodeService getWSupportNodeService()
    {
        WSupportNodeService wSupportNodeService =
            (WSupportNodeService) BaseStaticContextLoader
                .getApplicationContext().getBean("wSupportNodeService");
        return wSupportNodeService;
    }
    
    /**
     * event
     * 
     * @param event event
     */
    public void onApplicationEvent(ApplicationEvent event)
    {
        if (event instanceof MessageEvent)
        {
            MessageEvent msEvent = (MessageEvent) event;
            softLink(msEvent);
        }
    }
    
    /**
     * soft link make
     * 
     * @param msEvent msEvent
     */
    private void softLink(MessageEvent msEvent)
    {
        if (msEvent.getMessage() instanceof WSupportNode)
        {
            Properties props = System.getProperties();
            String osName = props.getProperty("os.name");
            if (osName.indexOf("Windows") != -1)
            {
                SupportNode
                    .updateInstance();
            }
            else
            {
                linuxSoftLink(msEvent, osName);
            }
        }
    }
    
    /**
     * linux软链接处理
     * 
     * @param msEvent
     * @param osName
     */
    private void linuxSoftLink(MessageEvent msEvent, String osName)
    {
        WSupportNode supportNode = (WSupportNode) msEvent.getMessage();
        String targetCsnGuid = supportNode.getCsnguid();
        String writePath = supportNode.getHangsCarriesPath().trim();
        String subwritePath =
            writePath.substring(writePath.lastIndexOf("/") + 1, writePath
                .length());// 取写入挂载路径最后一节
        String folderPath = Constants.RESCROOT + "/" + subwritePath;
        File file = new File(folderPath);
        String cmd = null;
        String lnName = null;
        if (file.exists())
        {
            String linkpath = folderPath;
            while (file.exists())
            {
                logger.info(msg.getString("msg.csp.symname_exist"));// 重复，取原挂载点后加上两位随机数字做新符号
                Random ram = new Random();
                int tmp = ram.nextInt(MODEL_100);
                linkpath = folderPath + "_" + String.valueOf(tmp);
                file = new File(linkpath);
            }
            cmd = "ln -s " + writePath + "  " + linkpath;
            lnName = cmd.substring(cmd.lastIndexOf("/") + 1, cmd.length());
        }
        else
        {
            logger.info(msg.getString("msg.csp.symname"));// 无重复
            cmd = "ln -s " + writePath + "  " + Constants.RESCROOT;
            lnName = subwritePath;
        }
        try
        {
            logger.info("os  = " + osName + "  writepath = " + writePath);
            logger.info(msg.getString("msg.csp.cmd") + " : " + cmd);
            if (supportNode.getCsnguid().equals(targetCsnGuid)
                && StringUtils.isNotBlank(lnName))
            {
                logger.info(msg.getString("msg.csp.startupdatacsn"));
                useOsCmd(cmd);
                supportNode = SupportNode.getInstance();
                supportNode.setSymName(lnName.trim());
                getWSupportNodeService().updateCsp(supportNode);
                SupportNode.updateInstance();// 更新单例
                logger.info(msg.getString("msg.csp.updatacsnsuccess"));
            }
            
        }
        catch (BusinessException e)
        {
            logger.error(e.getMessage(), e);
            throw new GeneralException(e.getMessage());
        }
        catch (Exception e)
        {
            logger.error(msg.getString("ERROR.00120"), e);// 挂载节点context出错
            throw new GeneralException("ERROR.00120");
        }
    }
    
    /**
     * 执行linux命令行
     * 
     * @param cmd
     * @throws Exception
     */
    private void useOsCmd(String cmd)
        throws Exception
    {
        BufferedReader reader = null;
        try
        {
            // 执行命令
            Runtime runtime = Runtime.getRuntime();
            Process process = runtime.exec(cmd);
            logger.info("命令执行完成:" + cmd);
            // 读取命令输出
            reader =
                new BufferedReader(new InputStreamReader(process
                    .getInputStream()));
            String str = "";
            while (reader.read() != -1)
            {
                str = reader.readLine();
                logger.info(str);
            }
        }
        catch (Exception e)
        {
            logger.error("error softlink", e);
        }
        finally
        {
            if (reader != null)
            {
                reader.close();
            }
        }
        
    }
}
