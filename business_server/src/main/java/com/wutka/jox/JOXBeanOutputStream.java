package com.wutka.jox;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;

import org.xml.sax.ContentHandler;

import com.wutka.dtd.DTD;

/**
 * An output stream filter that writes out a bean as an XML document. The stream
 * can write out basic Java types, their object equivalents and also strings,
 * dates and other beans. The XML tag names are the same as the bean properties.
 * 
 * @author Mark Wutka
 * @version 1.0 05/08/2000
 * @version 1.1 05/09/2000
 * @version 1.2 06/01/2000
 * @version 1.11 11/30/2000
 */

public class JOXBeanOutputStream
    extends FilterOutputStream
    implements JOXOutput
{
    private JOXBeanOutput output;
    
    private String encoding;
    
    private JOXConfig config;
    
    /**
     * Create a new output stream around an existing stream
     * 
     * @param baseOutputStream The underlying output stream
     */
    public JOXBeanOutputStream(OutputStream baseOutputStream)
    {
        this(baseOutputStream, false, JOXBeanOutput.DEFAULT_ENCODING);
    }
    
    /**
     * Create a new output stream around an existing stream
     * 
     * @param baseOutputStream The underlying output stream
     * @param writeAttributes Indicates whether we should write simple
     *            properties as
     *            attributes
     * @deprecated Use JOXConfig to set the writeAttributes flag
     *             (atomsAsAttributes)
     */
    public JOXBeanOutputStream(OutputStream baseOutputStream,
        boolean writeAttributes)
    {
        this(baseOutputStream, JOXBeanOutput.DEFAULT_ENCODING);
        if (config == null)
        {
            config = JOXConfig.getModifiableConfig();
            output.setConfig(config);
        }
        config.setAtomsAsAttributes(writeAttributes);
    }
    
    /**
     * Create a new output stream around an existing stream
     * 
     * @param baseOutputStream The underlying output stream
     * @param writeAttributes Indicates whether we should write simple
     *            properties as
     *            attributes
     * @param anEncoding The XML encoding for the output
     * @deprecated Use JOXConfig to set the writeAttributes flag
     *             (atomsAsAttributes)
     */
    public JOXBeanOutputStream(OutputStream baseOutputStream,
        boolean writeAttributes, String anEncoding)
    {
        this(baseOutputStream, anEncoding);
        if (config == null)
        {
            config = JOXConfig.getModifiableConfig();
            output.setConfig(config);
        }
        config.setAtomsAsAttributes(writeAttributes);
    }
    
    /**
     * Create a new output stream around an existing stream
     * 
     * @param baseOutputStream The underlying output stream
     * @param anEncoding The XML encoding for the output
     */
    public JOXBeanOutputStream(OutputStream baseOutputStream, String anEncoding)
    {
        super(baseOutputStream);
        
        this.encoding = anEncoding;
        
        output = new JOXBeanOutput(this.setupSink(baseOutputStream));
        output.setConfig(config);
    }
    
    /**
     * Create a new output stream around an existing stream and specifies a DTD
     * for selecting which attributes should be written and what the names
     * should look like.
     * 
     * @param dtdURI The URI of the DTD
     * @param baseOutputStream The underlying output stream
     * @throws IOException io exception
     */
    public JOXBeanOutputStream(String dtdURI, OutputStream baseOutputStream)
        throws IOException
    {
        this(dtdURI, baseOutputStream, JOXBeanOutput.DEFAULT_ENCODING);
    }
    
    /**
     * Create a new output stream around an existing stream and specifies a DTD
     * for selecting which attributes should be written and what the names
     * should look like.
     * 
     * @param dtdURI The URI of the DTD
     * @param baseOutputStream The underlying output stream
     * @param anEncoding The XML encoding for the output
     * @throws IOException io exception
     */
    public JOXBeanOutputStream(String dtdURI, OutputStream baseOutputStream,
        String anEncoding)
        throws IOException
    {
        super(baseOutputStream);
        
        this.encoding = anEncoding;
        
        output = new JOXBeanOutput(this.setupSink(baseOutputStream), dtdURI);
    }
    
    /**
     * Create a new output stream around an existing stream and specifies a DTD
     * for selecting which attributes should be written and what the names
     * should look like.
     * 
     * @param dtd The DTD to use
     * @param baseOutputStream The underlying output stream
     * @throws IOException io exception
     */
    public JOXBeanOutputStream(DTD dtd, OutputStream baseOutputStream)
        throws IOException
    {
        this(dtd, baseOutputStream, JOXBeanOutput.DEFAULT_ENCODING);
    }
    
    /**
     * Create a new output stream around an existing stream and specifies a DTD
     * for selecting which attributes should be written and what the names
     * should look like.
     * 
     * @param dtd The DTD to use
     * @param baseOutputStream The underlying output stream
     * @param anEncoding encoding
     * @throws IOException io exception
     */
    public JOXBeanOutputStream(DTD dtd, OutputStream baseOutputStream,
        String anEncoding)
        throws IOException
    {
        super(baseOutputStream);
        
        this.encoding = anEncoding;
        
        output = new JOXBeanOutput(this.setupSink(baseOutputStream), dtd);
        output.setConfig(config);
    }
    
    /**
     * getter
     * 
     * @return the output
     */
    public JOXBeanOutput getOutput()
    {
        return output;
    }
    
    /**
     * setter
     * 
     * @param output the output to set
     */
    public void setOutput(JOXBeanOutput output)
    {
        this.output = output;
    }
    
    /**
     * getter
     * 
     * @return the encoding
     */
    public String getEncoding()
    {
        return encoding;
    }
    
    /**
     * setter
     * 
     * @param encoding the encoding to set
     */
    public void setEncoding(String encoding)
    {
        this.encoding = encoding;
    }
    
    /**
     * Allow the default date format to be overridden by the caller.
     * 
     * @param fmt the date format to use when outputting dates
     */
    public void setDateFormat(DateFormat fmt)
    {
        if (config == null)
        {
            config = JOXConfig.getModifiableConfig();
            output.setConfig(config);
        }
        config.setDateFormat(fmt);
    }
    
    /** utility to create a ContentHandle (xmlSerializer) */
    private ContentHandler setupSink(OutputStream s)
    {
        org.apache.xml.serialize.OutputFormat f =
            new org.apache.xml.serialize.OutputFormat(
                org.apache.xml.serialize.Method.XML, this.encoding, false);
        
        return new org.apache.xml.serialize.XMLSerializer(s, f);
    }
    
    /**
     * Writes a bean as XML, using rootName as the tag name for the document
     * root. Other tag names will come from the names of the bean attributes.
     * 
     * @param rootName The name of the document root
     * @param ob The object to write out
     * @throws IOException If there is an error writing the object
     */
    public void writeObject(String rootName, Object ob)
        throws IOException
    {
        // Write out the bean as XML
        output.writeObject(rootName, ob);
    }
    
    /**
     * Write a string to the output stream. This method is used by the output
     * utility to write a string to either an output stream or a writer.
     * 
     * @param str The string to write
     * @throws IOException If there is an error writing the object
     */
    public void writeString(String str)
        throws IOException
    {
        write(str.getBytes());
    }
    
    /**
     * getter
     * 
     * @return return value
     */
    public JOXConfig getConfig()
    {
        return config;
    }
    
    /**
     * setter
     * 
     * @param aConfig aConfig
     */
    public void setConfig(JOXConfig aConfig)
    {
        config = aConfig;
    }
}
