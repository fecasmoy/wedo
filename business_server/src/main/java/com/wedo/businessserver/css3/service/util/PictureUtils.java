/**
 * 
 */
package com.wedo.businessserver.css3.service.util;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sun.image.codec.jpeg.ImageFormatException;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * 图片处理工具类
 * 
 * @author x90003512
 * 
 */
public class PictureUtils
{

    /**
     * 日志
     */
    private static final Log logger = LogFactory
        .getLog(PictureUtils.class);

    /**
     * 默认长度
     */
    private static final int LENGTH_2 = 2;

    /**
     * 对文件进行大小重置
     * 
     * @param srcFileName srcFileName
     * @param targetFileName targetFileName
     * @param width width
     * @param height heigh
     */
    public static final void resize(String srcFileName, String targetFileName,
        int width, int height)
    {
        FileOutputStream newImageOut = null;
        try
        {
            logger
                .debug("Start resize picture, file name is : " + srcFileName
                    + ", and size : " + width + ":" + height
                    + ", target file name is : " + targetFileName + ".");
            File srcFile = new File(srcFileName);
            // 构造Image对象
            Image image = ImageIO
                .read(srcFile);
            BufferedImage bufferImage =
                new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            // 绘制缩小后的图
            bufferImage
                .getGraphics().drawImage(image, 0, 0, width, height, null);
            // 输出到文件流
            newImageOut = new FileOutputStream(targetFileName);
            JPEGImageEncoder encoder = JPEGCodec
                .createJPEGEncoder(newImageOut);
            // 近JPEG编码
            encoder
                .encode(bufferImage);
            logger
                .debug("resize success!");
        }
        catch (FileNotFoundException e)
        {
            // 文件没有找到
            logger
                .error("file not found.", e);
            logger
                .debug(e
                    .getStackTrace());
        }
        catch (ImageFormatException e)
        {
            // 图片文件格式不匹配
            logger
                .error("file format no match.", e);
            logger
                .debug(e
                    .getStackTrace());
        }
        catch (IOException e)
        {
            // 图片文件读写错误
            logger
                .error("file write error.", e);
            logger
                .debug(e
                    .getStackTrace());
        }
        finally
        {
            if (newImageOut != null)
            {
                try
                {
                    newImageOut
                        .close();
                }
                catch (IOException e)
                {
                    // TODO Auto-generated catch block
                    logger
                        .debug(e
                            .getStackTrace());
                }
            }
        }
    }

    /**
     * 图片添加水印
     * 
     * @param markText 水印文字
     * @param targetImage 待添加水印的图片
     * @param fontName 字体名称
     * @param fontStyle 文字风格（加粗、斜体）
     * @param color 文字颜色
     * @param fontSize 字体大小
     * @param x 字体位置的横坐标
     * @param y 字体位置的纵坐标
     * @param alpha 字体的透明度
     */
    public static final void waterMark(String markText, String targetImage,
        String fontName, int fontStyle, Color color, int fontSize, float x,
        float y, float alpha)
    {
        // TODO 图片水印
        try
        {
            File img = new File(targetImage);
            Image src = ImageIO
                .read(img);
            int width = src
                .getWidth(null);
            int height = src
                .getHeight(null);
            logger
                .debug("height : " + height + " -width : " + width);
            BufferedImage image =
                new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = image
                .createGraphics();
            g
                .drawImage(src, 0, 0, width, height, null);
            g
                .setColor(color);
            g
                .setFont(new Font(fontName, fontStyle, fontSize));
            g
                .setComposite(AlphaComposite
                    .getInstance(AlphaComposite.SRC_ATOP, alpha));
            logger
                .debug("width :  " + (width * x));
            logger
                .debug("height : " + (height * y));
            g
                .drawString(markText, width * x, height * y);
            g
                .dispose();
            ImageIO
                .write((BufferedImage) image, "jpg", img);
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            logger
                .error("read file fail");
            logger
                .debug(e
                    .getStackTrace());
        }
    }

    /**
     * 获取文本串的长度
     * 
     * @param text 文本串
     * @return int
     */
    private static int getLength(String text)
    {
        int length = 0;
        for (int i = 0; i < text
            .length(); i++)
        {
            if ((text
                .charAt(i) + "")
                .getBytes().length > 1)
            {
                length += LENGTH_2;
            }
            else
            {
                length += 1;
            }
        }
        return length / LENGTH_2;
    }
    //
    // /**
    // * 测试用入口方法
    // *
    // * @param args args
    // */
    // public static void main(String[] args)
    // {
    // PictureUtils
    // .resize("c:\\test1.jpg", "c:\\test2.jpg", 1, 1);
    // // PictureUtils
    // // .waterMark("HELLO world", "c:\\test1.jpg", "Courier New",
    // // Font.ITALIC, Color.BLUE, 20, 0.75f, 0.8f, 0.4f);
    // }
}
