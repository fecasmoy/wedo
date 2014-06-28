package com.wedo.businessserver.css3.service.util;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

import com.wedo.businessserver.common.util.DateUtils;
import com.wedo.businessserver.css3.domain.WFile;

/**
 * 拷贝指定文件夹文件，插入文件夹下文件记录，JDBC批量更改内部类
 * 
 * @author l00100468
 * 
 */
public class InsertWfileValueSetterImpl
    implements BatchValueSetter
{

    /**
     * WFile对像列表
     */
    private final List<WFile> insertWfileList;

    /**
     * 日期格式化模型
     */
    private final String pattern = "yyyy-MM-dd HH:mm:ss";

    /**
     * 文件夹路径批量更改，构造函数
     * 
     * @param listwfiles WFile对像列表
     */
    public InsertWfileValueSetterImpl(List<WFile> listwfiles)
    {
        insertWfileList = listwfiles;
    }

    /**
     * 文件夹路径批量更改，必须实现的语句数接口
     * 
     * @return int 列表大小
     */
    public int getBatchSize()
    {
        return insertWfileList
            .size();
    }

    /**
     * JDBC批量更新，必须实现的参数设置接口 参数服务SQL语句：insert into
     * ci_file(guid,edition,repguid,fpath,appguid,
     * visitNumber,downNumber,fileSize,fName,
     * fileCreateTime,csnGuid,fileuuid,folderguid,avaible,mergeFlag,encryptFlag,
     * compressFlag,tag,avaibleService,md5)
     * values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)
     * 
     * @param ps ps
     * @param i i
     * @throws SQLException SQLException
     */
    public void setValues(java.sql.PreparedStatement ps, int i)
        throws SQLException
    {
        WFile insertFile = insertWfileList
            .get(i);
        ps
            .setString(SIZE_1, insertFile
                .getGuid());
        ps
            .setString(SIZE_2, insertFile
                .getVersion());
        ps
            .setString(SIZE_3, insertFile
                .getRepguid());
        ps
            .setString(SIZE_4, insertFile
                .getFpath());
        ps
            .setString(SIZE_5, insertFile
                .getAppGuid());
        ps
            .setBigDecimal(SIZE_6, insertFile
                .getVisitNumber());
        ps
            .setBigDecimal(SIZE_7, insertFile
                .getDownNumber());
        ps
            .setLong(SIZE_8, insertFile
                .getFileSize());
        ps
            .setString(SIZE_9, insertFile
                .getFName());
        ps
            .setString(SIZE_10, DateUtils
                .format(Calendar
                    .getInstance(), pattern));
        ps
            .setString(SIZE_11, null);
        ps
            .setString(SIZE_12, insertFile
                .getFileuuid());
        ps
            .setString(SIZE_13, insertFile
                .getFolderguid());
        ps
            .setString(SIZE_14, insertFile
                .getAvaible());
        ps
            .setString(SIZE_15, insertFile
                .getMergeFlag());
        ps
            .setString(SIZE_16, insertFile
                .getEncryptFlag());
        ps
            .setString(SIZE_17, insertFile
                .getCompressFlag());
        ps
            .setString(SIZE_18, null);
        ps
            .setString(SIZE_19, insertFile
                .getAvaibleService());
        ps
            .setString(SIZE_20, null);
    }
}
