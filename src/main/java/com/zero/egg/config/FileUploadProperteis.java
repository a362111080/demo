package com.zero.egg.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 文件上传相关配置
 *
 * @ClassName FileUploadProperteis
 * @Author lyming
 * @Date 2019/5/2 18:38
 **/
@Configuration
public class FileUploadProperteis {

    private static String separator = System.getProperty("file.separator");

    //静态资源对外暴露的访问路径
    private static String staticAccessPath;

    //文件上传目录
    private static String uploadFolder;

    //二维码文件相对目录
    private static String matrixPath;

    @Value("${file.staticAccessPath}")
    public void setStaticAccessPath(String staticAccessPath) {
        FileUploadProperteis.staticAccessPath = staticAccessPath;
    }

    @Value("${file.uploadFolder}")
    public void setUploadFolder(String uploadFolder) {
        FileUploadProperteis.uploadFolder = uploadFolder;
    }

    @Value("${matrix.relevant.path}")
    public void setMatrixPath(String matrixPath) {
        FileUploadProperteis.matrixPath = matrixPath;
    }

    public String getStaticAccessPath() {
        return staticAccessPath;
    }

    public String getUploadFolder() {
        return uploadFolder;
    }

    public String getMatrixPath() {
        return matrixPath;
    }

    public static String getUploadImgBasePath() {
        String basePath = uploadFolder;
        basePath = basePath.replace("/", separator);
        return basePath;
    }

    public static String getMatrixImagePath(String companyId, String shopId, String supplierId, String categoryId) {
        String imagePath = matrixPath + "/" + companyId + "/" + shopId + "/" + supplierId + "/" + categoryId + "/";
        return imagePath.replace("/", separator);
    }
}
