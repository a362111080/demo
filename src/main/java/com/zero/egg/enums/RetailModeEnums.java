package com.zero.egg.enums;

public enum RetailModeEnums {

    POUNDS(1, "斤"),
    SINGLE(2, "个"),
    DISHES(3, "盘");

    public int key;
    public String mode;

    RetailModeEnums(int key, String mode) {
        this.key = key;
        this.mode = mode;
    }

    public static String getValueByKey(int key) {
        String type = "";
        for (RetailModeEnums retailModeEnums : RetailModeEnums.values()) {
            if (retailModeEnums.key == key) {
                type = retailModeEnums.mode;
                break;
            }
        }
        return type;
    }

    public static int getKeyByValue(String mode) {
        int key = 0;
        for (RetailModeEnums retailModeEnums : RetailModeEnums.values()) {
            if (retailModeEnums.mode.equals(mode)) {
                key = retailModeEnums.key;
                break;
            }
        }
        return key;
    }
}
