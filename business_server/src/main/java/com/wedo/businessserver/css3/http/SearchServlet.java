package com.wedo.businessserver.css3.http;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wedo.businessserver.common.util.BaseStaticContextLoader;
import com.wedo.businessserver.common.util.LanguageUtil;
import com.wedo.businessserver.css3.service.WFolderService;
import com.wedo.businessserver.css3.service.util.DataServiceUtil;
import com.wedo.businessserver.css3.ws.model.FilePageInfo;
import com.wedo.businessserver.css3.ws.model.WappProperties;
import com.wedo.businessserver.css3.ws.service.Constants;
import com.wedo.businessserver.storage.jcr.util.JOXUtils;

/**
 * 搜索引擎对外暴露的Servlet，提供数据搜索服务
 * 
 * @author c90003207
 * 
 */
public class SearchServlet
    extends HttpServlet
{
    
    /**
     * 进行序列化的时候用到的序列码
     */
    private static final long serialVersionUID = 6736013467541831124L;
    
    /**
     * 记录日志的时候用到的日志工具类
     */
    private static final Log logger = LogFactory.getLog(SearchServlet.class);
    
    private ResourceBundle msg = LanguageUtil.getMessage();
    
    private WFolderService wFolderService =
        (WFolderService) BaseStaticContextLoader.getApplicationContext()
            .getBean("wFolderService");
    
    /**
     * get method
     * 
     * @param req req
     * @param resp resp
     * @throws ServletException Exception
     * @throws IOException Exception
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=utf-8");
        PrintWriter wr = null;
        try
        {
            wr = resp.getWriter();
            String appId = req.getParameter("appId");
            String keyWord = req.getParameter("keyWord");
            String folderuuid = req.getParameter("folderuuid");
            String currentPage = req.getParameter("currentPage");
            String countOfCurrentPage = req.getParameter("countOfCurrentPage");
            String fileType = req.getParameter("fileType");
            String starttime = req.getParameter("starttime");
            String endtime = req.getParameter("endtime");
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("filetype", fileType);
            map.put("starttime", starttime);
            map.put("endtime", endtime);
            if (StringUtils.isNotBlank(appId)
                && StringUtils.isNotBlank(folderuuid)
                && StringUtils.isNotBlank(currentPage)
                && StringUtils.isNotBlank(countOfCurrentPage))
            {
                
                // 检查该应用增值项权限
                WappProperties prop = DataServiceUtil.getAuthorization(appId);
                if (!prop.getFunction().getSearchEngine().getAvailable())
                {
                    // 该应用无搜索权限
                    logger.error(msg.getString("ERROR.00574"));
                    // resp.setStatus(403);
                    return;
                }
                // TODO:检查有该数据服务权限后，还需加入对是否摘要进行处理
                FilePageInfo filePageInfo = new FilePageInfo();
                filePageInfo.setCountOfCurrentPage(Integer
                    .valueOf(countOfCurrentPage));
                filePageInfo.setCurrentPage(Integer.valueOf(currentPage));
                searchFile(appId, keyWord, folderuuid, filePageInfo, map);
                String xmlString = JOXUtils.toXML(filePageInfo, "GB2312");
                logger.debug(msg.getString("msg.search.xml") + xmlString);
                wr.write(xmlString);
            }
            else
            {
                return;
            }
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            return;
        }
        finally
        {
            if (wr != null)
            {
                wr.close();
            }
        }
    }
    
    /**
     * 调用底层搜索具体的文件内容
     * 
     * @param appId appId
     * @param keyWord keyWord
     * @param folderuuid folderuuid
     * @param filePageInfo filePageInfo
     * @param map map
     * @throws Exception exception
     */
    private void searchFile(String appId, String keyWord, String folderuuid,
        FilePageInfo filePageInfo, HashMap<String, String> map)
        throws Exception
    {
        Long start = System.currentTimeMillis();
        wFolderService.search(appId, keyWord, folderuuid, filePageInfo, map);
        filePageInfo.setMessage(Constants.SUCCESS);
        Long end = System.currentTimeMillis();
        logger.info(msg.getString("msg.search.time") + (end - start) + "ms");
    }
    
    /**
     * post method
     * 
     * @param req req
     * @param resp resp
     * @throws ServletException Exception
     * @throws IOException Exception
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
        // TODO Auto-generated method stub
        doGet(req, resp);
    }
    
}
