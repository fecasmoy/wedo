package com.wedo.businessserver.common.dao.support;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

/**
 * 对Criteria查询进行Setup
 * 
 * @author calvin
 */
public class CriteriaSetup
{
    
    /**
     * 将查询条件加入到Criteria中
     * 
     * @param criteria criteria
     * @param filter filter
     */
    public void setup(Criteria criteria, Map<String, Object> filter)
    {
        // 要求条件不为空
        if (filter != null && !filter.isEmpty())
        {
            Iterator<Entry<String, Object>> it = filter.entrySet().iterator();
            // 遍历所有的查询条件
            while (it.hasNext())
            {
                Entry<String, Object> entry = it.next();
                if (StringUtils.isNotBlank((String) entry.getValue()))
                {
                    criteria.add(Restrictions.eq((String) entry.getKey(), entry
                        .getValue()));
                }
            }
        }
    }
}
