package com.wedo.businessserver.css3.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import com.wedo.businessserver.css3.domain.WFile;
import com.wedo.businessserver.css3.domain.WFileHistory;
import com.wedo.businessserver.css3.domain.WSupportNode;
import com.wedo.businessserver.css3.service.SystemParameter;
import com.wedo.businessserver.css3.service.WFileService;
import com.wedo.businessserver.css3.service.WSupportNodeService;
import com.wedo.businessserver.storage.jcr.service.JCRService;

/**
 * 下载文件servlet
 * 
 * @author c90003207
 * 
 */
public class DownLoadServlet
    extends HttpServlet
{

    /**
     * 进行序列化的时候用到的序列码
     */
    private static final long serialVersionUID = -6583574540645326866L;

    /**
     * 记录日志的时候用到的日志工具类
     */
    private static final Log logger = LogFactory
        .getLog(DownLoadServlet.class);

    /**
     * SIZE 8196
     */
    private static final int SIZE_8196 = 8196;

    /**
     * 国际化用到的工具类
     */
    private ResourceBundle msg = LanguageUtil
        .getMessage();

    /**
     * JCR操作的Service
     */
    private JCRService service;

    /**
     * 文件操作的Service
     */
    private WFileService wFileService;

    /**
     * 云存储服务节点的Service
     */
    private WSupportNodeService wSupportNodeService;

    /**
     * 销毁方法
     */
    @Override
    public void destroy()
    {
        super
            .destroy();
    }

    private void setFileName(String fileName, HttpServletRequest req,
        HttpServletResponse response)
        throws Exception
    {
        // 对文件名进行编码
        String downName = java.net.URLEncoder
            .encode(fileName, "UTF-8");
        String agent = req
            .getHeader("USER-AGENT");
        // 处理IE的下载场景
        try
        {
            if (StringUtils
                .isNotBlank(agent) && agent
                .indexOf("MSIE") != -1)
            {
                // 文件名转码
                response
                    .addHeader("Content-Disposition", "attachment; filename=\""
                        + StringUtils
                            .replace(downName, "+", "%20") + "\"");
            }
            // 处理FireFox的下载场景
            else if (StringUtils
                .isNotBlank(agent) && agent
                .indexOf("Mozilla") != -1)
            {
                downName =
                    "=?UTF-8?B?"
                        + (new String(org.apache.commons.codec.binary.Base64
                            .encodeBase64(fileName
                                .getBytes("UTF-8")))) + "?=";
                // 文件名转码
                response
                    .addHeader("Content-Disposition", "attachment; filename=\""
                        + downName + "\"");
            }
        }
        catch (StringIndexOutOfBoundsException e)
        {
            logger
                .error(e
                    .getMessage(), e);
            throw new Exception(e
                .getMessage());
        }
        catch (Exception e)
        {
            logger
                .error(e
                    .getMessage(), e);
            throw new Exception(e
                .getMessage());
        }
    }
    
    /**
     * 响应HTTP的POST请求的方法实现
     * 实现下载文件
     * 
     * @param req req
     * @param response resp
     * @throws ServletException ServletException
     * @throws IOException Exception
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response)
        throws ServletException, IOException
    {
        // 开始文件在线下载
        logger
            .debug(msg
                .getString("msg.download.start"));
        // 获取请求的referer信息
        String referer = req
            .getHeader("referer");
        // 文件路径
        String filePath = null;
        // 文件的实际路径
        File realFile = null;
        // 文件流
        InputStream inputStream = null;
        OutputStream out = null;
        try
        {
            if (referer != null)
            {
                String fileuuid = req
                    .getParameter("fileuuid");// 文件uuid
                String version = req
                    .getParameter("version");// 文件版本
                if (StringUtils
                    .isBlank(fileuuid))
                {
                    return;
                }
                String fileName = null;
                // 设置文件的可用信息
                Map<String, String> map = new HashMap<String, String>();
                map
                    .put("avaible", SystemParameter.FILE_FOLDER_AVIABLES);
                map
                    .put("fileuuid", fileuuid);
                if (StringUtils
                    .isBlank(version))
                {
                    // 获取最新文件路径
                    WFile wfile = this
                        .getWFileService().getWFile(map);
                    fileName = wfile
                        .getFName();
                    filePath = wfile
                        .getFpath();
                }
                else
                {
                    map
                        .put("version", version);
                    // 获取历史文件路径
                    WFileHistory wfileHistory = this
                        .getWFileService().getWFileHistory(map);
                    fileName = wfileHistory
                        .getFName();
                    filePath = wfileHistory
                        .getFpath();
                }
                // 设置请求的编码信息
                response
                    .setCharacterEncoding("UTF-8");
                // 设置请求的格式信息
                response
                    .setContentType("application/octet-stream;charset=utf-8");
                setFileName(fileName, req, response);
                List<WSupportNode> list =
                    getWSupportNodeService()
                        .getWSupportByIp(
                            com.wedo.businessserver.storage.jcr.Constants.LOCALIP);
                // 获取文件真实路径，有多个挂载的时候
                for (int i = 0; i < list
                    .size(); i++)
                {
                    String root = list
                        .get(i).getMountPath();
                    realFile = new File(root + filePath);

                    // 文件已经存在
                    if (realFile
                        .exists() && realFile
                        .isFile())
                    {
                        break;
                    }
                }
                if (realFile == null)
                {
                    logger
                        .info(msg
                            .getString("ERROR.00520")); // 文件操作，源文件为空
                    return;
                }
                inputStream = new FileInputStream(realFile); // 获取文件
                int n = -1;
                byte[] buffer = new byte[SIZE_8196];
                out = response
                    .getOutputStream();

                // 循环读取数据
                while ((n = inputStream
                    .read(buffer, 0, SIZE_8196)) > -1)
                {
                    out
                        .write(buffer, 0, n);
                }
                inputStream
                    .close();
                out
                    .close();
            }
            // 当请求的referer信息为空时，需要发出错误
            else
            {
                errorMessage(response);
                return;
            }
        }
        catch (Exception e)
        {
            // 位置异常发生
            logger
                .error(e
                    .getMessage(), e);
        }
        finally
        {
            if (inputStream != null)
            {
                inputStream
                    .close();
            }
            if (out != null)
            {
                out
                    .close();
            }
        }

    }

    /**
     * 响应HTTP的错误处理
     * 
     * @param response resp
     * @throws IOException Exception
     */
    private void errorMessage(HttpServletResponse response)
        throws IOException
    {
        response
            .setCharacterEncoding("UTF-8");
        response
            .setContentType("application/octet-stream;charset=utf-8");
        PrintWriter out = response
            .getWriter();
        out
            .println("<script type=\"text/javascript\" language=\"javascript\">");
        // 提示不要盗链文件
        out
            .print("alert('请不要盗链文件')");
        out
            .println("</script>");
    }

    /**
     * 响应HTTP的POST请求的方法实现
     * 实现下载文件
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
        // 直接调用GET方法进行处理
        doGet(req, resp);
    }

    /**
     * get Service
     * 
     * @return JCRService
     */
    public JCRService getService()
    {
        if (service == null)
        {
            service = (JCRService) BaseStaticContextLoader
                .getApplicationContext().getBean("jCRService");
        }
        return service;
    }

    /**
     * 设置JCR操作的Service
     * 
     * @param service service
     */
    public void setService(JCRService service)
    {
        this.service = service;
    }

    /**
     * init method
     * 
     * @throws ServletException Servlet Exception
     */
    @Override
    public void init()
        throws ServletException
    {
        super
            .init();
    }

    /**
     * get WFile Service
     * 
     * @return WFileService
     */
    public WFileService getWFileService()
    {
        wFileService = (WFileService) BaseStaticContextLoader
            .getApplicationContext().getBean("wFileService");
        return wFileService;
    }

    /**
     * set WFile Service
     * 
     * @param fileService fileService
     */
    public void setWFileService(WFileService fileService)
    {
        wFileService = fileService;
    }

    /**
     * get WSupport Node Service
     * 
     * @return WSupportNodeService
     */
    public WSupportNodeService getWSupportNodeService()
    {
        wSupportNodeService = (WSupportNodeService) BaseStaticContextLoader
            .getApplicationContext().getBean("wSupportNodeService");
        return wSupportNodeService;
    }

    /**
     * 设置云存储服务节点操作的Service
     * 
     * @param supportNodeService supportNodeService
     */
    public void setWSupportNodeService(WSupportNodeService supportNodeService)
    {
        wSupportNodeService = supportNodeService;
    }

}
