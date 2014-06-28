package com.wedo.businessserver.css3.ws.model;

import java.util.List;

/**
 * 搜索引擎返回的文件ws pojo
 * 
 * @author c90003207
 * 
 */
public class SFile
{
    /** 文件名称 */
    private String name;

    /** 文件uuid */
    private String fileuuid;

    /** 文件大小 */
    private Long fileSize;

    /** 发件人 */
    private String fileFrom;

    /** 收件人 */
    private String sendTo;

    /** NOTEURL */
    private String fileNoteUrl;

    /** 高亮显示 */
    private String hilight;

    /** 文件上传时间 */
    private String lastModifyTime;

    /** 版本 */
    private String version;

    /** 文件类型 */
    private String fileType;

    /** 邮件内容 */
    private String notesContent;

    /** 附件 */
    private List<NotesAttachment> accachlist;

    /**
     * sFileHitorys
     */
    private List<SFileHitory> sFileHitorys;

    /**
     * getName
     * 
     * @return string
     */
    public String getName()
    {
        return name;
    }

    /**
     * setName
     * 
     * @param name name
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * getFileuuid
     * 
     * @return string
     */
    public String getFileuuid()
    {
        return fileuuid;
    }

    /**
     * setFileuuid
     * 
     * @param fileuuid fileuuid
     */
    public void setFileuuid(String fileuuid)
    {
        this.fileuuid = fileuuid;
    }

    /**
     * getFileSize
     * 
     * @return long
     */
    public Long getFileSize()
    {
        return fileSize;
    }

    /**
     * setFileSize
     * 
     * @param fileSize fileSize
     */
    public void setFileSize(Long fileSize)
    {
        this.fileSize = fileSize;
    }

    /**
     * getHilight
     * 
     * @return string
     */
    public String getHilight()
    {
        return hilight;
    }

    /**
     * setHilight
     * 
     * @param hilight hilight
     */
    public void setHilight(String hilight)
    {
        this.hilight = hilight;
    }

    /**
     * getLastModifyTime
     * 
     * @return string
     */
    public String getLastModifyTime()
    {
        return lastModifyTime;
    }

    /**
     * setLastModifyTime
     * 
     * @param lastModifyTime lastModifyTime
     */
    public void setLastModifyTime(String lastModifyTime)
    {
        this.lastModifyTime = lastModifyTime;
    }

    /**
     * getVersion
     * 
     * @return string
     */
    public String getVersion()
    {
        return version;
    }

    /**
     * setVersion
     * 
     * @param version version
     */
    public void setVersion(String version)
    {
        this.version = version;
    }

    /**
     * getSFileHitorys
     * 
     * @return list
     */
    public List<SFileHitory> getSFileHitorys()
    {
        return sFileHitorys;
    }

    /**
     * setSFileHitorys
     * 
     * @param fileHitorys fileHitorys
     */
    public void setSFileHitorys(List<SFileHitory> fileHitorys)
    {
        sFileHitorys = fileHitorys;
    }

    /**
     * getFileType
     * 
     * @return string
     */
    public String getFileType()
    {
        return fileType;
    }

    /**
     * setFileType
     * 
     * @param fileType fileType
     */
    public void setFileType(String fileType)
    {
        this.fileType = fileType;
    }

    /**
     * getFileFrom
     * 
     * @return string
     */
    public String getFileFrom()
    {
        return fileFrom;
    }

    /**
     * setFileFrom
     * 
     * @param fileFrom fileFrom
     */
    public void setFileFrom(String fileFrom)
    {
        this.fileFrom = fileFrom;
    }

    /**
     * getFileNoteUrl
     * 
     * @return string
     */
    public String getFileNoteUrl()
    {
        return fileNoteUrl;
    }

    /**
     * setFileNoteUrl
     * 
     * @param fileNoteUrl fileNoteUrl
     */
    public void setFileNoteUrl(String fileNoteUrl)
    {
        this.fileNoteUrl = fileNoteUrl;
    }

    /**
     * getAccachlist
     * 
     * @return list
     */
    public List<NotesAttachment> getAccachlist()
    {
        return accachlist;
    }

    /**
     * setAccachlist
     * 
     * @param accachlist accachlist
     */
    public void setAccachlist(List<NotesAttachment> accachlist)
    {
        this.accachlist = accachlist;
    }

    /**
     * getNotesContent
     * 
     * @return string
     */
    public String getNotesContent()
    {
        return notesContent;
    }

    /**
     * setNotesContent
     * 
     * @param notesContent notesContent
     */
    public void setNotesContent(String notesContent)
    {
        this.notesContent = notesContent;
    }

    /**
     * getSendTo
     * 
     * @return string
     */
    public String getSendTo()
    {
        return sendTo;
    }

    /**
     * setSendTo
     * 
     * @param sendTo sendTo
     */
    public void setSendTo(String sendTo)
    {
        this.sendTo = sendTo;
    }

}
