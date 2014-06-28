package com.wedo.businessserver.attemper.threadpool;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 杀毒任务实现
 * 
 * @author c90003207
 * 
 */
public class SecurityTask
    implements Task
{
    
    // 记录日志的时候用到的日志工具类
    private static final Log logger = LogFactory.getLog(SecurityTask.class);
    
    // time out
    private static final int TIME_OUT_100 = 100;
    
    // 杀毒文件的绝对路径
    private String filePath;
    
    /**
     * 支持post中文，转utf-8发送
     * 
     * @author c90003207
     * 
     */
    public static class UTF8PostMethod
        extends PostMethod
    {
        /**
         * constructor
         * 
         * @param url url
         */
        public UTF8PostMethod(String url)
        {
            super(url);
        }
        
        /**
         * charset
         * 
         * @return {@link String}
         */
        @Override
        public String getRequestCharSet()
        {
            // return super.getRequestCharSet();
            return "UTF-8";
        }
    }
    
    /**
     * constructor
     * 
     * @param filePath filepath
     */
    public SecurityTask(String filePath)
    {
        this.filePath = filePath;
    }
    
    /**
     * 执行任务
     */
    public void execute()
    {
        try
        {
            HttpClient httpClient = new HttpClient();
            HttpClientParams params = new HttpClientParams();
            // 是否保持连接
            params
                .setParameter(HttpClientParams.USE_EXPECT_CONTINUE,
                    Boolean.FALSE);
            // 设置http超时时间
            params
                .setParameter(HttpClientParams.CONNECTION_MANAGER_TIMEOUT, Long
                    .valueOf(TIME_OUT_100));
            // 设置连接状态信息
            httpClient
                .setParams(params);
            // 设置发送地址
            PostMethod post =
                new UTF8PostMethod("http://" + Constants.SECURITYIP
                    + "/securityTask");
            // 通讯参数数组
            NameValuePair[] data = new NameValuePair[1];
            // 设置filePath参数
            data[0] = new NameValuePair("filePath", filePath);
            // 发送数据
            post
                .setRequestBody(data);
            // 关闭连接
            post
                .setRequestHeader("Connection", "close");
            // 执行post连接
            httpClient
                .executeMethod(post);
            // 返回的状态
            int status = post
                .getStatusCode();
            logger
                .info("Security Status:" + status);
        }
        catch (Exception e)
        {
            logger.info("Security Error", e);
        }
    }
    
    /**
     * file path
     * 
     * @return {@link String}
     */
    public String getFilePath()
    {
        return filePath;
    }
    
    /**
     * file path
     * 
     * @param filePath file Path
     */
    public void setFilePath(String filePath)
    {
        this.filePath = filePath;
    }
    
}
