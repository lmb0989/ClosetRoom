package com.closet.util;

import java.util.ArrayList;

public class StringUtil {
	
    public static ArrayList<Integer> string2List(String str, String regex){
        ArrayList<Integer> list = new ArrayList<Integer>();
        for(String s : str.split(regex)){
            if(!s.isEmpty()){
                list.add(Integer.parseInt(s));
            }
        }
        return list;
    }

    public static String list2String(ArrayList<Integer> videoIDS, String regex){
        String result = "";
        for(Integer o : videoIDS){
                result = o.toString() + regex + result;
        }
        return result;
    }
}
