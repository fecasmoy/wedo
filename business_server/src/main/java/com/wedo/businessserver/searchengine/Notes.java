package com.wedo.businessserver.searchengine;

import java.util.Date;
import java.util.List;

import com.wedo.businessserver.css3.ws.model.NotesAttachment;

/**
 * NOTES 格式定义
 * 
 * @author c90003207
 * 
 */
public class Notes
{
    /** 内容 */
    private String body;
    
    /** NOTESURL */
    private String noteUrl;
    
    /** URL地址 */
    private String linkUrl;
    
    /** 大小 */
    private Integer size;
    
    /** 发件人 */
    private String from;
    
    /** 主题 */
    private String subject;
    
    /** 日期 */
    private String data;
    
    /** 日期 */
    private Date sendDate;
    
    /** 收件人 */
    private String sendTo;
    
    /** 附件 */
    private List<NotesAttachment> accachlist;
    
    public String getBody()
    {
        return body;
    }
    
    public void setBody(String body)
    {
        this.body = body;
    }
    
    public String getNoteUrl()
    {
        return noteUrl;
    }
    
    public void setNoteUrl(String noteUrl)
    {
        this.noteUrl = noteUrl;
    }
    
    public Integer getSize()
    {
        return size;
    }
    
    public void setSize(Integer size)
    {
        this.size = size;
    }
    
    public String getSubject()
    {
        return subject;
    }
    
    public void setSubject(String subject)
    {
        this.subject = subject;
    }
    
    public String getData()
    {
        return data;
    }
    
    public void setData(String data)
    {
        this.data = data;
    }
    
    public List<NotesAttachment> getAccachlist()
    {
        return accachlist;
    }
    
    public void setAccachlist(List<NotesAttachment> accachlist)
    {
        this.accachlist = accachlist;
    }
    
    public String getFrom()
    {
        return from;
    }
    
    public void setFrom(String from)
    {
        this.from = from;
    }
    
    public String getLinkUrl()
    {
        return linkUrl;
    }
    
    public void setLinkUrl(String linkUrl)
    {
        this.linkUrl = linkUrl;
    }
    
    public String getSendTo()
    {
        return sendTo;
    }
    
    public void setSendTo(String sendTo)
    {
        this.sendTo = sendTo;
    }
    
    public Date getSendDate()
    {
        return sendDate;
    }
    
    public void setSendDate(Date sendDate)
    {
        this.sendDate = sendDate;
    }
    
}
