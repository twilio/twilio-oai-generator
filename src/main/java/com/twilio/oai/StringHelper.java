package com.twilio.oai;

import java.util.HashSet;

public class StringHelper {

    public static String titleCase(String input){
        input.replace(" ","");
        String[] split = input.split("_");
        String result = "";
        for(int i = 0; i < split.length; i++){
            String item = split[i];
            result+= Character.toTitleCase(item.charAt(0)) + item.substring(1);
        }
        return result;
    }

    public static boolean existInSetIgnoreCase(String item, HashSet<String> set){
        for (String s : set) {
            if(s.equalsIgnoreCase(item)){
                return true;
            }
        }
        return false;
    }

}