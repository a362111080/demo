package com.zero.egg.tool;

import com.zero.egg.requestDTO.WXSessionModel;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * 对称加密工具
 *
 * @ClassName AESUtil
 * @Author lyming
 * @Date 2019/4/29 13:01
 **/
public class AESUtil {
    public static final String KEY_ALGORITHM = "AES";
    public static final String KEY_ALGORITHM_MODE = "AES/CBC/PKCS5Padding";
    public static final String KEY = "com.zero.AESUtil";


    /**
     * AES对称加密
     *
     * @param data
     * @param key  key需要16位
     * @return
     */
    public static String encrypt(String data, String key) {
        try {
            SecretKeySpec spec = new SecretKeySpec(key.getBytes("UTF-8"), KEY_ALGORITHM);
            Cipher cipher = Cipher.getInstance(KEY_ALGORITHM_MODE);
            cipher.init(Cipher.ENCRYPT_MODE, spec, new IvParameterSpec(new byte[cipher.getBlockSize()]));
            byte[] bs = cipher.doFinal(data.getBytes("UTF-8"));
            return Base64Util.encode(bs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * AES对称解密 key需要16位
     *
     * @param data
     * @param key
     * @return
     */
    public static String decrypt(String data, String key) {
        try {
            SecretKeySpec spec = new SecretKeySpec(key.getBytes("UTF-8"), KEY_ALGORITHM);
            Cipher cipher = Cipher.getInstance(KEY_ALGORITHM_MODE);
            cipher.init(Cipher.DECRYPT_MODE, spec, new IvParameterSpec(new byte[cipher.getBlockSize()]));
            byte[] originBytes = Base64Util.decode(data);
            byte[] result = cipher.doFinal(originBytes);
            return new String(result, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void main(String[] args) throws Exception {
        String token = "1IteUKE/BDItDe+Jr9PZt3glmmtzMDlb4RjS46/WJwVMwOGG/k0KAZCDIHAd T2MVMsV4ol8/zBxjHxlc2yResuc5RShBUPih1DNPf8VrvKjZG4Vnub/g9nMb 3N76PCmu";
        String obj = decrypt(token, KEY);
        WXSessionModel wxSessionModel = JsonUtils.jsonToPojo(obj, WXSessionModel.class);
        System.out.println(wxSessionModel.getOpenid());
        System.out.println(wxSessionModel.getSession_key());
        //olhaX5AH879-fkuVJtDp4KpvgqAM
        //2CJZZRTrlB/XLcUDrpaXKA==
    }
}
