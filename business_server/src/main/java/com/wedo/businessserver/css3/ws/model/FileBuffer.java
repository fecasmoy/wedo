package com.wedo.businessserver.css3.ws.model;

import org.codehaus.xfire.aegis.type.java5.IgnoreProperty;

/**
 * 文件 webservice pojo
 * 
 * @author c90003207
 * 
 */
public class FileBuffer
{
    /**
     * 文件的偏移量
     */
    private Long offset = 0L;
    
    /**
     * 文件名,包含文件的扩展名
     */
    private String name;
    
    /**
     * 文件的总大小，单位Byte
     */
    private Long fullLength;
    
    /**
     * 当前文件的byte
     */
    private byte[] data;
    
    /**
     * 文件的版本
     */
    private String version;
    
    /**
     * 文件的uuid
     */
    private String fileuuid;
    
    /**
     * 是否增量传输
     */
    private Integer mergeFlag;
    
    /**
     * 是否加密
     */
    private Integer encryptFlag;
    
    /**
     * 是否压缩
     */
    private Integer compressFlag;
    
    /**
     * MD5
     */
    private String md5;
    
    /**
     * 下载大小
     */
    private Long downSize;
    
    /**
     * 是否传输完成
     */
    private boolean finish;
    
    /**
     * message
     */
    private String message;
    
    /**
     * offset
     * 
     * @return offset
     */
    public Long getOffset()
    {
        return offset;
    }
    
    /**
     * offset
     * 
     * @param offset offset
     */
    public void setOffset(Long offset)
    {
        this.offset = offset;
    }
    
    /**
     * data
     * 
     * @return data
     */
    public byte[] getData()
    {
        return data;
    }
    
    /**
     * data
     * 
     * @param data data
     */
    public void setData(byte[] data)
    {
        this.data = data;
    }
    
    /**
     * name
     * 
     * @return name
     */
    @IgnoreProperty
    public String getExtName()
    {
        if (name != null && !name.trim().equals(""))
        {
            String[] slices = name.split("\\.");
            return slices[slices.length - 1];
        }
        else
        {
            return null;
        }
    }
    
    /**
     * name
     * 
     * @return name
     */
    public String getName()
    {
        return name;
    }
    
    /**
     * name
     * 
     * @param name name
     */
    public void setName(String name)
    {
        this.name = name;
    }
    
    /**
     * getShortName
     * 
     * @return ShortName
     */
    @IgnoreProperty
    public String getShortName()
    {
        if (name != null && !name.trim().equals(""))
        {
            String[] slices = name.split("\\.");
            String shortname = "";
            for (int i = 0; i < slices.length; i++)
            {
                shortname = shortname.concat("." + slices[i]);
            }
            return shortname;
        }
        else
        {
            return null;
        }
    }
    
    /**
     * version
     * 
     * @return version
     */
    public String getVersion()
    {
        return version;
    }
    
    /**
     * version
     * 
     * @param version version
     */
    public void setVersion(String version)
    {
        this.version = version;
    }
    
    /**
     * fileuuid
     * 
     * @return fileuuid
     */
    public String getFileuuid()
    {
        return fileuuid;
    }
    
    /**
     * fileuuid
     * 
     * @param fileuuid fileuuid
     */
    public void setFileuuid(String fileuuid)
    {
        this.fileuuid = fileuuid;
    }
    
    /**
     * fullLength
     * 
     * @return fullLength
     */
    public Long getFullLength()
    {
        return fullLength;
    }
    
    /**
     * fullLength
     * 
     * @param fullLength fullLength
     */
    public void setFullLength(Long fullLength)
    {
        this.fullLength = fullLength;
    }
    
    /**
     * message
     * 
     * @return message
     */
    public String getMessage()
    {
        return message;
    }
    
    /**
     * message
     * 
     * @param message message
     */
    public void setMessage(String message)
    {
        this.message = message;
    }
    
    /**
     * mergeFlag
     * 
     * @return mergeFlag
     */
    public Integer getMergeFlag()
    {
        return mergeFlag;
    }
    
    /**
     * mergeFlag
     * 
     * @param mergeFlag mergeFlag
     */
    public void setMergeFlag(Integer mergeFlag)
    {
        this.mergeFlag = mergeFlag;
    }
    
    /**
     * encryptFlag
     * 
     * @return encryptFlag
     */
    public Integer getEncryptFlag()
    {
        return encryptFlag;
    }
    
    /**
     * encryptFlag
     * 
     * @param encryptFlag encryptFlag
     */
    public void setEncryptFlag(Integer encryptFlag)
    {
        this.encryptFlag = encryptFlag;
    }
    
    /**
     * compressFlag
     * 
     * @return compressFlag
     */
    public Integer getCompressFlag()
    {
        return compressFlag;
    }
    
    /**
     * compressFlag
     * 
     * @param compressFlag compressFlag
     */
    public void setCompressFlag(Integer compressFlag)
    {
        this.compressFlag = compressFlag;
    }
    
    /**
     * md5
     * 
     * @return md5
     */
    public String getMd5()
    {
        return md5;
    }
    
    /**
     * md5
     * 
     * @param md5 md5
     */
    public void setMd5(String md5)
    {
        this.md5 = md5;
    }
    
    /**
     * downSize
     * 
     * @return downSize
     */
    public Long getDownSize()
    {
        return downSize;
    }
    
    /**
     * downSize
     * 
     * @param downSize downSize
     */
    public void setDownSize(Long downSize)
    {
        this.downSize = downSize;
    }
    
    /**
     * finish
     * 
     * @return finish
     */
    public boolean isFinish()
    {
        return finish;
    }
    
    /**
     * finish
     * 
     * @param finish finish
     */
    public void setFinish(boolean finish)
    {
        this.finish = finish;
    }
    
}
