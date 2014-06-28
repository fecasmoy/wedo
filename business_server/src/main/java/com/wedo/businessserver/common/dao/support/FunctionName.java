package com.wedo.businessserver.common.dao.support;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * function name interface
 * 
 * @author c90003207
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface FunctionName
{
    
    /**
     * 存储过程调用名称
     */
    String name();
    
    /**
     * pojo中代表总行数的名称
     */
    String countName();
}
