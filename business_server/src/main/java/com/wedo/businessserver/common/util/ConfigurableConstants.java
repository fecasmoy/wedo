 package com.wedo.businessserver.common.util;
 
 import java.io.IOException;
 import java.io.InputStream;
 import java.util.Properties;
 import org.apache.commons.logging.Log;
 import org.apache.commons.logging.LogFactory;
 
 public class ConfigurableConstants
 {
  private static final Log logger = LogFactory.getLog(ConfigurableConstants.class);
 
  private static final Properties P = new Properties();
 
   protected static void init(String propertyFileName)
   {
    InputStream in = null;
     try
     {
      in = 
        ConfigurableConstants.class.getClassLoader()
        .getResourceAsStream(propertyFileName);
      if (in != null)
       {
        P.load(in);
       }
     }
     catch (IOException e)
     {
      logger.error("load " + propertyFileName + " into Constants error!", 
        e);
 
      if (in != null)
       {
         try
         {
          in.close();
         }
         catch (IOException ie)
         {
          logger.error("close " + propertyFileName + " error!", ie);
         }
       }
     }
     finally
     {
      if (in != null)
       {
         try
         {
          in.close();
         }
         catch (IOException e)
         {
          logger.error("close " + propertyFileName + " error!", e);
         }
       }
     }
   }
 
   protected static String getProperty(String key, String defaultValue)
   {
    return P.getProperty(key, defaultValue);
   }
 }

