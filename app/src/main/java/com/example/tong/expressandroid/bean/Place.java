package com.example.tong.expressandroid.bean;

import java.io.Serializable;

public class Place implements Serializable{
  private int placeid;
  private int userid;
  private String userName;
  private int sendid;
  private int placetype;

  public int getPlaceid() {
    return placeid;
  }

  public void setPlaceid(int placeid) {
    this.placeid = placeid;
  }

  public int getUserid() {
    return userid;
  }

  public void setUserid(int userid) {
    this.userid = userid;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public int getSendid() {
    return sendid;
  }

  public void setSendid(int sendid) {
    this.sendid = sendid;
  }

  public int getPlacetype() {
    return placetype;
  }

  public void setPlacetype(int placetype) {
    this.placetype = placetype;
  }
}
