package com.zero.egg.enums;

/**
 * 计重方式枚举类
 *
 * @Author lyming
 * @Date 2018/11/9 16:39
 **/
public enum ModeTypeEnum {
    PEEL(1, "去皮"),
    WRAP(2, "包");

    public int key;
    public String type;

    ModeTypeEnum(int key, String type) {
        this.key = key;
        this.type = type;
    }

    public static String getValueByKey(int key) {
        String type = "";
        for (ModeTypeEnum modeTypeEnum : ModeTypeEnum.values()) {
            if (modeTypeEnum.key == key) {
                type = modeTypeEnum.type;
                break;
            }
        }
        return type;
    }

    public static int getKeyByValue(String type) {
        int key = 0;
        for (ModeTypeEnum modeTypeEnum : ModeTypeEnum.values()) {
            if (modeTypeEnum.type.equals(type)) {
                key = modeTypeEnum.key;
                break;
            }
        }
        return key;
    }
}
