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
 * 文件历史表
 * 
 * @author c90003207
 * 
 */
@Entity
@Table(name = "ci_file_history")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class WFileHistory
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
    private char avaible;

    /**
     * 是否增量 0：全量 1：增量
     */
    private char mergeFlag;

    /**
     * 是否加密 0：没加密 1：加密
     */
    private char encryptFlag;

    /**
     * 是否压缩 0：没 1：是
     */
    private char compressFlag;

    /**
     * tag
     */
    private String tag;

    /**
     * 文件MD5码
     */
    private String md5;

    /**
     * 是否提供数据服务
     */
    private char avaibleService;

    /**
     * folder的treepath
     */
    private String foldertreepath;

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
     * @param version version
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
     * @param fpath fpath
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
     * appGuid的set方法
     * 
     * @param appGuid appGuid
     */
    public void setAppGuid(String appGuid)
    {
        this.appGuid = appGuid;
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
     * @param visitNumber visitNumber
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
     * @param downNumber downNumber
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
     * @param fileSize fileSize
     */
    public void setFileSize(Long fileSize)
    {
        this.fileSize = fileSize;
    }

    /**
     * name的get方法
     * 
     * @return 返回值
     */
    public String getFName()
    {
        return fName;
    }

    /**
     * name的set方法
     * 
     * @param name name
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
     * @param fileCreateTime fileCreateTime
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
     * @param csnGuid csnGuid
     */
    public void setCsnguid(String csnGuid)
    {
        this.csnguid = csnGuid;
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
     * @param fileuuid fileuuid
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
     * @param folderguid folderguid
     */
    public void setFolderguid(String folderguid)
    {
        this.folderguid = folderguid;
    }

    /**
     * avaible的get方法
     * 
     * @return 返回值
     */
    public char getAvaible()
    {
        return avaible;
    }

    /**
     * avaible的set方法
     * 
     * @param avaible avaible
     */
    public void setAvaible(char avaible)
    {
        this.avaible = avaible;
    }

    /**
     * mergeFlag的get方法
     * 
     * @return 返回值
     */
    public char getMergeFlag()
    {
        return mergeFlag;
    }

    /**
     * mergeFlag的set方法
     * 
     * @param mergeFlag mergeFlag
     */
    public void setMergeFlag(char mergeFlag)
    {
        this.mergeFlag = mergeFlag;
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
    public char getAvaibleService()
    {
        return avaibleService;
    }

    /**
     * avaibleService的set方法
     * 
     * @param avaibleService avaibleService
     */
    public void setAvaibleService(char avaibleService)
    {
        this.avaibleService = avaibleService;
    }

    /**
     * encryptFlag的get方法
     * 
     * @return 返回值
     */
    public char getEncryptFlag()
    {
        return encryptFlag;
    }

    /**
     * encryptFlag的set方法
     * 
     * @param encryptFlag encryptFlag
     */
    public void setEncryptFlag(char encryptFlag)
    {
        this.encryptFlag = encryptFlag;
    }

    /**
     * compressFlag的get方法
     * 
     * @return 返回值
     */
    public char getCompressFlag()
    {
        return compressFlag;
    }

    /**
     * compressFlag的set方法
     * 
     * @param compressFlag compressFlag
     */
    public void setCompressFlag(char compressFlag)
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
     * @param md5 md5 value
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
     * @param foldertreepath folder tree path
     */
    public void setFoldertreepath(String foldertreepath)
    {
        this.foldertreepath = foldertreepath;
    }
}
