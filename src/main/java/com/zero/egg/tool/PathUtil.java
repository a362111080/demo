package com.zero.egg.tool;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName PathUtil
 * @Description TODO
 * @Author lyming
 * @Date 2019/9/17 10:51 上午
 **/
@Configuration
public class PathUtil {
    private static String separator = System.getProperty("file.separator");

    private static String linuxPath;
    private static String shopPath;

    @Value("${file.uploadFolder}")
    public void setLinuxPath(String linuxPath) {
        PathUtil.linuxPath = linuxPath;
    }

    @Value("${shop.relevant.path}")
    public void setShopPath(String shopPath) {
        PathUtil.shopPath = shopPath;
    }

    public static String getImgBasePath() {
        String os = System.getProperty("os.name");
        String basePath = linuxPath;
        basePath = basePath.replace("/", separator);
        return basePath;
    }

    public static String getShopImagePath(String shopId) {
        String imagePath = shopPath + shopId + "/";
        return imagePath.replace("/", separator);
    }
}
