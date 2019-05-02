package com.zero.egg.tool;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.zero.egg.config.FileUploadProperteis;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
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
    private static final int WIDTH = 300;
    // 二维码图片高度
    private static final int HEIGHT = 300;
    // 二维码的图片格式
    private static final String FORMAT = "png";

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

    public static String writeToFile(String targetAddr, String text, String name) throws Exception {
        makeDirPath(targetAddr);
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


}

