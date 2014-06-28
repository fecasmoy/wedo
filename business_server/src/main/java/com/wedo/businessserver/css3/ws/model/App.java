package com.wedo.businessserver.css3.ws.model;

import java.math.BigDecimal;

/**
 * 应用webservice pojo
 * 
 * @author c90003207
 * 
 */
public class App
{
    /** 签名 */
    private String accessguid;
    
    /** 唯一标示 */
    private String guid;
    
    /** 总空间 */
    private BigDecimal toalspace;
    
    /** 证书URL */
    private String accesskeyUrl;
    
    /** 域名 */
    private String domainName;
    
    /** IP地址 */
    private String appIp;
    
    /** 应用描述 */
    private String appDescription;
    
    /** 应用类型 */
    private String appType;
    
    /** 应用语言 */
    private String appLanguage;
    
    /** 应用名称 */
    private String appName;
    
    /** 应用语种· */
    private String appClassification;
    
    /** 上传回调接口 */
    private String appUploadCallBack;
    
    private String message;
    
    public String getAccessguid()
    {
        return accessguid;
    }
    
    public void setAccessguid(String accessguid)
    {
        this.accessguid = accessguid;
    }
    
    public BigDecimal getToalspace()
    {
        return toalspace;
    }
    
    public void setToalspace(BigDecimal toalspace)
    {
        this.toalspace = toalspace;
    }
    
    public String getDomainName()
    {
        return domainName;
    }
    
    public void setDomainName(String domainName)
    {
        this.domainName = domainName;
    }
    
    public String getAppIp()
    {
        return appIp;
    }
    
    public void setAppIp(String appIp)
    {
        this.appIp = appIp;
    }
    
    public String getAppDescription()
    {
        return appDescription;
    }
    
    public void setAppDescription(String appDescription)
    {
        this.appDescription = appDescription;
    }
    
    public String getAppType()
    {
        return appType;
    }
    
    public void setAppType(String appType)
    {
        this.appType = appType;
    }
    
    public String getAppLanguage()
    {
        return appLanguage;
    }
    
    public void setAppLanguage(String appLanguage)
    {
        this.appLanguage = appLanguage;
    }
    
    public String getAppName()
    {
        return appName;
    }
    
    public void setAppName(String appName)
    {
        this.appName = appName;
    }
    
    public String getAppClassification()
    {
        return appClassification;
    }
    
    public void setAppClassification(String appClassification)
    {
        this.appClassification = appClassification;
    }
    
    public String getMessage()
    {
        return message;
    }
    
    public void setMessage(String message)
    {
        this.message = message;
    }
    
    public String getAccesskeyUrl()
    {
        return accesskeyUrl;
    }
    
    public void setAccesskeyUrl(String accesskeyUrl)
    {
        this.accesskeyUrl = accesskeyUrl;
    }
    
    public String getGuid()
    {
        return guid;
    }
    
    public void setGuid(String guid)
    {
        this.guid = guid;
    }
    
    public String getAppUploadCallBack()
    {
        return appUploadCallBack;
    }
    
    public void setAppUploadCallBack(String appUploadCallBack)
    {
        this.appUploadCallBack = appUploadCallBack;
    }
}
