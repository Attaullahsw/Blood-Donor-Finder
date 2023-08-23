package com.example.bloodbank.dataholder;

public class FeedBackDataHolder {
    private int id;
    private String name;
    private String email;
    private String msg;
    private int status;

    public FeedBackDataHolder(int id, String name, String email, String msg,int status) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.msg = msg;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getMsg() {
        return msg;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
