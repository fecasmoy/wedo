package com.wedo.businessserver.css3.ws.model;

/**
 * NOTES附件传输的pojo
 * 
 * @author c90003207
 * 
 */
public class NotesAttachment
{
    /** 附件名称 */
    private String name;
    
    /** 下载地址 */
    private String linkUrl;
    
    /**
     * 获得附件名称
     * 
     * @return {@link String}
     */
    public String getName()
    {
        return name;
    }
    
    /**
     * 设置附件名称
     * 
     * @param name name
     */
    public void setName(String name)
    {
        this.name = name;
    }
    
    /**
     * 获得附件下载地址
     * 
     * @return {@link String}
     */
    public String getLinkUrl()
    {
        return linkUrl;
    }
    
    /**
     * 设置附件下载地址
     * 
     * @param linkUrl link url
     */
    public void setLinkUrl(String linkUrl)
    {
        this.linkUrl = linkUrl;
    }
}
