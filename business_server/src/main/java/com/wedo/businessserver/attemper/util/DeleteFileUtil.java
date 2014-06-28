package com.wedo.businessserver.attemper.util;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 删除目录以及文件
 * 
 * @author c90003207
 * 
 */
public class DeleteFileUtil
{

    // 记录日志的时候用到的日志工具类
    protected static final Log logger = LogFactory
        .getLog(DeleteFileUtil.class);

    /**
     * 删除文件，可以是单个文件或文件夹
     * 
     * @param fileName 待删除的文件名
     * @return 文件删除成功返回true,否则返回false
     */
    public static boolean delete(String fileName)
    {
        File file = new File(fileName);
        if (!file
            .exists())
        {
            logger
                .info("Delete file ERROR : " + fileName + "not exist!");
            return false;
        }
        else
        {
            if (file
                .isFile())
            {
                return deleteFile(fileName);
            }
            else
            {
                return deleteDirectory(fileName);
            }
        }
    }

    /**
     * 删除单个文件
     * 
     * @param fileName 被删除文件的文件名
     * @return boolean 单个文件删除成功返回true,否则返回false
     */
    public static boolean deleteFile(String fileName)
    {
        File file = new File(fileName);
        if (file
            .isFile() && file
            .exists())
        {
            @SuppressWarnings("unused")
            Boolean flag = file
                .delete();
            logger
                .info("Delete File" + fileName + "Success");
            return true;
        }
        else
        {
            logger
                .info("Delete File" + fileName + "Error");
            return false;
        }
    }

    /**
     * 删除目录（文件夹）以及目录下的文件
     * 
     * @param dir 被删除目录的文件路径
     * @return boolean 目录删除成功返回true,否则返回false
     */
    public static boolean deleteDirectory(String dir)
    {
        // 如果dir不以文件分隔符结尾，自动添加文件分隔符
        if (!dir
            .endsWith(File.separator))
        {
            dir = dir + File.separator;
        }
        File dirFile = new File(dir);
        // 如果dir对应的文件不存在，或者不是一个目录，则退出
        if (!dirFile
            .exists() || !dirFile
            .isDirectory())
        {
            logger
                .info("Delete Folder Error" + dir + "Folder Not Exist");
            return false;
        }
        // 删除文件夹下的所有文件(包括子目录)
        boolean flag = true;
        File[] files = dirFile
            .listFiles();
        for (int i = 0; i < files.length; i++)
        {
            // 删除子文件
            if (files[i]
                .isFile())
            {
                flag = deleteFile(files[i]
                    .getAbsolutePath());
                if (!flag)
                {
                    break;
                }
            }
            // 删除子目录
            else
            {
                flag = deleteDirectory(files[i]
                    .getAbsolutePath());
                if (!flag)
                {
                    break;
                }
            }
        }
        if (!flag)
        {
            logger
                .info("Delete Folder Error");
            return false;
        }
        // 删除当前目录
        if (dirFile
            .delete())
        {
            logger
                .info("Delete Folder" + dir + "Success");
            return true;
        }
        else
        {
            logger
                .info("Delete Folder" + dir + "Error");
            return false;
        }
    }
}
