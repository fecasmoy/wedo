package com.wedo.businessserver.storage.jcr.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.wedo.businessserver.common.exception.BusinessException;
import com.wedo.businessserver.common.exception.JCRException;
import com.wedo.businessserver.storage.jcr.Constants;
import com.wedo.businessserver.storage.jcr.domain.MetaData;
import com.wedo.businessserver.storage.jcr.service.JCRService;
import com.wedo.businessserver.storage.jcr.util.JOXUtils;
import com.wedo.businessserver.storage.jcr.util.JuuidUtils;

/**
 * 元数据操作实现类，完成元数据的所有底层操作实现
 * 
 * @author huisk h90003107
 * 
 */
@Service("jCRService")
public class JCRServiceImpl
    implements JCRService
{

    protected static final Log logger = LogFactory
        .getLog(JCRServiceImpl.class);

    /**
     * 版本策略
     * 
     * @param nowVersion now Version
     * @return return value
     */
    public synchronized String getVersion(String nowVersion)
    {
        // 分割版本的后缀
        String s[] = nowVersion
            .split("\\.");
        // 取最后一位
        Integer last = Integer
            .valueOf(s[1]);
        last++;
        // 取得现在的版本
        return s[0] + "." + last;
    }

    /**
     * 创建应用
     * 
     * @param appGuid appGuid
     * @return return value
     * @throws BusinessException Business Exception
     */
    public String createAppRep(String appGuid)
        throws BusinessException
    {
        FileOutputStream fileOutputStream = null;
        try
        {
            // 获取uuid和uuid的磁盘存储目录
            Map<String, String> map = JuuidUtils
                .getUuid();
            String uuid = map
                .get("uuid");
            String uuidPath = map
                .get("uuidPath");
            File folder = new File(Constants.JCRROOT + "/" + uuidPath);
            // 创建uuid相应的目录结构
            if (!folder
                .exists())
            {
                Boolean flag = folder
                    .mkdirs();
                if (!flag)
                {
                    logger
                        .error("Folder create failed! folder is: " + folder
                            .getPath());
                }
            }
            String path =
                Constants.JCRROOT + "/" + uuidPath + "/" + uuid + ".xml";
            File file = new File(path);
            // 如果元数据文件存在在异常处理
            if (file
                .exists())
            {
                throw new JCRException("ERROR.00602");
            }
            fileOutputStream = new FileOutputStream(path);
            MetaData metaData = new MetaData();
            metaData
                .setParentUuid("root");
            metaData
                .setAppGuid(appGuid);
            // 以utf-8形式转成字符串
            String xml = JOXUtils
                .toXML(metaData, "UTF-8");
            // 刷新到磁盘上
            fileOutputStream
                .write(xml
                    .getBytes());
            return uuid;
        }
        catch (Exception e)
        {
            throw new JCRException("ERROR.00601", e);
        }
        finally
        {
            if (fileOutputStream != null)
            {
                try
                {
                    fileOutputStream
                        .close();
                }
                catch (IOException e)
                {
                    // TODO Auto-generated catch block
                    e
                        .printStackTrace();
                }
            }
        }
    }

    /**
     * 创建元数据
     * 
     * @param metaData metaData
     * @return return value
     * @throws BusinessException Business Exception
     */
    public MetaData createMetaData(MetaData metaData)
        throws BusinessException
    {
        FileOutputStream fileOutputStream = null;
        try
        {
            String path = null;
            Map<String, String> map = JuuidUtils
                .getUuid();
            String uuid = map
                .get("uuid");
            String uuidPath = map
                .get("uuidPath");
            File folder = new File(Constants.JCRROOT + "/" + uuidPath);
            if (!folder
                .exists())
            {
                @SuppressWarnings("unused")
                Boolean flag = folder
                    .mkdirs();
            }
            metaData
                .setUuid(uuid);
            // 如果对象已经chenckin了
            if (metaData
                .getCheckin())
            {
                metaData
                    .setVersion("1.0");
                path =
                    Constants.JCRROOT + "/" + uuidPath + "/" + uuid + "-1.0"
                        + ".xml";
            }
            else
            {
                path = Constants.JCRROOT + "/" + uuidPath + "/" + uuid + ".xml";

            }
            File file = new File(path);
            if (file
                .exists())
            {
                throw new JCRException("ERROR.00602");
            }
            fileOutputStream = new FileOutputStream(path);
            String xml = JOXUtils
                .toXML(metaData, "GB2312");
            fileOutputStream
                .write(xml
                    .getBytes());
            fileOutputStream
                .close();
            if (metaData
                .getCheckin())
            {
                MetaData metaDataDesign = new MetaData();
                String metaDataDesignPath =
                    Constants.JCRROOT + "/" + uuidPath + "/" + "design.xml";
                metaDataDesign
                    .setNewVersion("1.0");
                fileOutputStream = new FileOutputStream(metaDataDesignPath);
                String xmlDesign = JOXUtils
                    .toXML(metaDataDesign, "UTF-8");
                fileOutputStream
                    .write(xmlDesign
                        .getBytes());
                fileOutputStream
                    .close();
            }
            return metaData;
        }
        catch (Exception e)
        {
            throw new JCRException("ERROR.00601", e);
        }
        finally
        {
            if (fileOutputStream != null)
            {
                try
                {
                    fileOutputStream
                        .close();
                }
                catch (Exception e)
                {
                    e
                        .printStackTrace();
                }
            }
        }
    }

    /**
     * 获得元数据
     * 
     * @param fileUuid fileUuid
     * @param version version
     * @return return value
     * @throws BusinessException Business Exception
     */
    public MetaData getMetaData(String fileUuid, String version)
        throws BusinessException
    {
        try
        {
            String fileuuid = fileUuid
                .replaceAll("-", "");
            String folder = JuuidUtils
                .getFilePath(fileuuid);
            String path = null;
            String designPath =
                Constants.JCRROOT + "/" + folder + "/" + "design.xml";
            File designFile = new File(designPath);
            // 有版本的情况
            if (designFile
                .exists())
            {
                path = getMetaFilePath(designFile, version, fileUuid, folder);
            }
            // 没有版本的情况
            else
            {
                path =
                    Constants.JCRROOT + "/" + folder + "/" + fileUuid + ".xml";
            }
            MetaData metaData = (MetaData) JOXUtils
                .fromXML(new File(path), MetaData.class);
            return metaData;
        }
        catch (Exception e)
        {
            throw new JCRException("ERROR.00601", e);
        }
    }

    /**
     * 更新元数据
     * 
     * @param metaData metaData
     * @return return value
     * @throws BusinessException Business Exception
     */
    public MetaData updateMetaData(MetaData metaData)
        throws BusinessException
    {
        FileOutputStream fileOutputStream = null;
        try
        {
            String path = null;
            String fileuuid = metaData
                .getUuid().replaceAll("-", "");
            String folderpath = JuuidUtils
                .getFilePath(fileuuid);
            String newVersion = null;

            File folder = new File(Constants.JCRROOT + "/" + folderpath);
            if (!folder
                .exists())
            {
                @SuppressWarnings("unused")
                Boolean flag = folder
                    .mkdirs();
            }
            // 如果需要产生新版本,递增版本号，并新建源数据文件
            if (metaData
                .getCheckin())
            {
                try
                {
                    newVersion = getVersion(metaData
                        .getVersion());
                }
                catch (Exception e)
                {
                    newVersion = "1.0";
                }
                metaData
                    .setVersion(newVersion);
                path = Constants.JCRROOT + "/" + folderpath + "/" + metaData
                    .getUuid() + "-" + newVersion + ".xml";
                File file = new File(path);
                if (file
                    .exists())
                {
                    throw new JCRException("ERROR.00602");
                }
            }
            // 如果不需要产生版本，直接更新当前版本的源数据文件
            else
            {
                path = Constants.JCRROOT + "/" + folderpath + "/" + metaData
                    .getUuid() + "-" + metaData
                    .getVersion() + ".xml";
            }
            fileOutputStream = new FileOutputStream(path);
            String xml = JOXUtils
                .toXML(metaData, "GB2312");
            fileOutputStream
                .write(xml
                    .getBytes());
            fileOutputStream
                .close();
            // 如果需要产生版本，更新文件源数据描述文件
            if (metaData
                .getCheckin())
            {
                MetaData metaDataDesign = new MetaData();
                String metaDataDesignPath =
                    Constants.JCRROOT + "/" + folderpath + "/" + "design.xml";
                metaDataDesign
                    .setNewVersion(newVersion);
                fileOutputStream = new FileOutputStream(metaDataDesignPath);
                String xmlDesign = JOXUtils
                    .toXML(metaDataDesign, "UTF-8");
                fileOutputStream
                    .write(xmlDesign
                        .getBytes());
                fileOutputStream
                    .close();
            }
            return metaData;
        }
        catch (Exception e)
        {
            throw new JCRException("ERROR.00601", e);
        }
        finally
        {
            if (fileOutputStream != null)
            {
                try
                {
                    fileOutputStream
                        .close();
                }
                catch (Exception e)
                {
                    e
                        .printStackTrace();
                }
            }
        }
    }

    /**
     * 杀毒回退时候处理元数据
     * 
     * @param fileUuid fileUuid
     * @param version version
     * @throws BusinessException Business Exception
     */
    public void updateMetaData(String fileUuid, String version)
        throws BusinessException
    {
        FileOutputStream designfileOutputStream = null;
        try
        {
            String fileuuid = fileUuid
                .replaceAll("-", "");
            String folderpath = JuuidUtils
                .getFilePath(fileuuid);

            if (StringUtils
                .isNotBlank(fileUuid))
            {
                MetaData metaDataDesign = new MetaData();
                String metaDataDesignPath =
                    Constants.JCRROOT + "/" + folderpath + "/" + "design.xml";
                metaDataDesign
                    .setNewVersion(version);
                designfileOutputStream =
                    new FileOutputStream(metaDataDesignPath);
                String xmlDesign = JOXUtils
                    .toXML(metaDataDesign, "UTF-8");
                designfileOutputStream
                    .write(xmlDesign
                        .getBytes());
            }
        }
        catch (Exception e)
        {
            throw new JCRException("ERROR.00601", e);
        }
        finally
        {
            if (designfileOutputStream != null)
            {
                try
                {
                    designfileOutputStream
                        .close();
                }
                catch (IOException e)
                {
                    e
                        .printStackTrace();
                }
            }
        }
    }

    /**
     * 删除元素据
     * 
     * @param metaData metaData
     * @throws BusinessException Business Exception
     */
    public void delMetaData(MetaData metaData)
        throws BusinessException
    {
        try
        {
            String fileuuid = metaData
                .getUuid().replaceAll("-", "");
            String folderpath = JuuidUtils
                .getFilePath(fileuuid);

            if (StringUtils
                .isNotBlank(metaData
                    .getUuid()))
            {
                String path =
                    Constants.JCRROOT + "/" + folderpath + "/" + metaData
                        .getUuid() + "-" + metaData
                        .getVersion() + ".xml";
                File file = new File(path);
                if (file
                    .exists())
                {
                    @SuppressWarnings("unused")
                    Boolean flag = file
                        .delete();
                }
            }
        }
        catch (Exception e)
        {
            throw new JCRException("ERROR.00601", e);
        }
    }

    /**
     * 获取版本文件的元数据文件路径
     * 
     * @param designFile 元数据描述文件
     * @param version 指定文件版本
     * @param fileUuid 文件uuid
     * @param folder 源数据uuid文件夹路径
     * @return String 元数据文件路径
     */
    private String getMetaFilePath(File designFile, String version,
        String fileUuid, String folder)
    {
        String path = null;
        // 默认最高版本
        if (StringUtils
            .isBlank(version))
        {
            MetaData designmetaData = (MetaData) JOXUtils
                .fromXML(designFile, MetaData.class);
            if (StringUtils
                .isNotBlank(designmetaData
                    .getNewVersion()))
            {
                path =
                    Constants.JCRROOT + "/" + folder + "/" + fileUuid + "-"
                        + designmetaData
                            .getNewVersion() + ".xml";
            }
        }
        // 指定版本
        else
        {
            path =
                Constants.JCRROOT + "/" + folder + "/" + fileUuid + "-"
                    + version + ".xml";
        }
        return path;
    }
}
