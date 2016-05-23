package com.josie.earthquake.model;

/**
 * Created by Josie on 16/5/22.
 */
public class WhiteList {
    private int id;
    private String url;
    private int operator;
    private String username;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getOperator() {
        return operator;
    }

    public void setOperator(int operator) {
        this.operator = operator;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "WhiteList{" +
                "id=" + id +
                ", url='" + url + '\'' +
                ", operator=" + operator +
                ", username='" + username + '\'' +
                '}';
    }
}
