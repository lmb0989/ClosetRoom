package com.closet.util;

public class TransferUtil {

    public static String null2String(String str){
            return str==null ? "" : str;
    }

    public static int null2Int(Integer i){
            return i==null ? 0 : i;
    }
}
