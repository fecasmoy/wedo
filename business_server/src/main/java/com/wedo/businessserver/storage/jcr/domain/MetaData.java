package com.wedo.businessserver.storage.jcr.domain;

import java.util.HashMap;

/**
 * 元数据POJO
 * 
 * @author c90003207
 * 
 */
public class MetaData
{
    /**
     * 名称
     */
    private String name;

    /**
     * 父节点uuid
     */
    private String parentUuid;

    /**
     * 仓库uuid
     */
    private String repuuid;

    /**
     * uuid
     */
    private String uuid;

    /**
     * 最新版本
     */
    private String newVersion;

    /**
     * 版本
     */
    private String version;

    /**
     * 应用guid
     */
    private String appGuid;

    /**
     * 文件大小
     */
    private Long size;

    /**
     * 创建时间
     */
    private String createDate;

    /**
     * 是否增量传输
     */
    private String mergeFlag;

    /**
     * 是否加密
     */
    private String encryptFlag;

    /**
     * 是否压缩
     */
    private String compressFlag;

    /**
     * MD5
     */
    private String md5;

    /**
     * 文件路径
     */
    private String filePath;

    /**
     * 自定义元素据
     */
    private HashMap<String, String> map;

    /**
     * 是否生成版本
     */
    private Boolean checkin;

    /**
     * 设置对象名称
     * 
     * @return return value
     */
    public String getName()
    {
        return name;
    }

    /**
     * 获得对象名称
     * 
     * @param name name
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * 设置对象父节点uuid
     * 
     * @return return value
     */
    public String getParentUuid()
    {
        return parentUuid;
    }

    /**
     * 获取对象父节点uuid
     * 
     * @param parentUuid parentUuid
     */
    public void setParentUuid(String parentUuid)
    {
        this.parentUuid = parentUuid;
    }

    /**
     * 设置仓库uuid
     * 
     * @return return value
     */
    public String getRepuuid()
    {
        return repuuid;
    }

    /**
     * 获取仓库uuid
     * 
     * @param repuuid repuuid
     */
    public void setRepuuid(String repuuid)
    {
        this.repuuid = repuuid;
    }

    /**
     * 设置对象uuid
     * 
     * @return return value
     */
    public String getUuid()
    {
        return uuid;
    }

    /**
     * 获取对象uuid
     * 
     * @param uuid uuid
     */
    public void setUuid(String uuid)
    {
        this.uuid = uuid;
    }

    /**
     * 设置对象版本
     * 
     * @return return value
     */
    public String getVersion()
    {
        return version;
    }

    /**
     * 获取对象版本
     * 
     * @param version version
     */
    public void setVersion(String version)
    {
        this.version = version;
    }

    /**
     * getter appguid
     * 
     * @return return value
     */
    public String getAppGuid()
    {
        return appGuid;
    }

    /**
     * setter appGuid
     * 
     * @param appGuid appGuid
     */
    public void setAppGuid(String appGuid)
    {
        this.appGuid = appGuid;
    }

    /**
     * 获取对象大小
     * 
     * @return return value
     */
    public Long getSize()
    {
        return size;
    }

    /**
     * 设置对象大小
     * 
     * @param size size
     */
    public void setSize(Long size)
    {
        this.size = size;
    }

    /**
     * 获取对象创建时间
     * 
     * @return return value
     */
    public String getCreateDate()
    {
        return createDate;
    }

    /**
     * 设置对象创建时间
     * 
     * @param createDate createDate
     */
    public void setCreateDate(String createDate)
    {
        this.createDate = createDate;
    }

    /**
     * 获取对象是否增量
     * 
     * @return return value
     */
    public String getMergeFlag()
    {
        return mergeFlag;
    }

    /**
     * 设置对象是否增量
     * 
     * @param mergeFlag mergeFlag
     */
    public void setMergeFlag(String mergeFlag)
    {
        this.mergeFlag = mergeFlag;
    }

    /**
     * 获取对象是否加密
     * 
     * @return return value
     */
    public String getEncryptFlag()
    {
        return encryptFlag;
    }

    /**
     * 设置对象是否加密
     * 
     * @param encryptFlag encryptFlag
     */
    public void setEncryptFlag(String encryptFlag)
    {
        this.encryptFlag = encryptFlag;
    }

    /**
     * 获取对象是否压缩
     * 
     * @return return value
     */
    public String getCompressFlag()
    {
        return compressFlag;
    }

    /**
     * 设置对象是否压缩
     * 
     * @param compressFlag compressFlag
     */
    public void setCompressFlag(String compressFlag)
    {
        this.compressFlag = compressFlag;
    }

    /**
     * 获取对象MD5
     * 
     * @return return value
     */
    public String getMd5()
    {
        return md5;
    }

    /**
     * 设置对象MD5
     * 
     * @param md5 md5
     */
    public void setMd5(String md5)
    {
        this.md5 = md5;
    }

    /**
     * 获取对象自定义属性
     * 
     * @return return value
     */
    public HashMap<String, String> getMap()
    {
        return map;
    }

    /**
     * 设置对象自定义属性
     * 
     * @param map map
     */
    public void setMap(HashMap<String, String> map)
    {
        this.map = map;
    }

    /**
     * 获取对象存放路径
     * 
     * @return return value
     */
    public String getFilePath()
    {
        return filePath;
    }

    /**
     * 设置对象存放路径
     * 
     * @param filePath file Path
     */
    public void setFilePath(String filePath)
    {
        this.filePath = filePath;
    }

    /**
     * 获取对象是否checkin
     * 
     * @return return value
     */
    public Boolean getCheckin()
    {
        return checkin;
    }

    /**
     * 设置对象是否checkin
     * 
     * @param checkin checkin
     */
    public void setCheckin(Boolean checkin)
    {
        this.checkin = checkin;
    }

    /**
     * 获取对象现在的版本
     * 
     * @return return value
     */
    public String getNewVersion()
    {
        return newVersion;
    }

    /**
     * 设置对象现在的版本
     * 
     * @param newVersion newVersion
     */
    public void setNewVersion(String newVersion)
    {
        this.newVersion = newVersion;
    }
}
