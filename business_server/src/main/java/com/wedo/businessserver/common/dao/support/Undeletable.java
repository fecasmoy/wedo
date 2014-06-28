package com.wedo.businessserver.common.dao.support;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * un-delete
 * 
 * @author c90003207
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Undeletable
{
    
    /**
     * 伪删除的列
     */
    String status() default "status";
    
    /**
     * 标志位
     */
    String value() default "1";
}
