package com.paril.mlaclientapp.model;

/**
 * Created by paril on 7/12/2017.
 */
public class MlAFriendUsers
{
    public int userId ;
    public String userName ;
    public String userType ;
    Boolean check;
   // public int loggedId;


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public Boolean getCheck() {
        return check;
    }

    public void setCheck(Boolean check) {
        this.check = check;
    }


    public MlAFriendUsers(int userId, String userName ,String userType, Boolean check) {

        this.userId = userId;
        this.userName = userName;
        this.userType = userType;
        this.check = check;


    }
}
