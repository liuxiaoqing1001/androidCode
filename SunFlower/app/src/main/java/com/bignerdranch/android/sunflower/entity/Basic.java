package com.bignerdranch.android.sunflower.entity;

public class Basic {
    String[] explains;

    public String[] getExplains() {
        return explains;
    }

    public String getStrings(String[] strings){
        String str = "";
        for (String s:strings
             ) {
            str += s+"\n";
        }
        return str;
    }
}
