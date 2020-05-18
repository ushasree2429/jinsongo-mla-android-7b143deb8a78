package com.paril.mlaclientapp.model;

import java.io.Serializable;

public class MLAGroupDetails implements Serializable {
    public String group_name;
    public Integer[] friend_ids;
    public Integer group_id;
    public int userId;
    public String groupkey;
    Boolean check;

    public Integer getGroup_id() {
        return group_id;
    }

    public void setGroup_id(Integer group_id) {
        this.group_id = group_id;
    }
    public String getGroupName() {
        return group_name;
    }

    public void setGroupName(String group_name) {
        this.group_name = group_name;
    }

    public int getUserid(){ return userId;}

    public void setUserId(int userId){this.userId=userId;}

    public Integer[] getFriend_ids() {
        return friend_ids;
    }

    public void setFriend_ids(Integer[] friend_ids) {
        this.friend_ids = friend_ids;
    }

    public String getGroupKey() {
        return groupkey;
    }

    public void setGroupKey(String groupkey) {
        this.groupkey = groupkey;
    }

    public Boolean getCheck() {
        return check;
    }

    public void setCheck(Boolean check) {
        this.check = check;
    }

    public MLAGroupDetails(String group_name, int group_id,Boolean check)
    {
        this.group_name=group_name;
        this.check=check;
        this.group_id=group_id;

    }

    public MLAGroupDetails(String group_name,Boolean check)
    {
        this.group_name=group_name;
        this.check=check;

    }

    public MLAGroupDetails(String group_name)
    {
        this.group_name=group_name;


    }
}
