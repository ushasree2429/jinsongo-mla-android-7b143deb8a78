package com.paril.mlaclientapp.model;

import java.io.Serializable;

public class MLAPostDetails implements Serializable {
    public String post;
    public String username;
    public int groupId;

    public String getGroupKey() {
        return groupKey;
    }

    public void setGroupKey(String groupKey) {
        this.groupKey = groupKey;
    }

    public String groupKey;
    public String getSessionkey() {
        return sessionKey;
    }

    public void setSessionkey(String sessionkey) {
        this.sessionKey = sessionKey;
    }

    public String sessionKey;
    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getusername() {
        return username;
    }

    public void setusername(String username) {
        this.username = username;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getPostType() {
        return postType;
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }

   public  String  postType;


    public MLAPostDetails(String post,String username,int groupId)
    {
        this.username=username;
        this.post=post;
        this.groupId=groupId;
    }

}
