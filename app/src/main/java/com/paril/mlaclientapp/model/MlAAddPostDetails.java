package com.paril.mlaclientapp.model;



        import java.io.Serializable;

public class MlAAddPostDetails implements Serializable {
    public String post;
    public String owner;
    public int groupId;



    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String Owner) {
        this.owner = owner;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }
}