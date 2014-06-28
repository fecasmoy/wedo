package com.wedo.businessserver.css3.service.util;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.wedo.businessserver.css3.service.SystemParameter;

/**
 * 移动和复制文件夹时，所需的文件夹路径更改批量更改，JDBC批量更改内部类
 * 
 * @author l00100468
 * 
 */
public class DelWfileValueSetterImpl
    implements BatchValueSetter
{
    private final List<Map<String, String>> list;

    /**
     * 文件夹路径批量更改，构造函数
     * 
     * @param sqlList Map参数列表：KEY-folderguid、VALUE-删除标识
     */
    public DelWfileValueSetterImpl(List<Map<String, String>> sqlList)
    {
        list = sqlList;
    }

    /**
     * 文件夹路径批量更改，必须实现的语句数接口
     * 
     * @return int 列表大小
     */
    public int getBatchSize()
    {
        return list
            .size();
    }

    /**
     * JDBC批量更新，必须实现的参数设置接口 参数服务SQL语句： update ci_file c set c.avaible=? where
     * c.avaible=? and c.folderguid=?
     * 
     * @param ps ps
     * @param i i
     * @throws SQLException SQLException
     */
    public void setValues(java.sql.PreparedStatement ps, int i)
        throws SQLException
    {
        Map<String, String> row = list
            .get(i);
        Iterator<Entry<String, String>> it = row
            .entrySet().iterator();
        while (it
            .hasNext())
        {
            Entry<String, String> entry = it
                .next();
            ps
                .setString(SIZE_1, entry
                    .getValue());
            ps
                .setString(SIZE_2, SystemParameter.FILE_FOLDER_AVIABLES);
            ps
                .setString(SIZE_3, entry
                    .getKey());
        }
    }

}
