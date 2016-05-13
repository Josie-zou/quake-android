package com.josie.earthquake.model;

import java.util.Date;

/**
 * Created by Josie on 16/5/12.
 */
public class User {
    private int id;
    private String username;
    private String nickName;
    private int privilege;
    private String password;
    private String desciption;
    private String phoneNumber;
    private String mailAdress;
    private String iconUrl;
    private String workPlace;
    private String positon;
    private String qq;
    private Date createTime;
    private Date lastUpdateTime;

    public static enum Privilege {

        Common(1), Admin(2), Root(3);

        private int value;

        Privilege(
                int value) {
            this.value = value;
        }

        public int toInt() {
            return value;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getPrivilege() {
        return privilege;
    }

    public void setPrivilege(int privilege) {
        this.privilege = privilege;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDesciption() {
        return desciption;
    }

    public void setDesciption(String desciption) {
        this.desciption = desciption;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getMailAdress() {
        return mailAdress;
    }

    public void setMailAdress(String mailAdress) {
        this.mailAdress = mailAdress;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getWorkPlace() {
        return workPlace;
    }

    public void setWorkPlace(String workPlace) {
        this.workPlace = workPlace;
    }

    public String getPositon() {
        return positon;
    }

    public void setPositon(String positon) {
        this.positon = positon;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", nickName='" + nickName + '\'' +
                ", privilege=" + privilege +
                ", password='" + password + '\'' +
                ", desciption='" + desciption + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", mailAdress='" + mailAdress + '\'' +
                ", iconUrl='" + iconUrl + '\'' +
                ", workPlace='" + workPlace + '\'' +
                ", positon='" + positon + '\'' +
                ", qq='" + qq + '\'' +
                ", createTime=" + createTime +
                ", lastUpdateTime=" + lastUpdateTime +
                '}';
    }
}
