package com.wedo.businessserver.css3.http;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wedo.businessserver.common.util.BaseStaticContextLoader;
import com.wedo.businessserver.css3.service.WFileService;

/**
 * 杀毒回调的Servlet
 * 
 * @author c90003207
 * 
 */
public class SecurityServlet
    extends HttpServlet
{
    
    /**
     * 进行序列化的时候用到的序列码
     */
    private static final long serialVersionUID = -7444828059595998456L;
    
    /**
     * 记录日志的时候用到的日志工具类
     */
    private static final Log logger = LogFactory.getLog(DownLoadServlet.class);
    
    /**
     * STRING LENGTH 36
     */
    private static final int STRING_LENGTH_36 = 36;
    
    /**
     * STRING LENGTH 37
     */
    private static final int STRING_LENGTH_37 = 37;
    
    /**
     * WFileService
     */
    private WFileService wFileService;
    
    /**
     * getter
     * 
     * @return WFileService
     */
    public WFileService getFileService()
    {
        wFileService =
            (WFileService) BaseStaticContextLoader.getApplicationContext()
                .getBean("wFileService");
        return wFileService;
    }
    
    /**
     * 异步杀毒返回有毒对象的处理
     * 
     * @param req req
     * @param resp req
     * @throws ServletException exception
     * @throws IOException exception
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
        try
        {
            String filePath = req.getParameter("filePath");
            String fileName =
                filePath.substring(filePath.lastIndexOf("/") + 1, filePath
                    .length());
            String fileuuid = fileName.substring(0, STRING_LENGTH_36);
            String version =
                fileName.substring(STRING_LENGTH_37, fileName.length());
            logger.debug("Security fileuuid:" + fileuuid);
            logger.debug("Security version:" + version);
            getFileService().rollback(fileuuid, version);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
        }
        return;
    }
    
    /**
     * 异步杀毒返回有毒对象的处理
     * 
     * @param req req
     * @param resp req
     * @throws ServletException exception
     * @throws IOException exception
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
        // TODO Auto-generated method stub
        doGet(req, resp);
    }
    
}
