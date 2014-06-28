package com.wedo.businessserver.css3.ws.model;

/**
 * 支撑节点CSP webservice pojo
 * 
 * @author c90003207
 * 
 */
public class SupportNode
{
    
    /** 节点Ip */
    private String localIp;
    
    /** 标识（不是唯一标识） */
    private String guid;
    
    /** 挂载记录唯一标识（非唯一索引） */
    private String mountguid;
    
    /** 挂载路径 */
    private String mountPath;
    
    /** 挂载时间 */
    private String mountTime;
    
    /** 启用标识（0，1） */
    private String invocation;
    
    /** 当前写入挂载路径 */
    private String hangsCarriesPath;
    
    /** 存储结点guid */
    private String csnguid;
    
    public String getLocalIp()
    {
        return localIp;
    }
    
    public void setLocalIp(String localIp)
    {
        this.localIp = localIp;
    }
    
    public String getMountPath()
    {
        return mountPath;
    }
    
    public void setMountPath(String mountPath)
    {
        this.mountPath = mountPath;
    }
    
    public String getMountTime()
    {
        return mountTime;
    }
    
    public void setMountTime(String mountTime)
    {
        this.mountTime = mountTime;
    }
    
    public String getInvocation()
    {
        return invocation;
    }
    
    public void setInvocation(String invocation)
    {
        this.invocation = invocation;
    }
    
    public String getHangsCarriesPath()
    {
        return hangsCarriesPath;
    }
    
    public void setHangsCarriesPath(String hangsCarriesPath)
    {
        this.hangsCarriesPath = hangsCarriesPath;
    }
    
    public String getCsnguid()
    {
        return csnguid;
    }
    
    public void setCsnguid(String csnguid)
    {
        this.csnguid = csnguid;
    }
    
    public String getGuid()
    {
        return guid;
    }
    
    public void setGuid(String guid)
    {
        this.guid = guid;
    }
    
    public String getMountguid()
    {
        return mountguid;
    }
    
    public void setMountguid(String mountguid)
    {
        this.mountguid = mountguid;
    }
    
}
