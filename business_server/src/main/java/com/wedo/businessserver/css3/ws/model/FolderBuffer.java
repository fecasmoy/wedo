package com.wedo.businessserver.css3.ws.model;

/**
 * 文件夹 webservice pojo
 * 
 * @author c90003207
 * 
 */
public class FolderBuffer
{
    
    private String name; // 文件夹名
    
    private String version;// 文件夹的版本
    
    private String folderUuid;// 文件夹的uuid
    
    private String tag;// 备份集Tag
    
    private String message;// 返回的错误码
    
    private String statFlag;// 是否需要统计
    
    public String getName()
    {
        return name;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    public String getVersion()
    {
        return version;
    }
    
    public void setVersion(String version)
    {
        this.version = version;
    }
    
    public String getFolderUuid()
    {
        return folderUuid;
    }
    
    public void setFolderUuid(String folderUuid)
    {
        this.folderUuid = folderUuid;
    }
    
    public String getMessage()
    {
        return message;
    }
    
    public void setMessage(String message)
    {
        this.message = message;
    }
    
    public String getTag()
    {
        return tag;
    }
    
    public void setTag(String tag)
    {
        this.tag = tag;
    }
    
    public String getStatFlag()
    {
        return statFlag;
    }
    
    public void setStatFlag(String statFlag)
    {
        this.statFlag = statFlag;
    }
    
}
