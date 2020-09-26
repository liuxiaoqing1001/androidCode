package com.bignerdranch.android.sunflower.entity;

import java.util.UUID;

public class User {
//    public UUID id;
    public String name;
    public String password;

//    public User(){
//        id=UUID.randomUUID();
//    }

//    public UUID getId() {
//        return id;
//    }

//    public void setId(UUID id) {
//        this.id = id;
//    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
