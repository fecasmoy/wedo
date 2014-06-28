package com.wedo.businessserver.css3.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;

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
 * 文件表
 * 
 * @author c90003207
 * 
 */
@Entity
@Table(name = "ci_file")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class WFile
    implements Serializable
{
    
    /**
     * 进行序列化的时候用到的序列码
     */
    private static final long serialVersionUID = -7152955345127534413L;
    
    /**
     * 主键
     */
    private Long id;
    
    /**
     * guid
     */
    private String guid;
    
    /**
     * 版本
     */
    private String version;
    
    /**
     * 仓库guid
     */
    private String repguid;
    
    /**
     * 文件相对真实路径
     */
    private String fpath;
    
    /**
     * 应用guid
     */
    private String appGuid;
    
    /**
     * 累计读取次数
     */
    private BigDecimal visitNumber;
    
    /**
     * 累计下载次数
     */
    private BigDecimal downNumber;
    
    /**
     * 文件大小
     */
    private Long fileSize;
    
    /**
     * 文件名称
     */
    private String fName;
    
    /**
     * 文件创建时间
     */
    private Calendar fileCreateTime;
    
    /**
     * csnguid
     */
    private String csnguid;
    
    /**
     * 文件uuid
     */
    private String fileuuid;
    
    /**
     * 文件夹guid
     */
    private String folderguid;
    
    /**
     * 是否可用 0-有效，1-逻辑删除效，2-物理删除
     */
    private String avaible;
    
    /**
     * 是否增量 0：全量 1：增量
     */
    private String mergeFlag;
    
    /**
     * 是否加密 0：没加密 1：加密
     */
    private String encryptFlag;
    
    /**
     * 是否压缩 0：没 1：是
     */
    private String compressFlag;
    
    /**
     * 文件MD5码
     */
    private String md5;
    
    /**
     * tag
     */
    private String tag;
    
    /**
     * 是否提供数据服务
     */
    private String avaibleService;
    
    /**
     * folder的treepath
     */
    private String foldertreepath;
    
    /**
     * 前一个最新的版本文件
     */
    private WFileHistory wFileHistory;
    
    /**
     * 是否最新版本
     */
    private Boolean flag;
    
    /**
     * id的get方法
     * 
     * @return id
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
     * version的get方法
     * 
     * @return 返回值
     */
    @Column(name = "edition")
    public String getVersion()
    {
        return version;
    }
    
    /**
     * version的set方法
     * 
     * @param version 版本
     */
    public void setVersion(String version)
    {
        this.version = version;
    }
    
    /**
     * repguid的get方法
     * 
     * @return 返回值
     */
    public String getRepguid()
    {
        return repguid;
    }
    
    /**
     * repguid的set方法
     * 
     * @param repguid repguid
     */
    public void setRepguid(String repguid)
    {
        this.repguid = repguid;
    }
    
    /**
     * fpath的get方法
     * 
     * @return 返回值
     */
    public String getFpath()
    {
        return fpath;
    }
    
    /**
     * fpath的set方法
     * 
     * @param fpath 文件路径
     */
    public void setFpath(String fpath)
    {
        this.fpath = fpath;
    }
    
    /**
     * appguid的get方法
     * 
     * @return 返回值
     */
    @Column(name = "appguid")
    public String getAppGuid()
    {
        return appGuid;
    }

    /**
     * appguid的set方法
     * 
     * @param appguid 应用guid
     */
    public void setAppGuid(String appguid)
    {
        this.appGuid = appguid;
    }
    
    /**
     * visitNumber的get方法
     * 
     * @return 返回值
     */
    public BigDecimal getVisitNumber()
    {
        return visitNumber;
    }
    
    /**
     * visitNumber的set方法
     * 
     * @param visitNumber 访问次数
     */
    public void setVisitNumber(BigDecimal visitNumber)
    {
        this.visitNumber = visitNumber;
    }
    
    /**
     * downNumber的get方法
     * 
     * @return 返回值
     */
    public BigDecimal getDownNumber()
    {
        return downNumber;
    }
    
    /**
     * downNumber的set方法
     * 
     * @param downNumber 下载次数
     */
    public void setDownNumber(BigDecimal downNumber)
    {
        this.downNumber = downNumber;
    }
    
    /**
     * fileSize的get方法
     * 
     * @return 返回值
     */
    public Long getFileSize()
    {
        return fileSize;
    }
    
    /**
     * fileSize的set方法
     * 
     * @param fileSize 文件大小
     */
    public void setFileSize(Long fileSize)
    {
        this.fileSize = fileSize;
    }
    
    /**
     * fName的get方法
     * 
     * @return 返回值
     */
    public String getFName()
    {
        return fName;
    }
    
    /**
     * fName的set方法
     * 
     * @param name 文件名称
     */
    public void setFName(String name)
    {
        fName = name;
    }
    
    /**
     * fileCreateTime的get方法
     * 
     * @return 返回值
     */
    public Calendar getFileCreateTime()
    {
        return fileCreateTime;
    }
    
    /**
     * fileCreateTime的set方法
     * 
     * @param fileCreateTime 文件创建时间
     */
    public void setFileCreateTime(Calendar fileCreateTime)
    {
        this.fileCreateTime = fileCreateTime;
    }
    
    /**
     * csnGuid的get方法
     * 
     * @return 返回值
     */
    @Column(name = "csnGuid")
    public String getCsnguid()
    {
        return csnguid;
    }
    
    /**
     * csnGuid的set方法
     * 
     * @param csnguid CSN的guid
     */
    public void setCsnguid(String csnguid)
    {
        this.csnguid = csnguid;
    }
    
    /**
     * fileuuid的get方法
     * 
     * @return 返回值
     */
    public String getFileuuid()
    {
        return fileuuid;
    }
    
    /**
     * fileuuid的set方法
     * 
     * @param fileuuid 文件的uuid
     */
    public void setFileuuid(String fileuuid)
    {
        this.fileuuid = fileuuid;
    }
    
    /**
     * folderguid的get方法
     * 
     * @return 返回值
     */
    public String getFolderguid()
    {
        return folderguid;
    }
    
    /**
     * folderguid的set方法
     * 
     * @param folderguid 文件夹的guid
     */
    public void setFolderguid(String folderguid)
    {
        this.folderguid = folderguid;
    }
    
    /**
     * tag的get方法
     * 
     * @return 返回值
     */
    public String getTag()
    {
        return tag;
    }
    
    /**
     * tag的set方法
     * 
     * @param tag tag
     */
    public void setTag(String tag)
    {
        this.tag = tag;
    }
    
    /**
     * avaibleService的get方法
     * 
     * @return 返回值
     */
    public String getAvaibleService()
    {
        return avaibleService;
    }
    
    /**
     * avaibleService的set方法
     * 
     * @param avaibleService 可用业务
     */
    public void setAvaibleService(String avaibleService)
    {
        this.avaibleService = avaibleService;
    }
    
    /**
     * avaible的get方法
     * 
     * @return 返回值
     */
    public String getAvaible()
    {
        return avaible;
    }
    
    /**
     * avaible的set方法
     * 
     * @param avaible 是否删除
     */
    public void setAvaible(String avaible)
    {
        this.avaible = avaible;
    }
    
    /**
     * mergeFlag的get方法
     * 
     * @return 返回值
     */
    public String getMergeFlag()
    {
        return mergeFlag;
    }
    
    /**
     * mergeFlag的set方法
     * 
     * @param mergeFlag merge 标识
     */
    public void setMergeFlag(String mergeFlag)
    {
        this.mergeFlag = mergeFlag;
    }
    
    /**
     * encryptFlag的get方法
     * 
     * @return 返回值
     */
    public String getEncryptFlag()
    {
        return encryptFlag;
    }
    
    /**
     * encryptFlag的set方法
     * 
     * @param encryptFlag 是否加密
     */
    public void setEncryptFlag(String encryptFlag)
    {
        this.encryptFlag = encryptFlag;
    }
    
    /**
     * compressFlag的get方法
     * 
     * @return 返回值
     */
    public String getCompressFlag()
    {
        return compressFlag;
    }
    
    /**
     * compressFlag的set方法
     * 
     * @param compressFlag 是否压缩
     */
    public void setCompressFlag(String compressFlag)
    {
        this.compressFlag = compressFlag;
    }
    
    /**
     * md5的get方法
     * 
     * @return 返回值
     */
    public String getMd5()
    {
        return md5;
    }
    
    /**
     * md5的set方法
     * 
     * @param md5 md5
     */
    public void setMd5(String md5)
    {
        this.md5 = md5;
    }
    
    /**
     * foldertreepath的get方法
     * 
     * @return 返回值
     */
    @Transient
    public String getFoldertreepath()
    {
        return foldertreepath;
    }
    
    /**
     * foldertreepath的set方法
     * 
     * @param foldertreepath 文件夹的treepath
     */
    public void setFoldertreepath(String foldertreepath)
    {
        this.foldertreepath = foldertreepath;
    }
    
    /**
     * WfileHistory的get方法
     * 
     * @return 返回值
     */
    @Transient
    public WFileHistory getWFileHistory()
    {
        return wFileHistory;
    }
    
    /**
     * WfileHistory的set方法
     * 
     * @param wfileHistory 文件历史版本
     */
    public void setWFileHistory(WFileHistory wfileHistory)
    {
        wFileHistory = wfileHistory;
    }
    
    /**
     * flag的get方法
     * 
     * @return 返回值
     */
    @Transient
    public Boolean getFlag()
    {
        return flag;
    }
    
    /**
     * flag的set方法
     * 
     * @param flag 文件标识
     */
    public void setFlag(Boolean flag)
    {
        this.flag = flag;
    }
    
}
