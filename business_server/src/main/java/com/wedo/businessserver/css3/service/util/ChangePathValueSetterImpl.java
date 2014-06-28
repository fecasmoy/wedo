package com.wedo.businessserver.css3.service.util;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 移动和复制文件夹时，所需的文件夹路径更改，JDBC批量更改内部类
 * 
 * @author l00100468
 * 
 */
public class ChangePathValueSetterImpl
    implements BatchValueSetter
{

    private final List<Map<Long, String>> list;

    /**
     * 文件夹路径批量更改，构造函数
     * 
     * @param sqlList Map参数列表：KEY-文件夹记录id、VALUE-文件夹path
     */
    public ChangePathValueSetterImpl(List<Map<Long, String>> sqlList)
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
     * JDBC批量更新，必须实现的参数设置接口 参数服务SQL语句：update ci_folder c set c.treepath=? where
     * c.id=?
     * 
     * @param ps ps
     * @param i i
     * @throws SQLException SQLException
     */
    public void setValues(java.sql.PreparedStatement ps, int i)
        throws SQLException
    {
        Map<Long, String> row = list
            .get(i);
        Iterator<Entry<Long, String>> it = row
            .entrySet().iterator();
        while (it
            .hasNext())
        {
            Entry<Long, String> entry = it
                .next();
            ps
                .setString(1, entry
                    .getValue());
            ps
                .setLong(SIZE_2, entry
                    .getKey());
        }
    }
}
