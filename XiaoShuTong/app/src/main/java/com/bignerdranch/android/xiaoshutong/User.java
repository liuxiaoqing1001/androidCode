package com.bignerdranch.android.xiaoshutong;

public class User {
    public int _id;
    public String name;
    public String password;
    public int age;
    public String info;

    public User(){

    }

    public User(String name){
        this.name = name;
    }
    public User(String name,String password){
        this.name = name;
        this.password = password;
    }

    public User(String name,int age,String info){
        this.name = name;
        this.age = age;
        this.info = info;
    }
    public User(String name,String password,int age,String info){
        this.name = name;
        this.password = password;
        this.age = age;
        this.info = info;
    }


}
