package com.wedo.businessserver.css3.ws.model;

import java.util.HashMap;

/**
 * 文件夹 目录机构 webservice pojo
 * 
 * @author c90003207
 * 
 */
public class ResultBuffer
{
    private HashMap<String, String> folderuuids = new HashMap<String, String>();
    
    private HashMap<String, FileBuffer> fileBuffers =
        new HashMap<String, FileBuffer>();
    
    private Boolean flag;
    
    private String message;
    
    public HashMap<String, String> getFolderuuids()
    {
        return folderuuids;
    }
    
    public void setFolderuuids(HashMap<String, String> folderuuids)
    {
        this.folderuuids = folderuuids;
    }
    
    public HashMap<String, FileBuffer> getFileBuffers()
    {
        return fileBuffers;
    }
    
    public void setFileBuffers(HashMap<String, FileBuffer> fileBuffers)
    {
        this.fileBuffers = fileBuffers;
    }
    
    public String getMessage()
    {
        return message;
    }
    
    public void setMessage(String message)
    {
        this.message = message;
    }
    
    public Boolean getFlag()
    {
        return flag;
    }
    
    public void setFlag(Boolean flag)
    {
        this.flag = flag;
    }
    
}
