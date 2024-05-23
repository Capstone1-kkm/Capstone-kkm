package com.example.capstone;

public class chatdata {
    private String nickname;
    private String msg;

    public chatdata() {
        // Default constructor required for calls to DataSnapshot.getValue(chatdata.class)
    }

    public chatdata(String nickname, String msg) {
        this.nickname = nickname;
        this.msg = msg;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}