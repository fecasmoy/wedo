package com.wedo.businessserver.css3.ws.model;

/**
 * 历史版本
 * 
 * @author c90003207
 * 
 */
public class SFileHitory
{
    /** 文件名称 */
    private String name;
    
    /** 文件uuid */
    private String fileuuid;
    
    /** 文件大小 */
    private Long fileSize;
    
    /** 高亮显示 */
    private String hilight;
    
    /** 文件上传时间 */
    private String lastModifyTime;
    
    /** 版本 */
    private String version;
    
    public String getName()
    {
        return name;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    public String getFileuuid()
    {
        return fileuuid;
    }
    
    public void setFileuuid(String fileuuid)
    {
        this.fileuuid = fileuuid;
    }
    
    public Long getFileSize()
    {
        return fileSize;
    }
    
    public void setFileSize(Long fileSize)
    {
        this.fileSize = fileSize;
    }
    
    public String getHilight()
    {
        return hilight;
    }
    
    public void setHilight(String hilight)
    {
        this.hilight = hilight;
    }
    
    public String getLastModifyTime()
    {
        return lastModifyTime;
    }
    
    public void setLastModifyTime(String lastModifyTime)
    {
        this.lastModifyTime = lastModifyTime;
    }
    
    public String getVersion()
    {
        return version;
    }
    
    public void setVersion(String version)
    {
        this.version = version;
    }
}
