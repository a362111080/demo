package com.zero.egg.tool;

import java.security.MessageDigest;

public class MD5Utils  {

    /**
     * 固定盐
     */
    private static String fixSalt = "wuzero";

    public static String encode(String string) throws Exception {
        byte[] hash = MessageDigest.getInstance("MD5").digest(
                string.getBytes("UTF-8"));
        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) {
                hex.append("0");
            }
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

    /**
     * 固定盐加密(redis key)
     * @param string
     * @return
     * @throws Exception
     */
    public static String encodeWithFixSalt(String string) throws Exception {
        return encode(string + fixSalt);
    }


}
