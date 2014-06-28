package com.wedo.businessserver.attemper.job;

import java.io.File;
import java.io.Serializable;
import java.util.Calendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wedo.businessserver.common.util.BaseStaticContextLoader;
import com.wedo.businessserver.common.util.DateUtils;
import com.wedo.businessserver.cache.SupportNode;
import com.wedo.businessserver.common.exception.BusinessException;
import com.wedo.businessserver.css3.service.WSupportNodeService;

/**
 * 计划任务删除临时文件
 * 
 * @author c90003207
 * 
 */
public class TempFileDoJobImpl
    implements Serializable
{

    // 进行序列化的时候用到的序列码
    private static final long serialVersionUID = -4543556214645597766L;

    // 记录日志的时候用到的日志工具类
    private static final Log logger = LogFactory
        .getLog(TempFileDoJobImpl.class);

    // AVAILABLE DAY 3
    private static final int AVAILABLE_DAY_3 = 3;

    // WSupportNodeService
    private WSupportNodeService wSupportNodeService;

    /**
     * WSupport Node Service
     * 
     * @return WSupportNodeService
     */
    public WSupportNodeService getWSupportNodeService()
    {
        wSupportNodeService = (WSupportNodeService) BaseStaticContextLoader
            .getApplicationContext().getBean("wSupportNodeService");
        // 获取WSupportNodeService的实例
        return wSupportNodeService;
    }

    /**
     * 扫描临时目录并删除过期临时文件
     * 
     * @throws BusinessException Business Exception
     */
    public void scanTempFiles()
        throws BusinessException
    {
        File folder = null;
        File files[] = null;
        try
        {
            String root = SupportNode
                .getInstance().getHangsCarriesPath();
            folder = new File(root + "/temp");
            // 判断是文件还是目录
            if (folder
                .exists() && folder
                .isDirectory())
            {
                files = folder
                    .listFiles();
                // 删除文件
                deleteFile(files);
            }
        }
        catch (Exception e)
        {
            logger
                .error("Filed in Clean Temp file!", e);
            // 删除临时文件出错
            throw new BusinessException("ERROR.00432", e);
        }
    }

    /**
     * 根据时间策略删除文件
     * 
     * @param files files
     * @throws Exception Exception
     */
    private void deleteFile(File files[])
        throws Exception
    {
        int flag = 1;
        File tempFile = null;
        Calendar nowCalendar = Calendar
            .getInstance();
        for (int i = 0; i < files.length; i++)
        {
            if (!files[i]
                .isDirectory())
            {
                tempFile = files[i];
                // 获取当前时间
                Calendar calendar = Calendar
                    .getInstance();
                // 设置文件时间
                calendar
                    .setTimeInMillis(tempFile
                        .lastModified());
                // 暂时是3天有效时间
                calendar = DateUtils
                    .afterDays(calendar, AVAILABLE_DAY_3);
                // 比较文件时间是否过期,过去后,需要删除
                flag = calendar
                    .compareTo(nowCalendar);
                if (flag == -1)
                {
                    @SuppressWarnings("unused")
                    Boolean delflag = tempFile
                        .delete();
                }
            }
        }
    }
}
