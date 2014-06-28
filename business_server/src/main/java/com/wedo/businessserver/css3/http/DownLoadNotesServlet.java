package com.wedo.businessserver.css3.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
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
import com.wedo.businessserver.css3.domain.WSupportNode;
import com.wedo.businessserver.css3.service.WFileService;
import com.wedo.businessserver.css3.service.WSupportNodeService;
import com.wedo.businessserver.storage.jcr.domain.MetaData;
import com.wedo.businessserver.storage.jcr.service.JCRService;

/**
 * the download notes servlet
 * 
 * @author c90003207
 */
public class DownLoadNotesServlet
    extends HttpServlet
{
    
    /**
     * 进行序列化的时候用到的序列码
     */
    private static final long serialVersionUID = -6583574540645326866L;
    
    /**
     * 记录日志的时候用到的日志工具类
     */
    private static final Log logger = LogFactory.getLog(DownLoadServlet.class);
    
    /**
     * SIZE 8196
     */
    private static final int SIZE_8196 = 8196;
    
    /**
     * 实现国际化时用到的工具类
     */
    private ResourceBundle msg = LanguageUtil.getMessage();
    
    /**
     * 元数据操作的Service
     */
    private JCRService service;
    
    /**
     * 文件操作的Service
     */
    private WFileService wFileService;
    
    /**
     * 云存储服务节点操作的Service
     */
    private WSupportNodeService wSupportNodeService;
    
    /**
     * 完成后进行销毁
     */
    @Override
    public void destroy()
    {
        super.destroy();
    }
    
    /**
     * 响应HTTP的POST请求的方法实现
     * 实现下载文件
     * 
     * @param req req
     * @param response resp
     * @throws ServletException Exception
     * @throws IOException Exception
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response)
        throws ServletException, IOException
    {
        // 开始文件在线下载
        logger.info(msg.getString("msg.download.start"));
        // String referer = req.getHeader("referer");
        File realFile = null;
        InputStream inputStream = null;
        OutputStream out = null;
        try
        {
            // if (referer != null) {
            // 这里可作校验域名或者IP地址，目前暂不实现
            String fileuuid = req.getParameter("fileuuid");
            if (StringUtils.isBlank(fileuuid))
            {
                return;
            }
            // 获取文件的元数据
            MetaData metaData = getService().getMetaData(fileuuid, null);
            String fileName = metaData.getName();
            String filePath = metaData.getFilePath();
            // 设置编码信息
            response.setCharacterEncoding("UTF-8");
            // 设置格式信息
            response.setContentType("application/octet-stream;charset=utf-8");
            String attchName = java.net.URLEncoder.encode(fileName, "UTF-8");
            String agent = req.getHeader("USER-AGENT");
            // 在IE场景下进行下载
            if (null != agent && -1 != agent.indexOf("MSIE"))
            {
                // 文件名转码
                response.addHeader("Content-Disposition",
                    "attachment; filename=\""
                        + StringUtils.replace(attchName, "+", "%20") + "\"");
            }
            // 在FireFox场景下进行下载
            else if (null != agent && -1 != agent.indexOf("Mozilla"))
            {
                // 文件名转码
                attchName =
                    "=?UTF-8?B?"
                        + (new String(org.apache.commons.codec.binary.Base64
                            .encodeBase64(fileName.getBytes("UTF-8")))) + "?=";
                response.addHeader("Content-Disposition",
                    "attachment; filename=\"" + attchName + "\"");
            }
            
            // 获取可用的云存储服务节点
            List<WSupportNode> list =
                getWSupportNodeService().getWSupportByIp(
                    com.wedo.businessserver.storage.jcr.Constants.LOCALIP);
            // 获取文件真实路径，有多个挂载的时候
            for (int i = 0; i < list.size(); i++)
            {
                String root = list.get(i).getMountPath();
                realFile = new File(root + filePath);
                if (realFile.exists() && realFile.isFile())
                {
                    break;
                }
            }
            if (realFile == null)
            {
                // 文件操作，源文件为空
                logger.info(msg.getString("ERROR.00520"));
                return;
            }
            inputStream = new FileInputStream(realFile); // 获取文件
            int n = -1;
            byte[] buffer = new byte[SIZE_8196];
            out = response.getOutputStream();
            while ((n = inputStream.read(buffer, 0, SIZE_8196)) > -1)
            {
                out.write(buffer, 0, n);
            }
            inputStream.close();
            out.close();
            // } else {
            // errorMessage(response);
            // }
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
        }
        finally
        {
            if (inputStream != null)
            {
                inputStream.close();
            }
            if (inputStream != null)
            {
                out.close();
            }
        }
        
    }
    
    /**
     * 响应HTTP的GET请求的方法实现 实现下载文件
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
        doGet(req, resp);
    }
    
    /**
     * 获取jcr操作的Service
     * 
     * @return JCRService
     */
    public JCRService getService()
    {
        if (service == null)
        {
            // 从Spring容器中获取
            service =
                (JCRService) BaseStaticContextLoader.getApplicationContext()
                    .getBean("jCRService");
        }
        return service;
    }
    
    /**
     * jcr操作的Service的SET方法
     * 
     * @param service service
     */
    public void setService(JCRService service)
    {
        this.service = service;
    }
    
    /**
     * 初始化方法
     * 
     * @throws ServletException Servlet Exception
     */
    @Override
    public void init()
        throws ServletException
    {
        super.init();
    }
    
    /**
     * 获取文件操作的Service
     * 
     * @return WFileService
     */
    public WFileService getWFileService()
    {
        wFileService =
            (WFileService) BaseStaticContextLoader.getApplicationContext()
                .getBean("wFileService");
        return wFileService;
    }
    
    /**
     * 设置文件操作的Service
     * 
     * @param fileService fileService
     */
    public void setWFileService(WFileService fileService)
    {
        wFileService = fileService;
    }
    
    /**
     * 获取云存储服务节点的Service
     * 
     * @return WSupportNodeService
     */
    public WSupportNodeService getWSupportNodeService()
    {
        wSupportNodeService =
            (WSupportNodeService) BaseStaticContextLoader
                .getApplicationContext().getBean("wSupportNodeService");
        return wSupportNodeService;
    }
    
    /**
     * 设置云存储服务节点的Service
     * 
     * @param supportNodeService supportNodeService
     */
    public void setWSupportNodeService(WSupportNodeService supportNodeService)
    {
        wSupportNodeService = supportNodeService;
    }
}
