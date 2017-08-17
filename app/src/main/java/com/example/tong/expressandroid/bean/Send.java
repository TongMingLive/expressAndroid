package com.example.tong.expressandroid.bean;

import java.io.Serializable;

public class Send implements Serializable{
  private int sendid;
  private int companyid;
  private String companyName;
  private int userid;
  private String userName;
  private String userphone;
  private String useraddress;
  private int sendtype;
  private Double sendappraise;
  private String sendpage;

  public int getSendid() {
    return sendid;
  }

  public void setSendid(int sendid) {
    this.sendid = sendid;
  }

  public int getCompanyid() {
    return companyid;
  }

  public void setCompanyid(int companyid) {
    this.companyid = companyid;
  }

  public String getCompanyName() {
    return companyName;
  }

  public void setCompanyName(String companyName) {
    this.companyName = companyName;
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

  public String getUserphone() {
    return userphone;
  }

  public void setUserphone(String userphone) {
    this.userphone = userphone;
  }

  public String getUseraddress() {
    return useraddress;
  }

  public void setUseraddress(String useraddress) {
    this.useraddress = useraddress;
  }

  public int getSendtype() {
    return sendtype;
  }

  public void setSendtype(int sendtype) {
    this.sendtype = sendtype;
  }

  public double getSendappraise() {
    return sendappraise;
  }

  public void setSendappraise(double sendappraise) {
    this.sendappraise = sendappraise;
  }

  public String getSendpage() {
    return sendpage;
  }

  public void setSendpage(String sendpage) {
    this.sendpage = sendpage;
  }
}
