package com.paril.mlaclientapp.model;

import android.app.Application;

import java.security.Key;


public class Global extends Application {
    private int data;
    private  String Username;

    public Key getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(Key privateKey) {
        this.privateKey = privateKey;
    }

    private Key privateKey;

    public int getData(){
        return this.data;
    }

    public void setData(int d){
        this.data=d;
    }

    public  String getUsername(){return  this.Username;}

    public  void setUsername(String S){this.Username=S;}
}