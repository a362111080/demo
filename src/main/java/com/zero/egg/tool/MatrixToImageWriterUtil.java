package com.zero.egg.tool;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.Result;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.zero.egg.config.FileUploadProperteis;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Hashtable;


/*
 * @ClassName MatrixToImageWriterUtil
 * @Description 二维码生成工具(不带logo)
 * @Author lyming
 * @Date 2019/5/3 1:19
 **/
@Slf4j
public class MatrixToImageWriterUtil {
    private static final int BLACK = 0xFF000000;
    private static final int WHITE = 0xFFFFFFFF;
    // 二维码图片宽度
    private static final int WIDTH = 600;
    // 二维码图片高度
    private static final int HEIGHT = 400;
    // 二维码的图片格式
    private static final String FORMAT = "png";
    //字体大小
    private static final int FONTSIZE = 32;
    //字体类型
    private static final int FONTSTYLE = 1;
    private static String basePath = FileUploadProperteis.getUploadImgBasePath();

    private MatrixToImageWriterUtil() {
    }

    public static BufferedImage toBufferedImage(BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
            }
        }
        return image;
    }

    public static synchronized String writeToFile(String targetAddr, String text, String name, String shopName, String categoryName, String currentCode) throws Exception {
        makeDirPath(targetAddr);
        /**
         * 对二维码数据进行加密
         */
//        String aesText = AESUtil.encrypt(text, AESUtil.KEY);
        String relativeAddr = targetAddr + name + "." + FORMAT;
        log.debug("current relativeAddr is:" + relativeAddr);
        Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");    // 内容所使用字符集编码
        BitMatrix bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, WIDTH, HEIGHT, hints);
        log.debug(basePath + relativeAddr);
        File outputFile = new File(basePath + relativeAddr);
        BufferedImage image = toBufferedImage(bitMatrix);
        if (!ImageIO.write(image, FORMAT, outputFile)) {
            throw new IOException("Could not write an image of format " + FORMAT + " to " + outputFile);
        }
        pressTextShop("店铺:" + shopName, outputFile);
        pressTextCategoty("品种:" + categoryName, outputFile);
        if (null != currentCode) {
            pressTextGoodNo(currentCode, outputFile);
        }
        return relativeAddr;
    }

    public static void writeToStream(BitMatrix matrix, String format, OutputStream stream) throws IOException {
        BufferedImage image = toBufferedImage(matrix);
        if (!ImageIO.write(image, format, stream)) {
            throw new IOException("Could not write an image of format " + format);
        }
    }

    /**
     * 创建目标路径所涉及到的目录
     *
     * @param targetAddr
     */
    private static void makeDirPath(String targetAddr) {
        String realFileParentPath = basePath + targetAddr;
        File dirPath = new File(realFileParentPath);
        if (!dirPath.exists()) {
            dirPath.mkdirs();
        }
    }

    /**
     * 解析二维码
     *
     * @param file 二维码图片
     * @return
     * @throws Exception
     */
    public static String decode(File file) throws Exception {
        BufferedImage image;
        image = ImageIO.read(file);
        if (image == null) {
            return null;
        }
        BufferedImageLuminanceSource source = new BufferedImageLuminanceSource(
                image);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        Result result;
        Hashtable<DecodeHintType, Object> hints = new Hashtable<DecodeHintType, Object>();
        hints.put(DecodeHintType.CHARACTER_SET, "utf-8");
        result = new MultiFormatReader().decode(bitmap, hints);
        String resultStr = result.getText();
//        return AESUtil.decrypt(resultStr, AESUtil.KEY);
        return resultStr;
    }

    /**
     * 给二维码上方图片加上文字(商品编码)
     *
     * @param pressText 文字
     * @param qrFile    二维码文件
     */
    public static void pressTextGoodNo(String pressText, File qrFile) throws Exception {
        pressText = new String(pressText.getBytes(), "utf-8");
        Image src = ImageIO.read(qrFile);
        int imageW = src.getWidth(null);
        int imageH = src.getHeight(null);
        BufferedImage image = new BufferedImage(imageW, imageH, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.createGraphics();
        g.drawImage(src, 0, 0, imageW, imageH, null);
        //设置画笔的颜色
        g.setColor(Color.BLACK);
        //设置字体
        Font font = new Font("宋体", FONTSTYLE, FONTSIZE);
        FontMetrics metrics = g.getFontMetrics(font);
        //文字在图片中的坐标
//        int startX = (imageW - metrics.stringWidth(pressText)) / 2;
//        int startY = metrics.getHeight() + 16;
        int startX = (imageH - metrics.stringWidth(pressText)) / 2 + 16;
        int startY = metrics.getHeight() + 40;
        g.setFont(font);
        int strlength = pressText.length();
        int t = 24;
        //竖排文字
        for (int i = 0; i < strlength; i++) {
            g.drawString(String.valueOf(pressText.charAt(i)), startX, startY);
            startY += t;
        }
//        g.drawString(pressText, startX, startY);
        g.dispose();
        FileOutputStream out = new FileOutputStream(qrFile);
        ImageIO.write(image, "png", out);
        out.close();
    }

    /**
     * 给二维码上方图片加上文字(品种)
     *
     * @param pressText 文字
     * @param qrFile    二维码文件
     */
    public static void pressTextCategoty(String pressText, File qrFile) throws Exception {
        pressText = new String(pressText.getBytes(), "utf-8");
        Image src = ImageIO.read(qrFile);
        int imageW = src.getWidth(null);
        int imageH = src.getHeight(null);
        BufferedImage image = new BufferedImage(imageW, imageH, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.createGraphics();
        g.drawImage(src, 0, 0, imageW, imageH, null);
        //设置画笔的颜色
        g.setColor(Color.BLACK);
        //设置字体
        Font font = new Font("宋体", FONTSTYLE, FONTSIZE);
        FontMetrics metrics = g.getFontMetrics(font);
        //文字在图片中的坐标
        int startX = (imageW - metrics.stringWidth(pressText)) / 2;
        int startY = imageH - metrics.getHeight() / 2;
        g.setFont(font);
        g.drawString(pressText, startX, startY);
        g.dispose();
        FileOutputStream out = new FileOutputStream(qrFile);
        ImageIO.write(image, "png", out);
        out.close();
    }

    /**
     * 给二维码下方图片加上文字(店铺名)
     *
     * @param pressText 文字
     * @param qrFile    二维码文件
     */
    public static void pressTextShop(String pressText, File qrFile) throws Exception {
        pressText = new String(pressText.getBytes(), "utf-8");
        Image src = ImageIO.read(qrFile);
        int imageW = src.getWidth(null);
        int imageH = src.getHeight(null);
        BufferedImage image = new BufferedImage(imageW, imageH, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.createGraphics();
        g.drawImage(src, 0, 0, imageW, imageH, null);
        //设置画笔的颜色
        g.setColor(Color.BLACK);
        //设置字体
        Font font = new Font("宋体", FONTSTYLE, FONTSIZE);
        FontMetrics metrics = g.getFontMetrics(font);
        //文字在图片中的坐标
        int startX = (imageW - metrics.stringWidth(pressText)) / 2;
        int startY = metrics.getHeight();
        g.setFont(font);
        g.drawString(pressText, startX, startY);
        g.dispose();
        FileOutputStream out = new FileOutputStream(qrFile);
        ImageIO.write(image, "png", out);
        out.close();
    }

    /**
     * 解析二维码
     *
     * @param path 二维码图片地址
     * @return
     * @throws Exception
     */
    public static String decode(String path) throws Exception {
        return MatrixToImageWriterUtil.decode(new File(basePath + path));
    }

}

