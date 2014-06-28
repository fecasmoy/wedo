package com.wedo.businessserver.css3.domain;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

/**
 * 应用主表 每个应用对应一个Wapp
 * 
 * @author c90003207
 */
@Entity
@Table(name = "ci_app")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Wapp
    implements Serializable
{
    
    /**
     * 进行序列化的时候用到的序列码
     */
    private static final long serialVersionUID = -3312099204749865396L;
    
    /**
     * 主键
     * 数据库中的定义：
     * `id` int(10) unsigned NOT NULL auto_increment COMMENT '主键',
     */
    private Long id;
    
    /**
     * 唯一标示
     * 数据库中的定义：
     * `guid` char(32) NOT NULL COMMENT '唯一索引',
     */
    private String guid;
    
    /**
     * 签名
     * 数据库中的定义：
     * `accessguid` varchar(45) NOT NULL COMMENT '签名',
     */
    private String accessguid;
    
    /**
     * 总空间
     * 数据库中的定义：
     * `toalspace` bigint(20) unsigned NOT NULL COMMENT '总空间',
     */
    private BigDecimal toalspace;
    
    /**
     * 已用空间
     * 数据库中的定义：
     * `usespace` bigint(20) unsigned NOT NULL COMMENT '已用空间',
     */
    private BigDecimal usespace;
    
    /**
     * 证书
     * 数据库中的定义：
     * `accesskey` blob COMMENT '数字证书',
     */
    private String accesskey;
    
    /**
     * 证书URL
     */
    private String keyUrl;
    
    /**
     * 域名
     * 数据库中的定义：
     * `domainName` varchar(45) NOT NULL COMMENT '域名',
     */
    private String domainName;
    
    /**
     * IP地址
     * 数据库中的定义：
     * `appIp` varchar(45) NOT NULL COMMENT 'Ip',
     */
    private String appIp;
    
    /**
     * 应用描述
     * 数据库中的定义：
     * `appDescription` varchar(200) default NULL COMMENT '描述',
     */
    private String appDescription;
    
    /**
     * 应用类型
     * 数据库中的定义：
     * `appType` char(1) NOT NULL COMMENT '应用类型(0:自己运营1:接入运营)\0le=''ci_app''',
     */
    private String appType;
    
    /**
     * 应用语言
     * 数据库中的定义：
     * `appLanguage` char(1) NOT NULL COMMENT '应用语言(0:C,1:C++2:net3:java)\r\n\0言\0\0',
     */
    private String appLanguage;
    
    /**
     * 应用名称
     * 数据库中的定义：
     * `appName` varchar(200) NOT NULL COMMENT '应用名称',
     */
    private String appName;
    
    /**
     * 应用语种·
     * 数据库中的定义：
     * `appClassification` char(1) NOT NULL COMMENT '应用语种(0:中文,1:英文)\0n\0\0\0\0',
     */
    private String appClassification;
    
    /**
     * 上传回调接口
     * 数据库中的定义：
     * `appUploadCallBack` varchar(200) default NULL COMMENT '应用回调',
     */
    private String appUploadCallBack;
    
    /**
     * id的get方法
     * 
     * @return 返回值
     */
    @Id
    @GeneratedValue(generator = "identity")
    @GenericGenerator(name = "identity", strategy = "identity")
    public Long getId()
    {
        return id;
    }
    
    /**
     * id的set方法
     * 
     * @param id id
     */
    public void setId(Long id)
    {
        this.id = id;
    }
    
    /**
     * guid的get方法
     * 
     * @return 返回值
     */
    @Column(name = "guid")
    public String getGuid()
    {
        return guid;
    }
    
    /**
     * guid的set方法
     * 
     * @param guid guid
     */
    public void setGuid(String guid)
    {
        this.guid = guid;
    }
    
    /**
     * accessguid的get方法
     * 
     * @return 返回值
     */
    @Column(name = "accessguid")
    public String getAccessguid()
    {
        return accessguid;
    }
    
    /**
     * accessguid的set方法
     * 
     * @param accessguid accessguid
     */
    public void setAccessguid(String accessguid)
    {
        this.accessguid = accessguid;
    }
    
    /**
     * toalspace的get方法
     * 
     * @return 返回值
     */
    @Column(name = "toalspace")
    public BigDecimal getToalspace()
    {
        return toalspace;
    }
    
    /**
     * toalspace的set方法
     * 
     * @param toalspace toalspace
     */
    public void setToalspace(BigDecimal toalspace)
    {
        this.toalspace = toalspace;
    }
    
    /**
     * usespace的get方法
     * 
     * @return 返回值
     */
    @Column(name = "usespace")
    public BigDecimal getUsespace()
    {
        return usespace;
    }
    
    /**
     * usespace的set方法
     * 
     * @param usespace usespace
     */
    public void setUsespace(BigDecimal usespace)
    {
        this.usespace = usespace;
    }
    
    /**
     * accesskey的get方法
     * 
     * @return 返回值
     */
    @Column(name = "accesskey")
    public String getAccesskey()
    {
        return accesskey;
    }
    
    /**
     * accesskey的set方法
     * 
     * @param accesskey accesskey
     */
    public void setAccesskey(String accesskey)
    {
        this.accesskey = accesskey;
    }
    
    /**
     * domainName的get方法
     * 
     * @return 返回值
     */
    @Column(name = "domainName")
    public String getDomainName()
    {
        return domainName;
    }
    
    /**
     * domainName的set方法
     * 
     * @param domainName domainName
     */
    public void setDomainName(String domainName)
    {
        this.domainName = domainName;
    }
    
    /**
     * appIp的get方法
     * 
     * @return 返回值
     */
    @Column(name = "appIp")
    public String getAppIp()
    {
        return appIp;
    }
    
    /**
     * appIp的set方法
     * 
     * @param appIp appIp
     */
    public void setAppIp(String appIp)
    {
        this.appIp = appIp;
    }
    
    /**
     * appDescription的get方法
     * 
     * @return 返回值
     */
    @Column(name = "appDescription")
    public String getAppDescription()
    {
        return appDescription;
    }
    
    /**
     * appDescription的set方法
     * 
     * @param appDescription appDescription
     */
    public void setAppDescription(String appDescription)
    {
        this.appDescription = appDescription;
    }
    
    /**
     * appType的get方法
     * 
     * @return 返回值
     */
    @Column(name = "appType")
    public String getAppType()
    {
        return appType;
    }
    
    /**
     * appType的set方法
     * 
     * @param appType appType
     */
    public void setAppType(String appType)
    {
        this.appType = appType;
    }
    
    /**
     * appLanguage的get方法
     * 
     * @return 返回值
     */
    @Column(name = "appLanguage")
    public String getAppLanguage()
    {
        return appLanguage;
    }
    
    /**
     * appLanguage的set方法
     * 
     * @param appLanguage appLanguage
     */
    public void setAppLanguage(String appLanguage)
    {
        this.appLanguage = appLanguage;
    }
    
    /**
     * appName的get方法
     * 
     * @return 返回值
     */
    @Column(name = "appName")
    public String getAppName()
    {
        return appName;
    }
    
    /**
     * appName的set方法
     * 
     * @param appName appName
     */
    public void setAppName(String appName)
    {
        this.appName = appName;
    }
    
    /**
     * appClassification的get方法
     * 
     * @return 返回值
     */
    @Column(name = "appClassification")
    public String getAppClassification()
    {
        return appClassification;
    }
    
    /**
     * appClassification的set方法
     * 
     * @param appClassification appClassification
     */
    public void setAppClassification(String appClassification)
    {
        this.appClassification = appClassification;
    }
    
    /**
     * keyUrl的get方法
     * 
     * @return 返回值
     */
    @Transient
    public String getKeyUrl()
    {
        return keyUrl;
    }
    
    /**
     * keyUrl的set方法
     * 
     * @param keyUrl keyUrl
     */
    public void setKeyUrl(String keyUrl)
    {
        this.keyUrl = keyUrl;
    }
    
    /**
     * appUploadCallBack的get方法
     * 
     * @return 返回值
     */
    @Column(name = "appUploadCallBack")
    public String getAppUploadCallBack()
    {
        return appUploadCallBack;
    }
    
    /**
     * appUploadCallBack的set方法
     * 
     * @param appUploadCallBack appUploadCallBack
     */
    public void setAppUploadCallBack(String appUploadCallBack)
    {
        this.appUploadCallBack = appUploadCallBack;
    }
    
}
