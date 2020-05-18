package com.paril.mlaclientapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by paril.shah on 22-Oct-17.
 */

public class MLAFriendAddedData {



    private int userId;
    private int FriendUserId;



    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getFriendUserId() {
        return FriendUserId;
    }

    public void setFriendUserId(int logged_user_id) {
        this.FriendUserId = logged_user_id;
    }


}
